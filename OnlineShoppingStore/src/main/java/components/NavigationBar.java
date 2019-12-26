package components;

import common.WebElementVerifier;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class NavigationBar {

    private WebElementVerifier webElementVerifier;
    private WebDriver driver;

    public NavigationBar(WebElementVerifier webElementVerifier, WebDriver driver) {
        this.webElementVerifier = webElementVerifier;
        this.driver = driver;
    }

    @FindBy(how = How.XPATH, using = "//body/app-root[1]/div[1]/app-navigation[1]/nav[1]")
    private WebElement navigationBar;

    @FindBy(how = How.XPATH, using = "//body/app-root[1]/div[1]/app-navigation[1]/nav[1]/a[1]/img[1]")
    private WebElement shoppingPortalLogo;

    @FindBy(how = How.XPATH, using = "//body/app-root[1]/div[1]/app-navigation[1]/nav[1]/a[1]")
    private WebElement shoppingPortalText;

    @FindBy(how = How.XPATH, using = "//*[@id=\"navbarNav\"]/div[1]/a[1]")
    private WebElement booksCategory;

    @FindBy(how = How.XPATH, using = "//*[@id=\"navbarNav\"]/div[1]/a[2]")
    private WebElement foodCategory;

    @FindBy(how = How.XPATH, using = "//*[@id=\"navbarNav\"]/div[1]/a[3]")
    private WebElement clothesCategory;

    @FindBy(how = How.XPATH, using = "//*[@id=\"navbarNav\"]/div[1]/a[4]")
    private WebElement drinkCategory;

    private WebElement cartAction;
    private WebElement signInAction;
    private WebElement signUpAction;
    private WebElement ordersAction;

    @FindBy(how = How.XPATH, using = "//*/a")
    private List<WebElement> allNavBarActions;

    public String verifyNavigationBarElements() {

        StringBuilder result = new StringBuilder();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            result.append(e.getMessage()).append("\n");
        }
        initializeSpecialElements();
        webElementVerifier.isDisplayedAndEnabled(navigationBar , "navigationBar", result);

        webElementVerifier.isDisplayed(shoppingPortalLogo , "shoppingPortalLogo", result);

        webElementVerifier.isDisplayed(shoppingPortalText , "shoppingPortalText", result);
        webElementVerifier.isTextCorrectlyDisplayed(shoppingPortalText , "shoppingPortalText", result, "E-Shop");

        if(!isLoggedInAsManagerOrEmployee()) {
            webElementVerifier.isDisplayedAndEnabled(booksCategory , "booksCategory", result);
            webElementVerifier.isTextCorrectlyDisplayed(booksCategory , "booksCategory", result, "Books");

            webElementVerifier.isDisplayedAndEnabled(foodCategory , "foodCategory", result);
            webElementVerifier.isTextCorrectlyDisplayed(foodCategory , "foodCategory", result, "Food");

            webElementVerifier.isDisplayedAndEnabled(clothesCategory , "clothesCategory", result);
            webElementVerifier.isTextCorrectlyDisplayed(clothesCategory , "clothesCategory", result, "Clothes");

            webElementVerifier.isDisplayedAndEnabled(drinkCategory , "drinkCategory", result);
            webElementVerifier.isTextCorrectlyDisplayed(drinkCategory , "drinkCategory", result, "Drink");

            webElementVerifier.isDisplayedAndEnabled(cartAction, "cartAction", result);
            webElementVerifier.isTextCorrectlyDisplayed(cartAction, "cartAction", result, "Cart");
        }

        if(!isLoggedIn()) {
            webElementVerifier.isDisplayedAndEnabled(signInAction , "signInAction", result);
            webElementVerifier.isTextCorrectlyDisplayed(signInAction , "signInAction", result, "Sign In");

            webElementVerifier.isDisplayedAndEnabled(signUpAction , "signUpAction", result);
            webElementVerifier.isTextCorrectlyDisplayed(signUpAction , "signUpAction", result, "Sign Up");
        }
        return (!result.toString().trim().isEmpty()) ? result.toString() : "";
    }

    public void initializeSpecialElements() {
        if(!isLoggedIn()) {
            signInAction = driver.findElement(By.xpath("//*[@id='navbarNav']/div[2]/a[2]"));
            signUpAction = driver.findElement(By.xpath("//*[@id='navbarNav']/div[2]/a[3]"));
        }
        if(!isLoggedInAsManagerOrEmployee()) {
            cartAction = driver.findElement(By.xpath("//*[@id='navbarNav']/div[2]/a[1]"));
        }
        if(isLoggedIn()) {
            if(isLoggedInAsManagerOrEmployee()) {
                ordersAction = driver.findElement(By.xpath("//*[@id=\"navbarNav\"]/div/a[1]"));
            }
        }
    }

    public void clickCart() {
        cartAction.click();
    }
    public void clickSignIn() {
        signInAction.click();
    }
    public void clickSignUp() {
        signUpAction.click();
    }
    public void clickShoppingPortalText() {
        shoppingPortalText.click();
    }
    public boolean isLoggedIn() {
        return !driver.getPageSource().contains("Sign In");
    }
    public boolean isLoggedInAsManagerOrEmployee() {
        return driver.getPageSource().contains("manager1") || driver.getPageSource().contains("employee1");
    }

    public void clickOrders() {
        ordersAction.click();
    }
}
