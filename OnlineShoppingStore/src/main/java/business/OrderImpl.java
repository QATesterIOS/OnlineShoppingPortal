package business;

import common.PostgreSqlClient;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static common.Constants.PASS;

@Data
public class OrderImpl implements Order {
    private Long orderId;
    private String customerAddress;
    private String customerEmail;
    private String customerName;
    private String customerPhone;
    private double orderAmount;
    private int orderStatus;
    private Timestamp creationDate;

    @Override
    public List<Order> getAllOrders() throws SQLException, ClassNotFoundException {
        List<Order> orders = new ArrayList<>();
        String getAllOrders = "SELECT order_id, buyer_address, buyer_email, buyer_name, " +
                "buyer_phone, create_time, order_amount, order_status, update_time\n" +
                "FROM public.order_main;";
        ResultSet result = PostgreSqlClient.executeQuery(getAllOrders);
        while (result.next()) {
            OrderImpl order = new OrderImpl();
            order.orderId = result.getLong("order_id");
            order.creationDate = result.getTimestamp("create_time");
            order.customerAddress = result.getString("buyer_address");
            order.customerEmail = result.getString("buyer_email");
            order.customerPhone = result.getString("buyer_phone");
            order.orderAmount = result.getDouble("order_amount");
            order.orderStatus = result.getInt("order_status");
            orders.add(order);
        }
        return orders;
    }

    @Override
    public Order getSpecificOrder(Long orderId) throws SQLException, ClassNotFoundException {
        String getOrder = "SELECT order_id, buyer_address, buyer_email, buyer_name, " +
                "buyer_phone, create_time, order_amount, order_status, update_time\n" +
                "FROM public.order_main WHERE order_id = " + orderId + ";";

        ResultSet result = PostgreSqlClient.executeQuery(getOrder);
        OrderImpl order = new OrderImpl();
        while (result.next()) {
            order.orderId = result.getLong("order_id");
            order.creationDate = result.getTimestamp("create_time");
            order.customerAddress = result.getString("buyer_address");
            order.customerEmail = result.getString("buyer_email");
            order.customerPhone = result.getString("buyer_phone");
            order.orderAmount = result.getDouble("order_amount");
            order.orderStatus = result.getInt("order_status");
            order.customerName = result.getString("buyer_name");
        }
        return order;
    }

    @Override
    public Order getOrderWithNewStatus() throws SQLException, ClassNotFoundException {
        String getOrder = "SELECT order_id, buyer_address, buyer_email, buyer_name, buyer_phone, " +
                "create_time, order_amount, order_status, update_time\n" +
                "FROM public.order_main WHERE order_status = 0 limit 1;";

        ResultSet result = PostgreSqlClient.executeQuery(getOrder);
        OrderImpl order = new OrderImpl();
        while (result.next()) {
            order.orderId = result.getLong("order_id");
            order.creationDate = result.getTimestamp("create_time");
            order.customerAddress = result.getString("buyer_address");
            order.customerEmail = result.getString("buyer_email");
            order.customerPhone = result.getString("buyer_phone");
            order.orderAmount = result.getDouble("order_amount");
            order.orderStatus = result.getInt("order_status");
            order.customerName = result.getString("buyer_name");
        }
        return order;
    }

    @Override
    public int updateOrderStatus(Long orderId, int newStatus) throws SQLException, ClassNotFoundException {
        String updateOrderStatus = "UPDATE public.order_main\n" +
                " SET order_status=" + newStatus +
                " WHERE order_id=" + orderId + ";";
        return PostgreSqlClient.executeUpdateQuery(updateOrderStatus);
    }

    @Override
    public Order getLatestOrder() throws SQLException, ClassNotFoundException {
        String getLatestOrderQuery = "SELECT order_id, buyer_address, buyer_email, buyer_name, " +
                "buyer_phone, create_time, order_amount, order_status, update_time\n" +
                "FROM public.order_main order by create_time desc limit 1;";

        ResultSet result = PostgreSqlClient.executeQuery(getLatestOrderQuery);
        OrderImpl order = new OrderImpl();
        while (result.next()) {
            order.orderId = result.getLong("order_id");
            order.creationDate = result.getTimestamp("create_time");
            order.customerAddress = result.getString("buyer_address");
            order.customerEmail = result.getString("buyer_email");
            order.customerPhone = result.getString("buyer_phone");
            order.orderAmount = result.getDouble("order_amount");
            order.orderStatus = result.getInt("order_status");
            order.customerName = result.getString("buyer_name");
        }
        return order;
    }

    @Override
    public String isOrderSuccessfullyPlaced(OrderContext orderContext) {
        StringBuilder result = new StringBuilder();
        OrderContextImpl orderContext1 = (OrderContextImpl) orderContext;
        OrderImpl latestOrder = null;
        try {
            latestOrder = (OrderImpl) getLatestOrder();
            verifyProductInOrderTableInfo(result, orderContext1, latestOrder);
        } catch (SQLException e) {
            result.append("SQLException while getting latest order info").append("\n");
        } catch (ClassNotFoundException e) {
            result.append("ClassNotFoundException while getting latest order info").append("\n");
        }
        try {
            int newOrdersCount = getAllOrders().size();
            verifyOrdersTableData(result, orderContext1, latestOrder, newOrdersCount);
            verifyProductInfoTableData(result, orderContext1);
        } catch (SQLException e) {
            result.append("SQLException while getting New Orders Count").append("\n");
        } catch (ClassNotFoundException e) {
            result.append("ClassNotFoundException while getting New Orders Count").append("\n");
        }
        return (!result.toString().trim().isEmpty()) ? result.toString() : PASS;
    }

    private void verifyProductInfoTableData(StringBuilder result, OrderContextImpl orderContext1)
            throws SQLException, ClassNotFoundException {
        List<Product> updatedProducts = orderContext1.getProduct().getProducts();
        List<ProductImpl> productsBeforeOrder = orderContext1.getCartProductsSnapshot();
        for (Product updated : updatedProducts) {
            ProductImpl upProd = (ProductImpl) updated;
            for (ProductImpl old : productsBeforeOrder) {
                if (upProd.getId().equals(old.getId())) {
                    if (upProd.getQuantity() !=
                            old.getQuantity() - orderContext1.getCartProductToCartQuantity().get(old.getId()))
                        result.append("Incorrect stock of product: ")
                                .append(old.getId()).append(" in product_info table after placing new order\n");
                }
            }
        }


    }

    private void verifyProductInOrderTableInfo(StringBuilder result, OrderContextImpl orderContext1,
                                               OrderImpl latestOrder) throws SQLException, ClassNotFoundException {
        String getLatestOrderProductInOrderData = "SELECT product_description, product_id, product_name, " +
                "product_price, product_stock\n" +
                "FROM public.product_in_order\n" +
                "where order_id=" + latestOrder.getOrderId() + ";";
        ResultSet qResult = PostgreSqlClient.executeQuery(getLatestOrderProductInOrderData);
        ProductImpl productInfo = new ProductImpl();
        while (qResult.next()) {
            productInfo.setDescription(qResult.getString("product_description"));
            productInfo.setId(qResult.getString("product_id"));
            productInfo.setName(qResult.getString("product_name"));
            productInfo.setPrice(qResult.getDouble("product_price"));
            productInfo.setQuantity(qResult.getInt("product_stock"));
        }
        orderContext1.getCartProductsSnapshot().forEach(cartProduct -> {
            if (cartProduct.getId().equals(productInfo.getId())) {
                if ((!cartProduct.getId().equals(productInfo.getId())
                        || !cartProduct.getDescription().equals(productInfo.getDescription())
                        || !cartProduct.getName().equals(productInfo.getName())
                        || cartProduct.getPrice() != productInfo.getPrice())
                        || cartProduct.getQuantity() != productInfo.getQuantity())
                    result.append("Incorrect product info is being added into product_in_order table");
            }
        });
    }

    private void verifyOrdersTableData(StringBuilder result, OrderContextImpl orderContext1,
                                       OrderImpl latestOrder, int newOrdersCount) {
        if (newOrdersCount <= orderContext1.getCurrentOrdersCount())
            result.append("Orders count didn't increase after placing last order\n");

        if (!latestOrder.getCustomerEmail().equals(orderContext1.getBuyer().getEmail()))
            result.append("New Order is being replaced with incorrect user email\n");

        if (orderContext1.getOrderAmountAsInCart() != latestOrder.getOrderAmount())
            result.append("New Order is being replaced with incorrect amount\n");

        if (latestOrder.getOrderStatus() != 0)
            result.append("New Order is being replaced with incorrect status\n");
    }
}
