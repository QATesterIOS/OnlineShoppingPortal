import business.*;
import common.WebDriverUser;
import common.WebElementVerifier;
import common.WebElementVerifierImpl;
import components.NavigationBar;
import components.PaginationControl;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import pages.*;

import java.util.concurrent.TimeUnit;

public class AbstractTest implements WebDriverUser {
    protected Product productImpl;
    protected Order orderImpl;
    protected WebElementVerifier webElementVerifier;
    protected User userImpl;
    protected WebDriver driver;
    protected NavigationBar navigationBar;
    protected PaginationControl paginationControl;
    protected HomePage homePage;
    protected ProductDetail productDetail;
    protected SignIn signIn;
    protected SignUp signUp;
    protected Cart cart;
    protected Products products;
    protected Orders orders;
    protected EditProduct editProduct;

    @BeforeSuite
    protected void initializeTestCommonComponents() {
        // ToDo: make this factory read from parameterized data source
        driver = new ChromeDriver();
        //ToDo: make it parameterized
        System.setProperty("webdriver.chrome.driver", "~/Users/munthera/Desktop/chromedriver.exe");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        productImpl = new ProductImpl();
        orderImpl = new OrderImpl();
        webElementVerifier = new WebElementVerifierImpl();
        userImpl = new UserImpl();

        navigationBar = new NavigationBar(webElementVerifier, driver);
        PageFactory.initElements(driver, navigationBar);

        paginationControl = new PaginationControl(driver, productImpl, orderImpl);
        PageFactory.initElements(driver, paginationControl);

        homePage = new HomePage(driver, webElementVerifier, productImpl, paginationControl, navigationBar);
        PageFactory.initElements(driver, homePage);

        productDetail = new ProductDetail(driver, productImpl, navigationBar, webElementVerifier);
        PageFactory.initElements(driver, productDetail);

        signIn = new SignIn(navigationBar, webElementVerifier, driver);
        PageFactory.initElements(driver, signIn);

        signUp = new SignUp(navigationBar, webElementVerifier);
        PageFactory.initElements(driver, signUp);

        cart = new Cart(webElementVerifier, productImpl, navigationBar, driver);
        PageFactory.initElements(driver, cart);

        products = new Products(webElementVerifier, navigationBar, productImpl, paginationControl);
        PageFactory.initElements(driver, products);

        orders = new Orders(webElementVerifier, navigationBar, orderImpl, paginationControl, driver);
        PageFactory.initElements(driver, orders);

        editProduct = new EditProduct(webElementVerifier, navigationBar, productImpl);
        PageFactory.initElements(driver, editProduct);
    }

    @AfterSuite
    protected void releaseTestResources() {
        close(driver);
    }

    @Override
    public void close(WebDriver driver) {
       driver.close();
    }
}
