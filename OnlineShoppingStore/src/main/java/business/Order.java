package business;

import java.sql.SQLException;
import java.util.List;

public interface Order {

    List<Order> getAllOrders() throws SQLException, ClassNotFoundException;
    Order getSpecificOrder(Long orderId) throws SQLException, ClassNotFoundException;
    Order getOrderWithNewStatus() throws SQLException, ClassNotFoundException;
    int updateOrderStatus(Long orderId, int newStatus) throws SQLException, ClassNotFoundException;
    Order getLatestOrder() throws SQLException, ClassNotFoundException;
    String isOrderSuccessfullyPlaced(OrderContext orderContext) throws SQLException, ClassNotFoundException;
}
