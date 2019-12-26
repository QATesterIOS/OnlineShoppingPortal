package business;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Product {
     List<Product> getProducts() throws SQLException, ClassNotFoundException;
     Product getProduct(String productId) throws SQLException, ClassNotFoundException;
     int mapCategoryToInt(String category);
     String mapIntToCat(int category);
     String mapIntToStatus(int status);
     String getRandomProduct() throws SQLException, ClassNotFoundException;
     Map<Integer,Integer> getValidRandomPageNumberSize() throws SQLException, ClassNotFoundException;
}
