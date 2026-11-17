
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

    // Thêm tài khoản mới vào CSDL
    public boolean insert(TaiKhoanDTO tk) {
        if (tk == null || tk.getMaNV() == null || tk.getMaNV().getMaNV() == null) {
            return false;
        }
        String maNV = tk.getMaNV().getMaNV().trim().toLowerCase();
        String maDangNhap = tk.getMaDangNhap() != null && !tk.getMaDangNhap().trim().isEmpty()
                ? tk.getMaDangNhap().trim().toLowerCase()
                : maNV;
        if (maNV.isEmpty() || maDangNhap.isEmpty()) {
            return false;
        }
        String sql = "INSERT INTO dbo.TaiKhoan (maDangNhap, matKhau, maNV) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maDangNhap);
            ps.setString(2, tk.getMatKhau() != null ? tk.getMatKhau().trim() : "");
            ps.setString(3, maNV);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật mật khẩu tài khoản trong CSDL
    public boolean update(TaiKhoanDTO tk) {
        if (tk == null) {
            return false;
        }
        String maNV = (tk.getMaNV() != null && tk.getMaNV().getMaNV() != null)
                ? tk.getMaNV().getMaNV().trim()
                : "";
        String maDangNhap = tk.getMaDangNhap() != null && !tk.getMaDangNhap().trim().isEmpty()
                ? tk.getMaDangNhap().trim()
                : maNV;
        if (maDangNhap.isEmpty() && maNV.isEmpty()) {
            return false;
        }
        String sql = "UPDATE dbo.TaiKhoan SET matKhau = ? WHERE maDangNhap = ? OR maNV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getMatKhau() != null ? tk.getMatKhau().trim() : "");
            ps.setString(2, maDangNhap);
            ps.setString(3, maNV.isEmpty() ? maDangNhap : maNV);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa tài khoản khỏi CSDL theo mã đăng nhập hoặc mã nhân viên
    public boolean delete(String maDangNhap) {
        if (maDangNhap == null || maDangNhap.trim().isEmpty()) {
            return false;
        }
        String sql = "DELETE FROM dbo.TaiKhoan WHERE maDangNhap = ? OR maNV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String ma = maDangNhap.trim();
            ps.setString(1, ma);
            ps.setString(2, ma);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
        String maDangNhap = nullToEmpty(rs.getString("maDangNhap"));
        return new TaiKhoanDTO(nv, matKhau, maDangNhap);
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
