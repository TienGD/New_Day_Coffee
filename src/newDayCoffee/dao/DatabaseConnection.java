package newDayCoffee.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Quản lý kết nối cơ sở dữ liệu MySQL.
 *
 * Đọc thông tin kết nối từ file resources/database.properties.
 * Dùng getConnection() để lấy kết nối; đóng kết nối sau mỗi thao tác.
 */
public class DatabaseConnection {

    private static final String PROPERTIES_FILE = "/database.properties";

    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new ExceptionInInitializerError(
                    "Không tìm thấy file cấu hình: " + PROPERTIES_FILE);
            }
            Properties props = new Properties();
            props.load(input);

            String driver = props.getProperty("db.driver");
            url      = props.getProperty("db.url");
            user     = props.getProperty("db.user");
            password = props.getProperty("db.password");

            Class.forName(driver);
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DatabaseConnection() {
        // Lớp tiện ích, không cho phép khởi tạo
    }

    /**
     * Trả về một kết nối mới tới cơ sở dữ liệu.
     * Người gọi có trách nhiệm đóng kết nối sau khi dùng xong.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
