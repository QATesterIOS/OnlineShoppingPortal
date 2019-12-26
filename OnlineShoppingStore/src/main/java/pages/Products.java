package pages;

import business.Product;
import business.ProductImpl;
import common.WebElementVerifier;
import components.NavigationBar;
import components.PaginationControl;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static common.Constants.PASS;

public class Products implements PaginatedShoppingPortalPage {
    private WebElementVerifier webElementVerifier;
    private NavigationBar navigationBar;
    private Product productImpl;
    private PaginationControl paginationControl;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product/h1")
    private WebElement title;

    @FindBy(how = How.XPATH, using = "//*[@id=\"table\"]/thead/tr/*")
    private List<WebElement> tableHeaders;

    @FindBy(how = How.XPATH, using = "//*[@id=\"table\"]/tbody/tr[*]/th/img")
    private List<WebElement> productImages;

    @FindBy(how = How.XPATH, using = "//*[@id=\"table\"]/tbody/tr[*]/td[1]")
    private List<WebElement> productCodes;

    @FindBy(how = How.XPATH, using = "//*[@id=\"table\"]/tbody/tr[*]/td[2]")
    private List<WebElement> productNames;

    @FindBy(how = How.XPATH, using = "//*[@id=\"table\"]/tbody/tr[*]/td[3]")
    private List<WebElement> productTypes;

    @FindBy(how = How.XPATH, using = "//*[@id=\"table\"]/tbody/tr[*]/td[4]")
    private List<WebElement> productDesc;

    @FindBy(how = How.XPATH, using = "//*[@id=\"table\"]/tbody/tr[*]/td[5]")
    private List<WebElement> productPrices;

    @FindBy(how = How.XPATH, using = "//*[@id=\"table\"]/tbody/tr[*]/td[6]")
    private List<WebElement> productStocks;

    @FindBy(how = How.XPATH, using = "//*[@id=\"table\"]/tbody/tr[*]/td[7]")
    private List<WebElement> productStatuses;

    @FindBy(how = How.XPATH, using = "//*[@id=\"table\"]/tbody/tr[*]/td[8]/a")
    private List<WebElement> editProductLinks;

    public Products(WebElementVerifier webElementVerifier, NavigationBar navigationBar,
                    Product productImpl, PaginationControl paginationControl) {
        this.navigationBar = navigationBar;
        this.paginationControl = paginationControl;
        this.productImpl = productImpl;
        this.webElementVerifier = webElementVerifier;
    }

    @Override
    public String verifyPageElements(int pageNumber, int pageSize) {
        StringBuilder result = new StringBuilder();
        result.append(navigationBar.verifyNavigationBarElements());
        result.append(paginationControl.verifyPaginationControlStatus(pageNumber, pageSize));
        verifyTableHeaders(result);
        try {
            verifyFirstProductInfo(result);
        } catch (SQLException e) {
            result.append("SQLException, failed to get product info from db\n").append(e.getMessage()).append("\n");
        } catch (ClassNotFoundException e) {
            result.append("ClassNotFoundException, failed to get product info from db\n")
                    .append(e.getMessage()).append("\n");
        }
        return (!result.toString().trim().isEmpty()) ? result.toString() : PASS;
    }

    private void verifyTableHeaders(StringBuilder result) {
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(0),
                "PhotoHeader", result, "Photo");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(1),
                "CodeHeader", result, "Code");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(2),
                "NameHeader", result, "Name");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(3),
                "TypeHeader", result, "Type");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(4),
                "DescriptionHeader", result, "Description");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(5),
                "PriceHeader", result, "Price");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(6),
                "StockHeader", result, "Stock");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(7),
                "StatusHeader", result, "Status");
        webElementVerifier.isTextCorrectlyDisplayed(tableHeaders.get(8),
                "ActionHeader", result, "Action");
    }

    // verify first product only for sake of simplicity and demonestration
    private void verifyFirstProductInfo(StringBuilder result) throws SQLException, ClassNotFoundException {
        ProductImpl actualProduct = (ProductImpl) getFirstActualProduct();
        ProductImpl expectedProduct = (ProductImpl) productImpl.getProduct(actualProduct.getId());
        if (!(expectedProduct.getName().equals(actualProduct.getName())
                && expectedProduct.getQuantity() == actualProduct.getQuantity()
                && expectedProduct.getPrice() == actualProduct.getPrice()
                && expectedProduct.getDescription().equals(actualProduct.getDescription())
                && expectedProduct.getImage().equals(actualProduct.getImage())
                && expectedProduct.getId().equals(actualProduct.getId())
                && expectedProduct.getCategoryType() == actualProduct.getCategoryType()
                && expectedProduct.getStatus() == actualProduct.getStatus()))
            result.append("Incorrect product info is being displayed in products page\n");
    }

    public Product getFirstActualProduct() {
        ProductImpl actualProduct = new ProductImpl();
        actualProduct.setQuantity(Integer.parseInt(productStocks.get(0).getText()));
        actualProduct.setPrice(Double.parseDouble(productPrices.get(0).getText()
                .substring(productPrices.get(0).getText().indexOf("$") + 1)));

        actualProduct.setName(productNames.get(0).getText().trim());
        actualProduct.setImage(productImages.get(0).getAttribute("src"));
        actualProduct.setDescription(productDesc.get(0).getText().trim());
        actualProduct.setId(productCodes.get(0).getText());
        actualProduct.setCategoryType(0); // hardcoded for sake of simplicity
        actualProduct.setStatus(0);
        return actualProduct;
    }

    public void editProduct(String productId) {
        List<String> productCodesAsString = productCodes.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        int indexOfNeededProduct = productCodesAsString.indexOf(productId.trim());
        editProductLinks.get(indexOfNeededProduct).click();
    }

    // product index starts from 0
    public void clickEditProduct(int productIndex) {
        editProductLinks.get(productIndex).click();
    }

    public List<String> getFirstPageProducts() {
        return productCodes.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}
