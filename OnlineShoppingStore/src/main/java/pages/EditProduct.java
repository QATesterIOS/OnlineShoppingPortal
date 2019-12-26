package pages;

import business.Product;
import business.ProductImpl;
import common.WebElementVerifier;
import components.NavigationBar;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import java.sql.SQLException;

import static common.Constants.PASS;

public class EditProduct implements NonPaginatedShoppingPortalPage {
    private WebElementVerifier webElementVerifier;
    private NavigationBar navigationBar;
    private Product productImpl;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product-edit/h1")
    private WebElement title;

    @FindBy(how = How.XPATH, using = "//*[@id=\"productId\"]")
    private WebElement idBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"productIcon\"]")
    private WebElement photoBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"productName\"]")
    private WebElement nameBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"categoryType\"]")
    private WebElement categoryBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"productDescription\"]")
    private WebElement descriptionBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"productPrice\"]")
    private WebElement priceBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"productStatus\"]")
    private WebElement statusBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"productStock\"]")
    private WebElement stockBox;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product-edit/div/form/div[9]/button")
    private WebElement submit;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product-edit/div/form/div[1]/label")
    private WebElement code;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product-edit/div/form/div[2]/label")
    private WebElement photo;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product-edit/div/form/div[3]/label")
    private WebElement name;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product-edit/div/form/div[4]/label")
    private WebElement category;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product-edit/div/form/div[5]/label")
    private WebElement desc;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product-edit/div/form/div[6]/label")
    private WebElement price;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product-edit/div/form/div[7]/label")
    private WebElement stock;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-product-edit/div/form/div[8]/label")
    private WebElement status;

    public EditProduct(WebElementVerifier webElementVerifier, NavigationBar navigationBar,
                       Product product) {
        this.navigationBar = navigationBar;
        this.productImpl = product;
        this.webElementVerifier = webElementVerifier;
    }

    @Override
    public String verifyPageElements() {
        StringBuilder result = new StringBuilder();
        result.append(navigationBar.verifyNavigationBarElements());
        verifyPageLabels(result);
        try {
            verifyProductInfo(result);
        } catch (SQLException e) {
            result.append("SQLException, failed to get product info from db\n").append(e.getMessage()).append("\n");
        } catch (ClassNotFoundException e) {
            result.append("ClassNotFoundException, failed to get product info from db\n").append(e.getMessage()).append("\n");
        }
        return (!result.toString().trim().isEmpty()) ? result.toString() : PASS;
    }

    private void verifyProductInfo(StringBuilder result) throws SQLException, ClassNotFoundException {
        ProductImpl actualProduct = (ProductImpl) getActualProduct();
        ProductImpl expectedProduct = (ProductImpl) productImpl.getProduct(actualProduct.getId());
        if (!(expectedProduct.getName().equals(actualProduct.getName())
                && expectedProduct.getQuantity() == actualProduct.getQuantity()
                && expectedProduct.getPrice() == actualProduct.getPrice()
                && expectedProduct.getDescription().equals(actualProduct.getDescription())
                && expectedProduct.getImage().contains(actualProduct.getImage())
                && expectedProduct.getId().equals(actualProduct.getId())
                && expectedProduct.getCategoryType() == actualProduct.getCategoryType()
                && expectedProduct.getStatus() == actualProduct.getStatus()))
            result.append("Incorrect product info is being displayed in edit product page\n");
    }

    private Product getActualProduct() {
        ProductImpl actualProduct = new ProductImpl();
        actualProduct.setStatus(new Select(statusBox).getFirstSelectedOption().getText().equals("Available") ? 0 : 1);
        actualProduct.setCategoryType(productImpl.mapCategoryToInt(
                new Select(categoryBox).getFirstSelectedOption().getText()));
        actualProduct.setId(idBox.getAttribute("ng-reflect-model"));
        actualProduct.setDescription(descriptionBox.getAttribute("ng-reflect-model"));
        actualProduct.setImage(photoBox.getAttribute("ng-reflect-model"));
        actualProduct.setName(nameBox.getAttribute("ng-reflect-model"));
        actualProduct.setPrice(Double.parseDouble(priceBox.getAttribute("ng-reflect-model")
                .substring(priceBox.getAttribute("ng-reflect-model").indexOf("$") + 1)));
        actualProduct.setQuantity(Integer.parseInt(stockBox.getAttribute("ng-reflect-model")));
        return actualProduct;
    }

    private void verifyPageLabels(StringBuilder result) {
        webElementVerifier.isTextCorrectlyDisplayed(title, "title", result, "Edit Product");
        webElementVerifier.isTextCorrectlyDisplayed(code, "codeLabel", result, "Code");
        webElementVerifier.isTextCorrectlyDisplayed(photo, "photoLabel", result, "Photo Link");
        webElementVerifier.isTextCorrectlyDisplayed(name, "nameLabel", result, "Name");
        webElementVerifier.isTextCorrectlyDisplayed(category, "catLabel", result, "Category");
        webElementVerifier.isTextCorrectlyDisplayed(desc, "descLabel", result, "Description");
        webElementVerifier.isTextCorrectlyDisplayed(price, "priceLabel", result, "Price");
        webElementVerifier.isTextCorrectlyDisplayed(stock, "stockLabel", result, "Stock");
        webElementVerifier.isTextCorrectlyDisplayed(status, "statusLabel", result, "Status");
        webElementVerifier.isDisabled(idBox, "idBox", result);
    }

    public void editProductInfo(Product newProduct) {
        ProductImpl newProductImpl = (ProductImpl) newProduct;
        if (newProductImpl.getImage() != null && !newProductImpl.getImage().trim().isEmpty()) {
            photoBox.clear();
            photoBox.sendKeys(newProductImpl.getImage());
        }

        if (newProductImpl.getName() != null && !newProductImpl.getName().trim().isEmpty()) {
            nameBox.clear();
            nameBox.sendKeys(newProductImpl.getName());
        }
        if (newProductImpl.getDescription() != null && !newProductImpl.getDescription().trim().isEmpty()) {
            descriptionBox.clear();
            descriptionBox.sendKeys(newProductImpl.getDescription());
        }
        priceBox.clear();
        priceBox.sendKeys(newProductImpl.getPrice() + "");

        stockBox.clear();
        stockBox.sendKeys(newProductImpl.getQuantity() + "");
        submit.click();
    }
}
