package pages;

import business.Product;
import business.ProductImpl;
import common.WebElementVerifier;
import components.NavigationBar;
import components.PaginationControl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static common.Constants.PASS;

public class HomePage implements PaginatedShoppingPortalPage {
    private WebDriver driver;
    private WebElementVerifier webElementVerifier;
    private NavigationBar navigationBar;
    private Product productImpl;
    private PaginationControl paginationControl;

    @FindBy(how = How.CLASS_NAME, using = "col-lg-4")
    private List<WebElement> productCard;

    @FindBy(how = How.CLASS_NAME, using = "card-img-top")
    private List<WebElement> productImage;

    @FindBy(how = How.CLASS_NAME, using = "card-title")
    private List<WebElement> productTile;

    @FindBy(how = How.CLASS_NAME, using = "card-test")
    private List<WebElement> productDesc;

    @FindBy(how = How.CLASS_NAME, using = "card-text")
    private List<WebElement> productPriceStock;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-card/h1")
    private WebElement title;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-card/div/*/div/div/a[@class='btn btn-primary btn-lg']")
    private List<WebElement> getIt; // get only available products

    public HomePage(WebDriver driver, WebElementVerifier webElementVerifier,
                    Product productImpl, PaginationControl paginationControl, NavigationBar navigationBar) {
        this.productImpl = productImpl;
        this.driver = driver;
        this.navigationBar = navigationBar;
        this.webElementVerifier = webElementVerifier;
        this.paginationControl = paginationControl;
    }

    @Override
    public String verifyPageElements(int pageNumber, int pageSize) {
        StringBuilder result = new StringBuilder();
        webElementVerifier.isTextCorrectlyDisplayed(title, "home page title",
                result, "Get Whatever You Want!");
        verifyProductsInfoOfCurrentPage(result, pageSize, pageNumber);
        result.append(navigationBar.verifyNavigationBarElements()).append("\n");
        result.append(paginationControl.verifyPaginationControlStatus(pageNumber, pageSize)).append("\n");
        try {
            verifyProductsSizeOfAllPages(result, pageSize);
        } catch (InterruptedException e) {
            result.append("InterruptedException failed to click a page while pagination").append(pageNumber).append("\n");
        }

        return (!result.toString().trim().isEmpty()) ? result.toString() : PASS;
    }

    private void verifyProductsInfoOfCurrentPage(StringBuilder result, int pageSize, int pageNumber) {
        try {
            List<Product> expectedProducts = productImpl.getProducts();
            List<Product> actualProducts = getActualProductsOfCurrentPage();
            int numberOfMatchedProductsInCurrentPage = 0;
            for (Product expectedProduct : expectedProducts) {
                for (Product actualProduct : actualProducts) {
                    ProductImpl expected = (ProductImpl) expectedProduct;
                    ProductImpl actual = (ProductImpl) actualProduct;
                    if (actual.getName().equals(expected.getName())
                            && verifyTwoProductsEqual(expected, actual))
                        numberOfMatchedProductsInCurrentPage++;
                }
            }
            if (numberOfMatchedProductsInCurrentPage != pageSize)
                result.append("Incorrect product info is being displayed in page:").append(pageNumber).append("\n");
        } catch (SQLException e) {
            result.append("Failed to get expected products from db\n");
        } catch (ClassNotFoundException e) {
            result.append("Class not found while getting expected products from db\n");
        }
    }

    private boolean verifyTwoProductsEqual(ProductImpl expected, ProductImpl actual) {
        return expected.getImage().equals(actual.getImage()) &&
                expected.getDescription().equals(actual.getDescription()) &&
                expected.getPrice() == actual.getPrice() &&
                expected.getQuantity() == actual.getQuantity();
    }

    private void verifyProductsSizeOfAllPages(StringBuilder result, int pageSize) throws InterruptedException {
        try {
            List<Product> expectedProducts = new ProductImpl().getProducts();
            if (expectedProducts.size() != getAllPagesProductsSize(pageSize))
                result.append("Missing products from home page, not all products are being displayed\n");
        } catch (SQLException e) {
            result.append("Failed to get expected products from db\n");
        } catch (ClassNotFoundException e) {
            result.append("Class not found while getting expected products from db\n");
        }
    }

    private List<Product> getActualProductsOfCurrentPage() {
        List<Product> products = new ArrayList<>();
        List<String> stocks = extractProductStock();
        List<String> prices = extractProductPrice();
        for (int i = 0; i < productCard.size(); i++) {
            ProductImpl p = new ProductImpl();
            String pDescAndLabel = productDesc.get(i).getText();
            String pDescription = pDescAndLabel.substring(
                    pDescAndLabel.indexOf(":") + 1).trim();
            p.setImage(productImage.get(i).getAttribute("src"));
            p.setName(productTile.get(i).getText());
            p.setDescription(pDescription);
            p.setPrice(Double.parseDouble(prices.get(i)));
            p.setQuantity(Integer.parseInt(stocks.get(i)));
            products.add(p);
        }
        return products;
    }

    private List<String> extractProductPrice() {
        return productPriceStock.stream().
                filter(element -> element.getText().contains("Price: "))
                .map(element -> element.getText()
                        .substring(element.getText().indexOf("$") + 1, element.getText().length() - 1))
                .collect(Collectors.toList());
    }

    private List<String> extractProductStock() {
        return productPriceStock.stream().
                filter(element -> element.getText().contains("Stock: "))
                .map(element -> element.getText().substring(element.getText().indexOf(":") + 2))
                .collect(Collectors.toList());
    }

    private int getAllPagesProductsSize(int pageSize) throws InterruptedException {
        int actualProductsSize = 0;
        this.driver.get("http://localhost:4200/product?page=1&size=" + pageSize);
        actualProductsSize += this.driver.findElements(By.className("col-lg-4")).size();
        while (paginationControl.clickNextButton()) {
            actualProductsSize += this.driver.findElements(By.className("col-lg-4")).size();
        }
        return actualProductsSize;
    }

    public String getRandomProduct() {
        int availableProducts = getIt.size();
        int randomIndex = (int) (Math.random() * availableProducts);
        String productId = getIt.get(randomIndex).getAttribute("href")
                .substring(getIt.get(randomIndex).getAttribute("href").lastIndexOf("/") + 1);
        getIt.get(randomIndex).click();
        return productId;
    }

    public void getSpecificProduct(String productId) {
        getIt.forEach(element -> {
            if (element.getAttribute("href").equals("/product/" + productId))
                element.click();
        });
    }
}
