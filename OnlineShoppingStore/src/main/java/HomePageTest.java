import business.OrderImpl;
import business.ProductImpl;
import common.WebElementVerifierImpl;
import components.NavigationBar;
import components.PaginationControl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import pages.*;

public class HomePageTest{

    static WebDriver driver;

    static By homePageUserName = By.xpath("//body/app-root[1]/div[1]/app-card[1]/div[1]/div[1]/div[1]/div[1]/a[1]");


    public static void main(String[] args) {
       driver = new ChromeDriver();
       System.setProperty("webdriver.chrome.driver","~/Users/munthera/Desktop/chromedriver.exe");
        NavigationBar navigationBar = new NavigationBar(new WebElementVerifierImpl(), driver);
        PageFactory.initElements(driver, navigationBar);
        PaginationControl paginationControl = new PaginationControl(driver, new ProductImpl(), new OrderImpl());
        PageFactory.initElements(driver, paginationControl);
        HomePage hp= new HomePage(driver, new WebElementVerifierImpl(),new ProductImpl(),paginationControl, navigationBar);
        PageFactory.initElements(driver, hp);
        driver.get("http://localhost:4200/product?page=5&size=2");
        String result = hp.verifyPageElements(5, 2);
        ProductDetail dp = new ProductDetail(driver,new ProductImpl(), navigationBar,new WebElementVerifierImpl());
        PageFactory.initElements(driver, dp);
        String r2 =  dp.verifyPageElements();
        SignIn signIn = new SignIn(navigationBar, new WebElementVerifierImpl(), driver);
        PageFactory.initElements(driver, signIn);
        navigationBar.clickSignIn();
        String r3 = signIn.verifyPageElements();
        navigationBar.clickSignUp();
        SignUp signUp = new SignUp(navigationBar, new WebElementVerifierImpl());
        PageFactory.initElements(driver, signUp);
        String r4 = signUp.verifyPageElements();
        Cart cart = new Cart(new WebElementVerifierImpl(), new ProductImpl(), navigationBar, driver);
        PageFactory.initElements(driver, cart);
        navigationBar.clickCart();
        String r5 = cart.verifyPageElements();
        navigationBar.clickSignIn();
        signIn.signInAsManager();
        Products products = new Products(new WebElementVerifierImpl(), navigationBar, new ProductImpl(),paginationControl);
        PageFactory.initElements(driver, products);
        String r6 = products.verifyPageElements(1, 5);
        Orders orders = new Orders(new WebElementVerifierImpl(), navigationBar, new OrderImpl(), paginationControl,driver);
        PageFactory.initElements(driver, orders);
        navigationBar.clickOrders();
        String r7 = orders.verifyPageElements(1, 10); // for this page should always be 1 and 10
        System.out.println(orders.clickActionOfNthOrder(1, ActionName.SHOW)+"");
        EditProduct editProduct = new EditProduct(new WebElementVerifierImpl(), navigationBar, new ProductImpl());
        PageFactory.initElements(driver, editProduct);
        navigationBar.clickShoppingPortalText(); // get back to products page
        products.clickEditProduct(0);
        String r8 = editProduct.verifyPageElements();
        System.out.println(result);
        System.out.println(r2);
        System.out.println(r3);
        System.out.println(r4);
        System.out.println(r5);
        System.out.println(r6);
        System.out.println(r7);
        System.out.println(r8);
        //navigationBar.clickSignIn();
        //driver.close();


    }

    public HomePageTest(WebDriver driver){

        this.driver = driver;

    }

    //Get the User name from Home Page

    public static void getHomePageDashboardUserName(){

        driver.get("http://localhost:4200/product");
        driver.findElement(homePageUserName).click();

    }

}