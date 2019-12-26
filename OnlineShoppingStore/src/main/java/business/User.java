package business;

import java.sql.SQLException;

public interface User {
    User getUserByEmail(String email) throws SQLException, ClassNotFoundException;
    int deleteUserById(Long userId) throws SQLException, ClassNotFoundException;
    boolean areTwoUsersEquals(User actualUser, User expectedUser);
}
