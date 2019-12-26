import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static common.Constants.PASS;

public class ProductDetailPageTest extends  AbstractTest {
    @BeforeMethod
    private void setUpTest() {
        driver.get("http://localhost:4200/product");
        navigationBar.initializeSpecialElements();
        navigationBar.clickSignIn();
        signIn.singInAsCustomer();
        homePage.getRandomProduct();
    }

    @Test
    private void verifyPageContentWhenLoggedInAsCustomer() {
        String result = productDetail.verifyPageElements();
        Assert.assertEquals(result, PASS);
    }

    @Test
    private void verifySubtotalOnQuantityChange() {
        double price = Double.parseDouble(productDetail.getPrice());
        int newQuantity = new Random().nextInt(7) + 1;
        productDetail.setProductQuantity(newQuantity);
        double expectedSubtotal = price * Double.parseDouble(newQuantity+"");
        Assert.assertEquals(Double.parseDouble(productDetail.getSubtotalAmount()), expectedSubtotal);
    }
}
