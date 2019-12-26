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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static common.Constants.PASS;

public class Cart implements NonPaginatedShoppingPortalPage {
    private WebElementVerifier webElementVerifier;
    private NavigationBar navigationBar;
    private Product productImpl;
    private WebDriver driver;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/h1")
    private WebElement title;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/table/thead/tr")
    private WebElement tableHeaders;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/h4")
    private WebElement emptyCartMessage;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/table/tbody/tr/td[5]/a")
    private List<WebElement> removeFromCartButton;

    @FindBy(how = How.XPATH, using = "//*/button")
    private List<WebElement> pageButtons;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/table/tbody/tr[*]")
    private List<WebElement> actualProductsAsWebElement;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/table/tbody/tr[*]/th/a/img")
    private List<WebElement> productImages;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/table/tbody/tr[*]/td[1]/a")
    private List<WebElement> productTitles;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/table/tbody/tr[*]/td[2]")
    private List<WebElement> productPrices;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/table/tbody/tr[*]/td[3]/input")
    private List<WebElement> productQuantities;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/table/tbody/tr[*]/td[4]")
    private List<WebElement> productSubtotals;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/table/tbody/tr[*]/th/a")
    private List<WebElement> productIds;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/table/tbody")
    private WebElement tableBody;

    @FindBy(how = How.XPATH, using = "//body/app-root/div/app-cart/div/h5")
    private WebElement productsTotal;

    public Cart(WebElementVerifier webElementVerifier, Product productImpl,
                NavigationBar navigationBar, WebDriver driver) {
        this.productImpl = productImpl;
        this.navigationBar = navigationBar;
        this.webElementVerifier = webElementVerifier;
        this.driver = driver;
    }

    @Override
    public String verifyPageElements() {
        StringBuilder result = new StringBuilder();
        result.append(navigationBar.verifyNavigationBarElements());
        verifyCartPageTable(result);
        verifyEmptyCart(result);
        return (!result.toString().trim().isEmpty()) ? result.toString() : PASS;
    }

    private void verifyCartPageTable(StringBuilder result) {
        webElementVerifier.isTextCorrectlyDisplayed(title, "cart title", result, "My Cart");
        verifyTableHeaders(result);
    }

    private void verifyTableHeaders(StringBuilder result) {
        List<WebElement> headers = tableHeaders.findElements(By.tagName("th"));
        if (headers.size() != 6)
            result.append("Incorrect columns count appear in cart page");
        webElementVerifier.isTextCorrectlyDisplayed(headers.get(0), "PhotoHeader", result, "Photo");
        webElementVerifier.isTextCorrectlyDisplayed(headers.get(1), "NameHeader", result, "Name");
        webElementVerifier.isTextCorrectlyDisplayed(headers.get(2), "PriceHeader", result, "Price");
        webElementVerifier.isTextCorrectlyDisplayed(headers.get(3), "QuantityHeader", result, "Quantity");
        webElementVerifier.isTextCorrectlyDisplayed(headers.get(4), "SubtotalHeader", result, "Subtotal");
        webElementVerifier.isTextCorrectlyDisplayed(headers.get(5), "ActionHeader", result, "Action");
    }

    private void verifyEmptyCart(StringBuilder result) {
        if (tableBody.findElements(By.tagName("tr")).size() <= 0) {
            webElementVerifier.isTextCorrectlyDisplayed(emptyCartMessage, "webElementVerifier",
                    result, "Cart is empty. Go to get something! :)");
            if(pageButtons.size() > 1){
                result.append("Checkout button is being displayed while it should be hidden\n");
            }
            if (removeFromCartButton.size() != 0) {
                result.append("Remove from cart button is being displayed while it should be hidden");
            }
        } else {
            webElementVerifier.isElementHidden(emptyCartMessage, "emptyCartMessage", result);
            if(pageButtons.size() <= 1){
                result.append("Checkout button is is hidden while it should be displayed \n");
            }
        }
    }

    public void removeRandomProduct() {
        int randomIndex = new Random().nextInt(removeFromCartButton.size());
        removeFromCartButton.get(randomIndex).click();
    }

    public void removeSpecificProduct(int productIndex) {
        removeFromCartButton.get(productIndex).click();
    }

    public boolean clickCheckout() {
        if(pageButtons.size() <=1)
            return false;
        else
            driver.findElement(By.xpath("//body/app-root/div/app-cart/div/button")).click();
        return true;
    }

    public List<Product> getActualProducts() {
        List<Product> actualProducts = new ArrayList<>();
        int currentProductsCount = actualProductsAsWebElement.size();
        for (int i = 0; i < currentProductsCount; i++) {
            ProductImpl productImpl = new ProductImpl();

            productImpl.setImage(productImages.get(i).getAttribute("src"));

            productImpl.setId(productIds.get(i).getAttribute("href")
                    .substring(productIds.get(i).getAttribute("href").lastIndexOf("/") + 1));

            productImpl.setName(productTitles.get(i).getText().trim());

            productImpl.setPrice(Double.parseDouble(productPrices.get(i).getText()
                    .substring(productPrices.get(i).getText().indexOf("$") + 1)));

            productImpl.setQuantity(Integer.parseInt(productQuantities.get(i).getAttribute("ng-reflect-model")));

            productImpl.setTotal(Double.parseDouble(productSubtotals.get(i).getText()
                    .substring(productSubtotals.get(i).getText().indexOf("$") + 1)));

            actualProducts.add(productImpl);
        }
        return actualProducts;
    }

    // productIndex starts from 0 to n-1 of displayed products
    public boolean setQuantityOfNthProduct(int productIndex) {
        if (productIndex < 0 || productIndex > actualProductsAsWebElement.size() - 1)
            return false;
        else {
            productQuantities.get(productIndex).clear();
            productQuantities.get(productIndex).sendKeys(productIndex + "");
            return true;
        }
    }

    public double getProductsTotal() {
        return Double.parseDouble(productsTotal.getText()
                .substring(productsTotal.getText().indexOf("$") + 1));
    }

    // productIndex starts from 0 to n-1 of displayed products
    public boolean goToNthProductPage(int productIndex) {
        if (productIndex < 0 || productIndex > actualProductsAsWebElement.size() - 1)
            return false;
        else {
            productTitles.get(productIndex).click();
            return true;
        }
    }
}
