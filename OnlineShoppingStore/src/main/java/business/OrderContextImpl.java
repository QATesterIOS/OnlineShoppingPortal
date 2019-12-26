package business;

import lombok.Data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class OrderContextImpl implements OrderContext {
    private Product product;
    private Order order;
    private User user;
    private List<ProductImpl> cartProductsSnapshot;
    private int currentOrdersCount;
    private UserImpl buyer;
    private double orderAmountAsInCart;
    private Map<String, Integer> cartProductToCartQuantity = new HashMap<>();

    public OrderContextImpl(Product productImp, Order orderImp, User user) {
        this.order = orderImp;
        this.product = productImp;
        this.user = user;
    }

    @Override
    public void buildOrderContext(List<Product> cartProducts, String buyerMail)
            throws SQLException, ClassNotFoundException {
        List<Product> allDBProducts = this.product.getProducts();
        List<ProductImpl> filteredDBProducts = new ArrayList<>();
        List<String> cartProductsIds = new ArrayList<>();
        for (Product product : cartProducts) {
            ProductImpl castedCartProduct = (ProductImpl) product;
            cartProductToCartQuantity.put(castedCartProduct.getId(), castedCartProduct.getQuantity());
            cartProductsIds.add(castedCartProduct.getId());
            orderAmountAsInCart += castedCartProduct.getTotal();
        }
        for (Product product : allDBProducts) {
            ProductImpl castedDbProduct = (ProductImpl) product;
            if (cartProductsIds.indexOf(castedDbProduct.getId()) != -1)
                filteredDBProducts.add(castedDbProduct);
        }
        this.cartProductsSnapshot = filteredDBProducts; // has stock before placing order
        this.currentOrdersCount = this.order.getAllOrders().size();
        this.buyer = (UserImpl) user.getUserByEmail(buyerMail);
    }
}
