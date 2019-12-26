package components;

import business.Order;
import business.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class PaginationControl {

    private WebDriver driver;
    private Product product;
    private Order order;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/*/app-pagination/div/ul")
    private WebElement paginationControl;

    public PaginationControl(WebDriver driver, Product productImpl, Order orderImpl) {
        this.driver = driver;
        this.product = productImpl;
        this.order = orderImpl;
    }

    /*
     * This function verifies the number of pages that should be present in the control
     * The status (enabled - disabled) of Previous button based on the current page number
     * The status (enabled - disabled) of Next button based on the current page number
     * The selected page number and it is status
     * */
    public String verifyPaginationControlStatus(int pageNumber, int pageSize) {
        StringBuilder result = new StringBuilder();
        try {
            int dataSize = 0;
            if (driver.getCurrentUrl().equals("http://localhost:4200/order"))
                dataSize = this.order.getAllOrders().size();
            else
                dataSize = this.product.getProducts().size();
            int expectedNumberOfPages =
                    (dataSize % pageSize == 0) ? dataSize / pageSize : (dataSize / pageSize) + 1;
            int expectedNumberOfPaginationControlActions = expectedNumberOfPages + 2;
            if (expectedNumberOfPaginationControlActions != getPaginationActualControlsSize())
                result.append("Incorrect number of pages in pagination control or missing previous / next buttons\n");

            String expectedStatusOfPreviousButton = (pageNumber == 1) ? "page-item  disabled" : "page-item";
            if (!expectedStatusOfPreviousButton.equals(getActualStatusOfPreviousButton()))
                result.append("Incorrect enable-disable status of previous button\n");
            if (!"Previous".equals(getPreviousButtonActualText()))
                result.append("Incorrect text or position of previous button\n");

            if (pageNumber != getActualActivePageNumber())
                result.append("Incorrect selected page number in the pagination control\n");

            String expectedStatusOfNextButton =
                    (pageNumber == expectedNumberOfPages) ? "page-item disabled" : "page-item";
            if (!expectedStatusOfNextButton.equals(getActualStatusOfNextButton()))
                result.append("Incorrect enable-disable status of next button\n");
            if (!"Next".equals(getNextButtonActualText()))
                result.append("Incorrect text or position of next button\n");

        } catch (SQLException | ClassNotFoundException e) {
            result.append("Exception while getting products\n")
                    .append(e.getMessage())
                    .append("\n").append(e.getLocalizedMessage()).append("\n").append(e.getClass());
        }
        return (!result.toString().trim().isEmpty()) ? result.toString() : "";
    }

    private int getPaginationActualControlsSize() {
        return paginationControl.findElements(By.tagName("li")).size();
    }

    private String getActualStatusOfPreviousButton() {
        return getPaginationControlElements().get(0).getAttribute("class").trim();
    }

    private String getActualStatusOfNextButton() {
        return getPaginationControlElements().get(getPaginationControlElements().size() - 1)
                .getAttribute("class").trim();
    }

    private String getNextButtonActualText() {
        return getPaginationControlElements().
                get(getPaginationControlElements().size() - 1).findElement(By.tagName("a")).getText();
    }

    private java.util.List<WebElement> getPaginationControlElements() {
        return paginationControl.findElements(By.tagName("li"));
    }

    private String getPreviousButtonActualText() {
        return getPaginationControlElements().get(0).findElement(By.tagName("a")).getText().trim();
    }

    private int getActualActivePageNumber() {
        String activePageNumber = getPaginationControlElements().stream().
                filter(element -> element.getAttribute("class").trim().equals("page-item active")).
                map(element -> element.findElement(By.tagName("button")).getText()).collect(Collectors.toList()).get(0);
        return Integer.parseInt(activePageNumber);
    }

    public boolean clickNextButton() throws InterruptedException {
        Thread.sleep(100);
        if (!getPaginationControlElements().
                get(getPaginationControlElements().size() - 1).getAttribute("class").equals("page-item disabled ")) {
            getPaginationControlElements().
                    get(getPaginationControlElements().size() - 1).findElement(By.tagName("a")).click();
            return true;
        }
        return false;
    }
}
