import business.OrderContextImpl;
import business.UserImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.Map;

import static common.Constants.PASS;

public class E2EScenarioSignNewUser extends AbstractTest {
    String newUserName;
    String password;
    @BeforeMethod
    private void setUpTest() {
        try {
            createNewUser();
            Map<Integer, Integer> pageToSize= productImpl.getValidRandomPageNumberSize();
            int pageNumber = pageToSize.keySet().iterator().next();
            int pageSize = pageToSize.values().iterator().next();
            driver.get("http://localhost:4200/product?page="+ pageNumber + "&size=" + pageSize);
            navigationBar.initializeSpecialElements();
            navigationBar.clickSignIn();
            signIn.setUserName(newUserName);
            signIn.setPassword(password);
            signIn.clickSignIn();
        } catch (SQLException e) {
            Assert.fail("SQLException failed to get random page number page size");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to get random page number page size");
        }
    }

    private void createNewUser() {
        driver.get("http://localhost:4200/product");
        navigationBar.initializeSpecialElements();
        navigationBar.clickSignUp();
        UserImpl newUser = new UserImpl();
        newUser.setActive(true);
        newUser.setAddress("Amman - Jordan");
        newUserName = "test"+ System.currentTimeMillis() +"@gmail.com";
        newUser.setEmail(newUserName);
        newUser.setName("test user");
        password = "123";
        newUser.setPassword(password);
        newUser.setPhone("0546854988");
        newUser.setRole("ROLE_CUSTOMER");
        signUp.signUpNewUser(newUser);
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
            orderContext.buildOrderContext(cart.getActualProducts(), newUserName);
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
