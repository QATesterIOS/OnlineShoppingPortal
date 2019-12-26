package business;

import java.sql.SQLException;
import java.util.List;

public interface OrderContext {
    void buildOrderContext(List<Product> cartProducts, String buyerMail) throws SQLException, ClassNotFoundException;
}
