import business.UserImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static common.Constants.PASS;

public class SignUpPageTest extends AbstractTest {

    @BeforeMethod
    private void setUpTest() {
        driver.get("http://localhost:4200/product");
        navigationBar.initializeSpecialElements();
        navigationBar.clickSignUp();
    }

    @Test
    private void verifySignPageElements() {
        String result = signUp.verifyPageElements();
        Assert.assertEquals(result, PASS);
    }

    @Test
    private void verifyNewSignedUpUser() {
        UserImpl newUser = new UserImpl();
        newUser.setActive(true);
        newUser.setAddress("Amman - Jordan");
        newUser.setEmail("test@gmail.com");
        newUser.setName("test user");
        newUser.setPassword("123");
        newUser.setPhone("0546854988");
        newUser.setRole("ROLE_CUSTOMER");
        signUp.signUpNewUser(newUser);
        try {
            Thread.sleep(200);
            UserImpl registeredUser = (UserImpl)userImpl.getUserByEmail(newUser.getEmail());
            Assert.assertTrue(userImpl.areTwoUsersEquals(newUser, registeredUser));
            int affectedRows = userImpl.deleteUserById(registeredUser.getUserId());
            if(affectedRows != 2)
                Assert.fail("Failed to delete new created user");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Assert.fail("SQLException while getting or deleting user from db");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException while getting or deleting user from db");
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException while waiting new user");
        }
    }
}
