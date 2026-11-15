
package newDayCoffee.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static String url;
    private static String user;
    private static String password;
    private static SQLException initError;

    static {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getResourceAsStream("/database.properties")) {
            if (input == null) {
                initError = new SQLException("Không tìm thấy file /database.properties");
            } else {
                properties.load(input);
                String driver = properties.getProperty("db.driver");
                String loadedUrl = properties.getProperty("db.url");
                String loadedUser = properties.getProperty("db.user");
                String loadedPassword = properties.getProperty("db.password");

                if (isBlank(driver) || isBlank(loadedUrl) || isBlank(loadedUser) || isBlank(loadedPassword)) {
                    initError = new SQLException("Cấu hình database.properties thiếu thông tin bắt buộc.");
                } else {
                    Class.forName(driver.trim());
                    url = loadedUrl.trim();
                    user = loadedUser.trim();
                    password = loadedPassword.trim();
                }
            }
        } catch (IOException e) {
            initError = new SQLException("Lỗi đọc file cấu hình database.properties", e);
        } catch (ClassNotFoundException e) {
            initError = new SQLException("Không tìm thấy JDBC Driver", e);
        }
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (initError != null) {
            throw new SQLException(initError.getMessage(), initError);
        }
        return DriverManager.getConnection(url, user, password);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
