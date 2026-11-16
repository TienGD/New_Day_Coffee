
package newDayCoffee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.dto.HoaDonDTO;

public class ChiTietHoaDonDAO {

    private static final String SELECT_COLUMNS =
            "SELECT ct.maHoaDon, ct.maDoUong, ct.soLuong, "
            + "du.tenDoUong, du.moTa, du.giaBan "
            + "FROM dbo.ChiTietHoaDon ct "
            + "INNER JOIN dbo.DoUong du ON ct.maDoUong = du.maDoUong";

    // Lấy tất cả chi tiết hóa đơn
    public ArrayList<ChiTietHoaDonDTO> getAll() {
        ArrayList<ChiTietHoaDonDTO> dsChiTiet = new ArrayList<>();
        String sql = SELECT_COLUMNS + " ORDER BY ct.maHoaDon, ct.maDoUong";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsChiTiet.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsChiTiet;
    }

    // Lấy chi tiết theo mã hóa đơn
    public ArrayList<ChiTietHoaDonDTO> getByMaHD(String maHD) {
        ArrayList<ChiTietHoaDonDTO> dsChiTiet = new ArrayList<>();
        if (maHD == null || maHD.trim().isEmpty()) {
            return dsChiTiet;
        }
        String sql = SELECT_COLUMNS + " WHERE ct.maHoaDon = ? ORDER BY ct.maDoUong";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD.trim());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dsChiTiet.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsChiTiet;
    }

    private static ChiTietHoaDonDTO mapRow(ResultSet rs) throws SQLException {
        HoaDonDTO hd = new HoaDonDTO();
        hd.setMaHD(nullToEmpty(rs.getString("maHoaDon")));

        double giaBan = rs.getDouble("giaBan");

        DoUongDTO du = new DoUongDTO();
        du.setMaMon(nullToEmpty(rs.getString("maDoUong")));
        du.setTenMon(nullToEmpty(rs.getString("tenDoUong")));
        du.setMoTa(nullToEmpty(rs.getString("moTa")));
        du.setDonGia(giaBan);

        ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO();
        ct.setHoaDon(hd);
        ct.setDoUong(du);
        ct.setSoLuong(rs.getInt("soLuong"));
        // ct.setDonGia(giaBan); // Vô hiệu hóa lệnh này vì đơn giá đã được nạp qua DoUongDTO (du.setDonGia)
        return ct;
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
