import business.OrderContextImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.Map;

import static common.Constants.PASS;

public class E2EScenarioAvailableUser extends AbstractTest {

    @BeforeMethod
    private void setUpTest() {
        try {
            Map<Integer, Integer> pageToSize= productImpl.getValidRandomPageNumberSize();
            int pageNumber = pageToSize.keySet().iterator().next();
            int pageSize = pageToSize.values().iterator().next();
            driver.get("http://localhost:4200/product?page="+ pageNumber + "&size=" + pageSize);
            navigationBar.initializeSpecialElements();
            navigationBar.clickSignIn();
            signIn.singInAsCustomer();
        } catch (SQLException e) {
            Assert.fail("SQLException failed to get random page number page size");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to get random page number page size");
        }
    }

    @Test
    private void buyProductsUsingAvailableUser() {
        homePage.getRandomProduct();
        productDetail.addProductToCart();
        navigationBar.clickShoppingPortalText();
        homePage.getRandomProduct();
        productDetail.addProductToCart();
        OrderContextImpl orderContext = new OrderContextImpl(productImpl, orderImpl, userImpl);
        try {
            orderContext.buildOrderContext(cart.getActualProducts(), "customer1@email.com");
            cart.clickCheckout();
            Thread.sleep(2000);
            Assert.assertEquals(orderImpl.isOrderSuccessfullyPlaced(orderContext), PASS);
        } catch (SQLException e) {
            Assert.fail("SQLException failed to build order context");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to build order context");
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException failed to build order context");
        }
    }
}
