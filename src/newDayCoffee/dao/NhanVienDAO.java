
package newDayCoffee.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import newDayCoffee.dto.NhanVienDTO;

public class NhanVienDAO {

    private static final String SELECT_COLUMNS =
            "SELECT maNV, tenNV, ngaySinh, soDienThoai, email, chucVu, gioiTinh "
            + "FROM dbo.NhanVien";

    // Lấy danh sách tất cả nhân viên
    public ArrayList<NhanVienDTO> getAll() {
        ArrayList<NhanVienDTO> dsNhanVien = new ArrayList<>();
        String sql = SELECT_COLUMNS + " ORDER BY maNV";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsNhanVien.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsNhanVien;
    }

    // Tìm nhân viên theo mã
    public NhanVienDTO getById(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            return null;
        }
        String sql = SELECT_COLUMNS + " WHERE maNV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV.trim());

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

    // Thêm nhân viên mới vào CSDL
    public boolean insert(NhanVienDTO nv) {
        if (nv == null || nv.getMaNV() == null || nv.getMaNV().trim().isEmpty() || nv.getNgaySinh() == null) {
            return false;
        }
        String sql = "INSERT INTO dbo.NhanVien (maNV, tenNV, gioiTinh, soDienThoai, chucVu, ngaySinh, email) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String chucVu = nv.getChucVu() != null ? nv.getChucVu().trim() : "";
            if ("Quản lý".equalsIgnoreCase(chucVu)) {
                chucVu = "Quản lí";
            }

            ps.setString(1, nv.getMaNV().trim().toLowerCase());
            ps.setString(2, nv.getTenNV() != null ? nv.getTenNV().trim() : "");
            ps.setString(3, nv.getGioiTinh() != null ? nv.getGioiTinh().trim() : "Nam");
            ps.setString(4, nv.getSdt() != null ? nv.getSdt().trim() : "");
            ps.setString(5, chucVu);
            ps.setDate(6, Date.valueOf(nv.getNgaySinh()));
            ps.setString(7, nv.getEmail() != null ? nv.getEmail().trim() : "");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin nhân viên trong CSDL
    public boolean update(NhanVienDTO nv) {
        if (nv == null || nv.getMaNV() == null || nv.getMaNV().trim().isEmpty() || nv.getNgaySinh() == null) {
            return false;
        }
        String sql = "UPDATE dbo.NhanVien SET tenNV = ?, gioiTinh = ?, soDienThoai = ?, chucVu = ?, ngaySinh = ?, email = ? "
                + "WHERE maNV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String chucVu = nv.getChucVu() != null ? nv.getChucVu().trim() : "";
            if ("Quản lý".equalsIgnoreCase(chucVu)) {
                chucVu = "Quản lí";
            }

            ps.setString(1, nv.getTenNV() != null ? nv.getTenNV().trim() : "");
            ps.setString(2, nv.getGioiTinh() != null ? nv.getGioiTinh().trim() : "Nam");
            ps.setString(3, nv.getSdt() != null ? nv.getSdt().trim() : "");
            ps.setString(4, chucVu);
            ps.setDate(5, Date.valueOf(nv.getNgaySinh()));
            ps.setString(6, nv.getEmail() != null ? nv.getEmail().trim() : "");
            ps.setString(7, nv.getMaNV().trim());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa nhân viên khỏi CSDL theo mã
    public boolean delete(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            return false;
        }
        String sql = "DELETE FROM dbo.NhanVien WHERE maNV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static NhanVienDTO mapRow(ResultSet rs) throws SQLException {
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
        return nv;
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
