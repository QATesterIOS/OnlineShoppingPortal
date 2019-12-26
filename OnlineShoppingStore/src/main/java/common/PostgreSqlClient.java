package common;

import java.sql.*;

public class PostgreSqlClient {
    private static Connection getPostgreSqlConnection() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/postgres", "munthera", "root");
            System.out.println("Connected to PostgreSQL database!");
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Failed to connect to PostgreSql db");
            throw e;
        }
    }

    public static ResultSet executeQuery(String query) throws SQLException, ClassNotFoundException {
        Connection connection = getPostgreSqlConnection();
        System.out.format("Executing query: %s\n", query);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        connection.close();
        return resultSet;
    }

    public static int executeUpdateQuery(String query) throws SQLException, ClassNotFoundException {
        Connection connection = getPostgreSqlConnection();
        System.out.format("Executing query: %s\n", query);
        PreparedStatement pstmt = connection.prepareStatement(query);
        int affectedRows = pstmt.executeUpdate();
        connection.close();
        return affectedRows;
    }
}
