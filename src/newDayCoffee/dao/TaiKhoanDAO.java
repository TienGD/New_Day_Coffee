
package newDayCoffee.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.dto.TaiKhoanDTO;

public class TaiKhoanDAO {

    private static final String SELECT_COLUMNS =
            "SELECT tk.maDangNhap, tk.matKhau, tk.maNV, "
            + "nv.tenNV, nv.ngaySinh, nv.soDienThoai, nv.email, nv.chucVu, nv.gioiTinh "
            + "FROM dbo.TaiKhoan tk "
            + "INNER JOIN dbo.NhanVien nv ON tk.maNV = nv.maNV";

    // Lấy tất cả tài khoản
    public ArrayList<TaiKhoanDTO> getAll() {
        ArrayList<TaiKhoanDTO> dsTaiKhoan = new ArrayList<>();
        String sql = SELECT_COLUMNS + " ORDER BY tk.maNV";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsTaiKhoan.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsTaiKhoan;
    }

    // Tìm tài khoản theo mã đăng nhập
    public TaiKhoanDTO getById(String maDangNhap) {
        if (maDangNhap == null || maDangNhap.trim().isEmpty()) {
            return null;
        }
        String sql = SELECT_COLUMNS + " WHERE tk.maDangNhap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maDangNhap.trim());

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

    private static TaiKhoanDTO mapRow(ResultSet rs) throws SQLException {
        NhanVienDTO nv = new NhanVienDTO();
        nv.setMaNV(nullToEmpty(rs.getString("maNV")));
        nv.setTenNV(nullToEmpty(rs.getString("tenNV")));
        Date ngaySinh = rs.getDate("ngaySinh");
        if (ngaySinh != null) {
            nv.setNgaySinh(ngaySinh.toLocalDate());
        }
        nv.setSdt(nullToEmpty(rs.getString("soDienThoai")));
        nv.setEmail(nullToEmpty(rs.getString("email")));
        nv.setChucVu(nullToEmpty(rs.getString("chucVu")));
        nv.setGioiTinh(nullToEmpty(rs.getString("gioiTinh")));

        String matKhau = nullToEmpty(rs.getString("matKhau"));
        return new TaiKhoanDTO(nv, matKhau);
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
