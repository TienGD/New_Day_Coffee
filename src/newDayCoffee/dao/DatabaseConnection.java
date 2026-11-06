
package newDayCoffee.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        Properties properties = new Properties();

        try (InputStream input = DatabaseConnection.class
                .getResourceAsStream("/database.properties")) {

            if (input == null) {
                throw new SQLException("Không tìm thấy database.properties");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new SQLException("Lỗi đọc file cấu hình", e);
        }

        String driver = properties.getProperty("db.driver");
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy JDBC Driver", e);
        }

        return DriverManager.getConnection(url, user, password);
    }
}
