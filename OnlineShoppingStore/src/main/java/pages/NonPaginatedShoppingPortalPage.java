package pages;

import java.sql.SQLException;

interface  NonPaginatedShoppingPortalPage extends ShoppingPortalPage {
     String verifyPageElements() throws SQLException, ClassNotFoundException;
}
