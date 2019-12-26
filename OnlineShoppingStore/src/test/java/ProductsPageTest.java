import business.ProductImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static common.Constants.PASS;

public class ProductsPageTest extends AbstractTest {

    @BeforeMethod
    private void setUpTest() {
        driver.get("http://localhost:4200/product");
        navigationBar.initializeSpecialElements();
        navigationBar.clickSignIn();
    }

    @Test
    private void verifyPageContentWhenLoggedInAsManager() {
        signIn.signInAsManager();
        synchronized (driver) {
            try {
                driver.wait(500);
            } catch (InterruptedException e) {
                Assert.fail("failed to wait");
            }
        }
        navigationBar.initializeSpecialElements();
        String result = products.verifyPageElements(1, 5);
        Assert.assertEquals(result, PASS);
    }

    @Test
    private void verifyPageContentWhenLoggedInAsEmployee() {
        signIn.signInAsEmployee();
        synchronized (driver) {
            try {
                driver.wait(500);
            } catch (InterruptedException e) {
                Assert.fail("failed to wait");
            }
        }
        navigationBar.initializeSpecialElements();
        String result = products.verifyPageElements(1, 5);
        Assert.assertEquals(result, PASS);
    }

    @Test
    private void editFirstProductAsManager() {
        signIn.signInAsManager();
        synchronized (driver) {
            try {
                driver.wait(500);
            } catch (InterruptedException e) {
                Assert.fail("failed to wait");
            }
        }
        navigationBar.initializeSpecialElements();
        ProductImpl product = (ProductImpl) products.getFirstActualProduct();
        String productIdToBeEdited = product.getId();
        products.editProduct(productIdToBeEdited);
        Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:4200/seller/product/" + productIdToBeEdited + "/edit");
    }

    @Test
    private void editFirstProductAsEmployee() {
        signIn.signInAsEmployee();
        synchronized (driver) {
            try {
                driver.wait(500);
            } catch (InterruptedException e) {
                Assert.fail("failed to wait");
            }
        }
        navigationBar.initializeSpecialElements();
        ProductImpl product = (ProductImpl) products.getFirstActualProduct();
        String productIdToBeEdited = product.getId();
        products.editProduct(productIdToBeEdited);
        Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:4200/seller/product/" + productIdToBeEdited + "/edit");
    }
}
