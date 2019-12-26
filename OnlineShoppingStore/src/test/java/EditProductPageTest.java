import business.ProductImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import static common.Constants.PASS;

public class EditProductPageTest extends AbstractTest {

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
        List<String> firstPageProductsId = products.getFirstPageProducts();
        int randomProdIdIndex = new Random().nextInt(firstPageProductsId.size());
        products.editProduct(firstPageProductsId.get(randomProdIdIndex));
        Assert.assertEquals(editProduct.verifyPageElements(), PASS);
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
        List<String> firstPageProductsId = products.getFirstPageProducts();
        int randomProdIdIndex = new Random().nextInt(firstPageProductsId.size());
        products.editProduct(firstPageProductsId.get(randomProdIdIndex));
        Assert.assertEquals(editProduct.verifyPageElements(), PASS);
    }

    @Test
    private void editProductInfoWhenLoggedInAsManager() {
        ProductImpl prodBeforeUpdated = new ProductImpl();
        ProductImpl prodUpdated = new ProductImpl();
        signIn.signInAsManager();
        synchronized (driver) {
            try {
                driver.wait(500);
            } catch (InterruptedException e) {
                Assert.fail("failed to wait");
            }
        }
        List<String> firstPageProductsId = products.getFirstPageProducts();
        int randomProdIdIndex = new Random().nextInt(firstPageProductsId.size());
        String productIdToBeEdited = firstPageProductsId.get(randomProdIdIndex);
        try {
            prodBeforeUpdated = (ProductImpl) productImpl.getProduct(productIdToBeEdited);
        } catch (SQLException e) {
            Assert.fail("SQLException failed to get product info before update from db");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to get product info before update from db");
        }
        products.editProduct(productIdToBeEdited);
        ProductImpl updatedProduct = new ProductImpl();
        updatedProduct.setQuantity(1000);
        updatedProduct.setPrice(102.02);
        updatedProduct.setDescription("Desc updated by EditProductPageTest - Manager");
        editProduct.editProductInfo(updatedProduct);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException failed to wait");
        }
        try {
            prodUpdated = (ProductImpl) productImpl.getProduct(productIdToBeEdited);
        } catch (SQLException e) {
            Assert.fail("SQLException failed to get product info after update from db");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to get product info after update from db");
        }
        Assert.assertTrue(isProductCorrectlyUpdated(prodBeforeUpdated, prodUpdated));
    }

    private boolean isProductCorrectlyUpdated(ProductImpl prodBeforeUpdated, ProductImpl prodUpdated) {
        return prodBeforeUpdated.getStatus() == prodUpdated.getStatus()
                && prodBeforeUpdated.getImage().equals(prodUpdated.getImage())
                && prodBeforeUpdated.getId().equals(prodUpdated.getId())
                && prodBeforeUpdated.getCategoryType() == prodUpdated.getCategoryType()
                && prodUpdated.getDescription().contains("Desc updated by EditProductPageTest - ")
                && prodUpdated.getQuantity() == 1000
                && prodUpdated.getPrice() == 102;
    }

    @Test
    private void editProductInfoWhenLoggedInAsEmployee() {
        ProductImpl prodBeforeUpdated = new ProductImpl();
        ProductImpl prodUpdated = new ProductImpl();
        signIn.signInAsEmployee();
        synchronized (driver) {
            try {
                driver.wait(500);
            } catch (InterruptedException e) {
                Assert.fail("failed to wait");
            }
        }
        List<String> firstPageProductsId = products.getFirstPageProducts();
        int randomProdIdIndex = new Random().nextInt(firstPageProductsId.size());
        String productIdToBeEdited = firstPageProductsId.get(randomProdIdIndex);
        try {
            prodBeforeUpdated = (ProductImpl) productImpl.getProduct(productIdToBeEdited);
        } catch (SQLException e) {
            Assert.fail("SQLException failed to get product info before update from db");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to get product info before update from db");
        }
        products.editProduct(productIdToBeEdited);
        ProductImpl updatedProduct = new ProductImpl();
        updatedProduct.setQuantity(1000);
        updatedProduct.setPrice(102.02);
        updatedProduct.setDescription("Desc updated by EditProductPageTest - Manager");
        editProduct.editProductInfo(updatedProduct);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException failed to wait");
        }
        try {
            prodUpdated = (ProductImpl) productImpl.getProduct(productIdToBeEdited);
        } catch (SQLException e) {
            Assert.fail("SQLException failed to get product info after update from db");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException failed to get product info after update from db");
        }
        Assert.assertTrue(isProductCorrectlyUpdated(prodBeforeUpdated, prodUpdated));
    }
}
