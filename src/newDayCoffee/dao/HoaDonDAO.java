
package newDayCoffee.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.dto.NhanVienDTO;

public class HoaDonDAO {

    private static final String SELECT_COLUMNS =
            "SELECT hd.maHoaDon, hd.ngayLap, hd.tienKhachTra, hd.ghiChu, hd.trangThai, hd.maNV, "
            + "nv.tenNV, nv.ngaySinh, nv.soDienThoai, nv.email, nv.chucVu, nv.gioiTinh "
            + "FROM dbo.HoaDon hd "
            + "INNER JOIN dbo.NhanVien nv ON hd.maNV = nv.maNV";

    // Lấy tất cả hóa đơn
    public ArrayList<HoaDonDTO> getAll() {
        ArrayList<HoaDonDTO> dsHoaDon = new ArrayList<>();
        String sql = SELECT_COLUMNS + " ORDER BY hd.ngayLap, hd.maHoaDon";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsHoaDon.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsHoaDon;
    }

    // Tìm hóa đơn theo mã
    public HoaDonDTO getById(String maHD) {
        if (maHD == null || maHD.trim().isEmpty()) {
            return null;
        }
        String sql = SELECT_COLUMNS + " WHERE hd.maHoaDon = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD.trim());

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

    // Thêm hóa đơn mới vào CSDL
    public boolean insert(HoaDonDTO hd) {
        if (hd == null || hd.getMaHD() == null || hd.getMaHD().trim().isEmpty()
                || hd.getMaNV() == null || hd.getMaNV().getMaNV() == null) {
            return false;
        }
        String sql = "INSERT INTO dbo.HoaDon (maHoaDon, ngayLap, tienKhachTra, ghiChu, maNV, trangThai) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHD().trim());
            if (hd.getNgayLap() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap().atStartOfDay()));
            } else {
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            }
            ps.setDouble(3, hd.getTienKhachDua());
            String ghiChu = hd.getGhiChu() != null ? hd.getGhiChu().trim() : "";
            if (ghiChu.isEmpty()) {
                ps.setNull(4, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(4, ghiChu);
            }
            ps.setString(5, hd.getMaNV().getMaNV().trim());
            String trangThai = (hd.getTrangThai() != null && !hd.getTrangThai().trim().isEmpty())
                    ? hd.getTrangThai().trim()
                    : "Đã thanh toán";
            ps.setString(6, trangThai);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static HoaDonDTO mapRow(ResultSet rs) throws SQLException {
        HoaDonDTO hd = new HoaDonDTO();
        hd.setMaHD(nullToEmpty(rs.getString("maHoaDon")));

        Timestamp ngayLapTs = rs.getTimestamp("ngayLap");
        if (ngayLapTs != null) {
            hd.setNgayLap(ngayLapTs.toLocalDateTime().toLocalDate());
        } else {
            Date ngayLapDate = rs.getDate("ngayLap");
            if (ngayLapDate != null) {
                hd.setNgayLap(ngayLapDate.toLocalDate());
            }
        }

        hd.setTienKhachDua(rs.getDouble("tienKhachTra"));
        hd.setGhiChu(nullToEmpty(rs.getString("ghiChu")));
        hd.setTrangThai(nullToEmpty(rs.getString("trangThai")));

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
        hd.setMaNV(nv);

        return hd;
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
