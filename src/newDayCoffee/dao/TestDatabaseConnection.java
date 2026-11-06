package newDayCoffee.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseConnection {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection != null && !connection.isClosed()) {
                System.out.println("KET NOI SQL SERVER THANH CONG!");
                System.out.println("Database: "
                        + connection.getCatalog());
            }

        } catch (SQLException e) {
            System.out.println("KET NOI THAT BAI!");
            e.printStackTrace();
        }
    }
}