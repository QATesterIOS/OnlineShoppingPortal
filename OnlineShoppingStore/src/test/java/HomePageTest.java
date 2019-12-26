import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.Map;

import static common.Constants.*;

public class HomePageTest extends AbstractTest{

    @Test
    private void verifyHomePageElementsWithRandomPageSize() {
        try {
            Map<Integer, Integer> pageNumberToSize =  productImpl.getValidRandomPageNumberSize();
            int randomPageNumber = pageNumberToSize.keySet().iterator().next();
            int randomPageSize = pageNumberToSize.values().iterator().next();
            driver.get("http://localhost:4200/product?page="+randomPageNumber+"&size="+randomPageSize);
            String result = homePage.verifyPageElements(randomPageNumber, randomPageSize);
            Assert.assertEquals(result, PASS);
        } catch (SQLException e) {
            Assert.fail("SQLException while getting random page number and page size");
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException while getting random page number and page size");
        }
    }

    @Test
    private void verifyHomePageElementsForDefaultLaunchPage() {
            driver.get("http://localhost:4200/product");
            String result = homePage.verifyPageElements(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
            Assert.assertEquals(result, PASS);
    }
}
