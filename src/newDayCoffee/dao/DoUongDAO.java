
package newDayCoffee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import newDayCoffee.dto.DoUongDTO;

public class DoUongDAO {

    private static final String SELECT_COLUMNS =
            "SELECT maDoUong, tenDoUong, moTa, giaBan FROM dbo.DoUong";

    // Lấy tất cả đồ uống
    public ArrayList<DoUongDTO> getAll() {
        ArrayList<DoUongDTO> dsDoUong = new ArrayList<>();
        String sql = SELECT_COLUMNS + " ORDER BY maDoUong";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsDoUong.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsDoUong;
    }

    // Tìm đồ uống theo mã
    public DoUongDTO getById(String maDoUong) {
        if (maDoUong == null || maDoUong.trim().isEmpty()) {
            return null;
        }
        String sql = SELECT_COLUMNS + " WHERE maDoUong = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maDoUong.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static DoUongDTO mapRow(ResultSet rs) throws SQLException {
        DoUongDTO du = new DoUongDTO();
        du.setMaMon(nullToEmpty(rs.getString("maDoUong")));
        du.setTenMon(nullToEmpty(rs.getString("tenDoUong")));
        du.setMoTa(nullToEmpty(rs.getString("moTa")));
        du.setDonGia(rs.getDouble("giaBan"));
        return du;
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
