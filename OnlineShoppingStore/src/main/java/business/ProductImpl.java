package business;

import common.PostgreSqlClient;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Data
public class ProductImpl implements Product {
    private String id;
    private int categoryType;
    private String description;
    private String image;
    private String name;
    private double price;
    private int status;
    private int quantity;
    private double total;

    @Override
    public List<Product> getProducts() throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String getAllProducts = "SELECT product_id, category_type, product_description, " +
                "product_icon, product_name, product_price, product_status, product_stock\n" +
                "FROM public.product_info;";
        ResultSet result = PostgreSqlClient.executeQuery(getAllProducts);
        while (result.next()) {
            ProductImpl product = new ProductImpl();
            product.categoryType = result.getInt("category_type");
            product.description = result.getString("product_description");
            product.id = result.getString("product_id");
            product.image = result.getString("product_icon");
            product.name = result.getString("product_name");
            product.price = result.getInt("product_price");
            product.quantity = result.getInt("product_stock");
            product.status = result.getInt("product_status");
            products.add(product);
        }
        return products;
    }

    @Override
    public Product getProduct(String productId) throws SQLException, ClassNotFoundException {
        String getProduct = "SELECT product_id, category_type, product_description, " +
                "product_icon, product_name, product_price, product_status, product_stock\n" +
                "FROM public.product_info" +
                " WHERE PRODUCT_ID='" + productId + "'";
        ResultSet result = PostgreSqlClient.executeQuery(getProduct);
        ProductImpl product = new ProductImpl();
        while (result.next()) {
            product.categoryType = result.getInt("category_type");
            product.description = result.getString("product_description");
            product.id = result.getString("product_id");
            product.image = result.getString("product_icon");
            product.name = result.getString("product_name");
            product.price = result.getInt("product_price");
            product.quantity = result.getInt("product_stock");
            product.status = result.getInt("product_status");
        }
        return product;
    }

    public int mapCategoryToInt(String category) {
        switch (category) {
            case "Books":
                return 0;
            case "Food":
                return 1;
            case "Clothes":
                return 2;
            default:
                return 3;
        }
    }

    public String mapIntToCat(int category) {
        switch (category) {
            case 0:
                return "Books";
            case 1:
                return "Food";
            case 2:
                return "Clothes";
            default:
                return "Drink";
        }
    }

    @Override
    public String mapIntToStatus(int status) {
        if(status == 0)
            return "Available";
        else
            return "Unavailable";
    }

    @Override
    public String getRandomProduct() throws SQLException, ClassNotFoundException {
        List<Product> products = getProducts();
        int availableProducts = products.size();
        int randomIndex = (int) (Math.random() * availableProducts);
        ProductImpl productImpl = (ProductImpl) products.get(randomIndex);
        return productImpl.getId();
    }

    @Override
    public Map<Integer, Integer> getValidRandomPageNumberSize() throws SQLException, ClassNotFoundException {
        Map<Integer, Integer> pageNumberToPageSize = new HashMap<>();
        int productsSize = getProducts().size();
        int randomPageSize = new Random().nextInt(4) + 1; //divide to max 3 pages
        int numberOfPages = productsSize % randomPageSize == 0 ?
                productsSize / randomPageSize : (productsSize / randomPageSize) +1;
        int randomPageNumber = new Random().nextInt(numberOfPages) + 1;
        pageNumberToPageSize.put(randomPageNumber, randomPageSize);
        return pageNumberToPageSize;
    }
}
