import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static common.Constants.PASS;

public class SignInPageTest extends AbstractTest {

    @BeforeMethod
    private void setUpTest() {
        driver.get("http://localhost:4200/product");
        navigationBar.initializeSpecialElements();
        navigationBar.clickSignIn();
    }

    @Test
    private void verifySignPageElements() {
        String result = signIn.verifyPageElements();
        Assert.assertEquals(result, PASS);
    }

    @Test
    private void loginWithInvalidUserName() {
        signIn.setUserName("dummy@gmail.com");
        signIn.setPassword("123"); // valid password for my user
        signIn.clickSignIn();
        try {
            synchronized (driver) {
                driver.wait(500);
            }
            Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:4200/login");
            Assert.assertTrue(signIn.verifyInvalidUserNameOrPassword());
        } catch (InterruptedException e) {
            Assert.fail("failed to wait");
        }
    }

    // This test assumes there is a valid user with email "maa190@hotmail.com" and pass 1234
    @Test
    private void loginWithInvalidPassword() {
        signIn.setUserName("maa190@hotmail.com"); // valid user name
        signIn.setPassword("1234"); // invalid password for my user
        signIn.clickSignIn();
        try {
            synchronized (driver) {
                driver.wait(500);
            }
            Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:4200/login");
            Assert.assertTrue(signIn.verifyInvalidUserNameOrPassword());
        } catch (InterruptedException e) {
            Assert.fail("failed to wait");
        }
    }

    @Test
    private void loginWithInvalidUserNameInvalidPassword() {
        signIn.setUserName("dummy@hotmail.com"); // valid user name
        signIn.setPassword("1234"); // invalid password for my user
        signIn.clickSignIn();
        try {
            synchronized (driver) {
                driver.wait(500);
            }
            Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:4200/login");
            Assert.assertTrue(signIn.verifyInvalidUserNameOrPassword());
        } catch (InterruptedException e) {
            Assert.fail("failed to wait");
        }
    }

    @Test
    private void loginWithValidCredentials() {
        signIn.setUserName("maa190@hotmail.com"); // valid user name
        signIn.setPassword("123"); // invalid password for my user
        signIn.clickSignIn();
        try {
            synchronized (driver) {
                driver.wait(500);
            }
            Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:4200/product");
            Assert.assertTrue(navigationBar.isLoggedIn());
        } catch (InterruptedException e) {
            Assert.fail("failed to wait");
        }
    }

    @Test
    private void loginAsManager() {
        signIn.signInAsManager();
        try {
            synchronized (driver) {
                driver.wait(500);
            }
            Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:4200/seller/product");
            Assert.assertTrue(navigationBar.isLoggedInAsManagerOrEmployee());
        } catch (InterruptedException e) {
            Assert.fail("failed to wait");
        }
    }

    @Test
    private void loginAsEmployee() {
        signIn.signInAsEmployee();
        try {
            synchronized (driver) {
                driver.wait(500);
            }
            Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:4200/seller/product");
            Assert.assertTrue(navigationBar.isLoggedInAsManagerOrEmployee());
        } catch (InterruptedException e) {
            Assert.fail("failed to wait");
        }
    }
}
