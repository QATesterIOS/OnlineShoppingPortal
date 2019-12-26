package pages;

import business.Product;
import business.ProductImpl;
import common.WebElementVerifier;
import components.NavigationBar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static common.Constants.PASS;

public class ProductDetail implements NonPaginatedShoppingPortalPage {
    private String productId;
    private WebDriver driver;
    private WebElementVerifier webElementVerifier;
    private NavigationBar navigationBar;
    private Product productImpl;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-detail/h1")
    private WebElement title;

    @FindBy(how = How.CLASS_NAME, using = "col-lg-4")
    private WebElement productCard;

    @FindBy(how = How.CLASS_NAME, using = "card-img-top")
    private WebElement productImage;

    @FindBy(how = How.CLASS_NAME, using = "card-title")
    private WebElement productTile;

    @FindBy(how = How.CLASS_NAME, using = "card-test")
    private WebElement productDesc;

    @FindBy(how = How.CLASS_NAME, using = "card-text")
    private List<WebElement> productPriceStock;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-detail/div/div/div/div/form/button")
    private WebElement addToCart;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-detail/div/div/div/div/form/div/label/input")
    private WebElement quantityInput;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-detail/div/div/div/div/form/div/p[1]/strong")
    private WebElement descriptionLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-detail/div/div/div/div/form/div/p[2]/strong")
    private WebElement priceLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-detail/div/div/div/div/form/div/p[3]/strong")
    private WebElement stockLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-detail/div/div/div/div/form/div/label/strong")
    private WebElement quantityLabel;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-detail/div/div/div/div/form/div/p[4]/strong")
    private WebElement subtotalLabel;

    @FindBy(how = How.XPATH, using = "//*[@id=\"subtotal\"]")
    private WebElement subtotalAmount;

    public ProductDetail(WebDriver driver, Product productImpl,
                         NavigationBar navigationBar, WebElementVerifier webElementVerifier) {
        this.driver = driver;
        this.webElementVerifier = webElementVerifier;
        this.productImpl = productImpl;
        this.navigationBar = navigationBar;
    }

    @Override
    public String verifyPageElements() {
        StringBuilder result = new StringBuilder();
        try {
            this.productId = productImpl.getRandomProduct();
            driver.get("http://localhost:4200/product/" +this.productId);
            webElementVerifier.isTextCorrectlyDisplayed(title, "product detail title",
                    result, "Product Detail");
            verifyPageLabels(result);
            verifyProductInfo(result);
            verifySubTotalCalculation(result);
        } catch (SQLException e) {
            result.append("Failed to get expected products from db\n");
        } catch (ClassNotFoundException e) {
            result.append("Class not found while getting expected products from db\n");
        }
        result.append(navigationBar.verifyNavigationBarElements()).append("\n");
        return (!result.toString().trim().isEmpty()) ? result.toString() : PASS;
    }

    private void verifyPageLabels(StringBuilder result) {
        webElementVerifier.isTextCorrectlyDisplayed(descriptionLabel, "descriptionLabel",
                result, "Description:");
        webElementVerifier.isTextCorrectlyDisplayed(priceLabel, "priceLabel",
                result, "Price:");
        webElementVerifier.isTextCorrectlyDisplayed(stockLabel, "stockLabel",
                result, "Stock:");
        webElementVerifier.isTextCorrectlyDisplayed(quantityLabel, "quantityLabel",
                result, "Quantity:");
        webElementVerifier.isTextCorrectlyDisplayed(subtotalLabel, "subtotalLabel",
                result, "Subtotal:");
    }

    private Product getActualProduct() {
        ProductImpl p = new ProductImpl();
        String pDescAndLabel = productDesc.getText();
        String pDescription = pDescAndLabel.substring(
                pDescAndLabel.indexOf(":") + 1).trim();
        p.setImage(productImage.getAttribute("src"));
        p.setName(productTile.getText());
        p.setDescription(pDescription);
        p.setPrice(Double.parseDouble(getPrice()));
        p.setQuantity(Integer.parseInt(getStock()));
        return p;
    }

    public String getPrice() {
        return productPriceStock.stream().
                filter(element -> element.getText().contains("Price: "))
                .map(element -> element.getText()
                        .substring(element.getText().indexOf("$") + 1))
                .collect(Collectors.toList()).get(0);
    }

    private String getStock() {
        return productPriceStock.stream().
                filter(element -> element.getText().contains("Stock: "))
                .map(element -> element.getText().substring(element.getText().indexOf(":") + 2))
                .collect(Collectors.toList()).get(0);
    }

    private void verifyProductInfo(StringBuilder result) throws SQLException, ClassNotFoundException {
        ProductImpl actualProduct = (ProductImpl) getActualProduct();
        ProductImpl expectedProduct = (ProductImpl) productImpl.getProduct(productId);
        if (!verifyTwoProductsEqual(expectedProduct, actualProduct))
            result.append("Incorrect product info is being displayed in product detail page of product :")
                    .append(productId).append("\n");
    }

    private boolean verifyTwoProductsEqual(ProductImpl expected, ProductImpl actual) {
        return expected.getName().equals(actual.getName()) &&
                expected.getImage().equals(actual.getImage()) &&
                expected.getDescription().equals(actual.getDescription()) &&
                expected.getPrice() == actual.getPrice() &&
                expected.getQuantity() == actual.getQuantity();
    }

    private void verifySubTotalCalculation(StringBuilder result) throws SQLException, ClassNotFoundException {
        ProductImpl expectedProduct = (ProductImpl) productImpl.getProduct(productId);
        double price = expectedProduct.getPrice();
        int testQuantity = 3;
        double expectedPrice = testQuantity * price;
        quantityInput.clear();
        quantityInput.sendKeys(testQuantity + "");
        double newPrice = Double.parseDouble(
                this.driver.findElement(By.xpath("//*[@id=\"subtotal\"]")).getText().substring(1));
        if (expectedPrice != newPrice)
            result.append("Incorrect price is being calculated!\n");
    }

    public void addProductToCart() {
        addToCart.click();
    }

    public void setProductQuantity(int neededQuantity) {
        quantityInput.clear();
        quantityInput.sendKeys(neededQuantity+"");
    }

    public String getSubtotalAmount() {
        return subtotalAmount.getText().substring(subtotalAmount.getText().indexOf("$") + 1);
    }
}
