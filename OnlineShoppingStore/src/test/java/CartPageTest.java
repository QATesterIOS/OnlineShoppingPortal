import business.Product;
import business.ProductImpl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;

import static common.Constants.PASS;

public class CartPageTest extends AbstractTest {

    @BeforeMethod
    private void setUpTest() {
        driver.get("http://localhost:4200/product");
        navigationBar.initializeSpecialElements();
        navigationBar.clickCart();
    }

    @Test
    private void verifyCartPageWhenEmpty() {
        String result = cart.verifyPageElements();
        Assert.assertEquals(result, PASS);
    }

    // This test verifies product info and price and subtotal and total and remove button
    // verifies totals after removing item from cart
    // navigates to product page and verifies the url
    @Test
    private void verifyCartPagePageWhenProductsAdded() {
        navigationBar.clickSignIn();
        signIn.singInAsCustomer();
        try {
            homePage.getRandomProduct();
            productDetail.addProductToCart();
            navigationBar.clickShoppingPortalText();
            homePage.getRandomProduct();
            productDetail.addProductToCart();
            List<Product> expectedProducts = productImpl.getProducts();
            List<Product> actualProducts = cart.getActualProducts();
            int numberOfMatchedProducts = 0;
            for (Product expectedProduct : expectedProducts) {
                for (Product actualProduct : actualProducts) {
                    ProductImpl expected = (ProductImpl) expectedProduct;
                    ProductImpl actual = (ProductImpl) actualProduct;
                    if (actual.getId().equals(expected.getId())
                            && verifyTwoProductsEqual(expected, actual))
                        numberOfMatchedProducts++;
                }
            }
            Assert.assertEquals(numberOfMatchedProducts, actualProducts.size());
            Assert.assertTrue(isCorrectProductSubTotal(actualProducts, expectedProducts));
            Assert.assertEquals(getActualProductsTotal(actualProducts), cart.getProductsTotal());
            cart.removeSpecificProduct(0);
            actualProducts = cart.getActualProducts();
            if (actualProducts.size() > 0)
                Assert.assertEquals(getActualProductsTotal(actualProducts), cart.getProductsTotal());
            if (actualProducts.size() > 0)
                cart.goToNthProductPage(0);
            ProductImpl product = (ProductImpl)actualProducts.get(0);
            Assert.assertEquals("http://localhost:4200/product/" + product.getId(), driver.getCurrentUrl());
        } catch (SQLException e) {
            Assert.fail("SQLException while getting products from db");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException while getting products from db");
        }
    }

    private boolean verifyTwoProductsEqual(ProductImpl expected, ProductImpl actual) {
        return expected.getImage().equals(actual.getImage()) &&
                expected.getPrice() == actual.getPrice() &&
                expected.getImage().equals(actual.getImage()) &&
                expected.getName().equals(actual.getName()) &&
                (expected.getStatus()) == actual.getStatus();
    }

    private boolean isCorrectProductSubTotal(List<Product> actualProducts, List<Product> expectedProducts) {
        int numberOfMatchedSubtotals = 0;
        for (Product expectedProduct : expectedProducts) {
            for (Product actualProduct : actualProducts) {
                ProductImpl expected = (ProductImpl) expectedProduct;
                ProductImpl actual = (ProductImpl) actualProduct;
                if (actual.getId().equals(expected.getId())
                        && actual.getTotal() == (expected.getPrice() * actual.getQuantity()))
                    numberOfMatchedSubtotals++;
            }
        }
        return numberOfMatchedSubtotals == actualProducts.size();
    }

    private double getActualProductsTotal(List<Product> actualProducts) {
        double total = 0.0;
        for (Product actualProduct : actualProducts) {
            ProductImpl actual = (ProductImpl) actualProduct;
            total += actual.getTotal();
        }
        return total;
    }
}
