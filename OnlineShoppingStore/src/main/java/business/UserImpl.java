package business;

import common.PostgreSqlClient;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class UserImpl implements User {

    private long userId;
    private boolean isActive;
    private String address;
    private String email;
    private String name;
    private String password;
    private String phone;
    private String role;

    @Override
    public User getUserByEmail(String email) throws SQLException, ClassNotFoundException {
        String getUser = "SELECT id, active, address, email, name, password, phone, role\n" +
                "FROM public.users WHERE email ='" + email +"';";

        ResultSet result = PostgreSqlClient.executeQuery(getUser);
        UserImpl user = new UserImpl();
        while (result.next()) {
            user.userId = result.getLong("id");
            user.address = result.getString("address");
            user.email = result.getString("email");
            user.isActive = result.getBoolean("active");
            user.name = result.getString("name");
            user.password = result.getString("password");
            user.phone = result.getString("phone");
            user.role = result.getString("role");
        }
        return user;
    }

    @Override
    public int deleteUserById(Long userId) throws SQLException, ClassNotFoundException {
        int numOfDeletedRows = 0;
        String deleteUser = "DELETE FROM public.users WHERE id= " + userId +";";
        String deleteUserFromCart = "DELETE FROM public.cart WHERE user_id= "+ userId +";";
        numOfDeletedRows+= PostgreSqlClient.executeUpdateQuery(deleteUserFromCart);
        numOfDeletedRows+= PostgreSqlClient.executeUpdateQuery(deleteUser);
        return numOfDeletedRows;
    }

    @Override
    public boolean areTwoUsersEquals(User actualUser, User expectedUser) {
        UserImpl newAdded = (UserImpl) actualUser;
        UserImpl dbUser = (UserImpl) expectedUser;
        return newAdded.getAddress().equals(dbUser.getAddress())
               && newAdded.getEmail().equals(dbUser.getEmail())
               && newAdded.getName().equals(dbUser.getName())
               && newAdded.getPhone().equals(dbUser.getPhone())
               && newAdded.getRole().equals(dbUser.getRole()) ;
    }
}
