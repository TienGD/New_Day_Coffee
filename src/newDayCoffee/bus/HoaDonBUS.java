package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.Validator;
import newDayCoffee.utils.XuatHoaDon;

/**
 * Xử lý nghiệp vụ cho một hóa đơn bán hàng.
 *
 * Nhiệm vụ:
 *  - Kiểm tra giỏ hàng và tiền khách đưa (thông qua Validator).
 *  - Lấy nhân viên đang đăng nhập từ Auth.
 *  - Tính tiền, tạo hóa đơn và chuyển cho DSHoaDonBUS lưu lại.
 */
public class HoaDonBUS {

    // ==================== KHU VỰC SINGLETON ====================
    private static HoaDonBUS instance;

    public static synchronized HoaDonBUS getInstance() {
        if (instance == null) {
            instance = new HoaDonBUS();
        }
        return instance;
    }

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final DSHoaDonBUS dsHoaDonBUS;
    private String loiGanNhat;

    private HoaDonBUS() {
        dsHoaDonBUS = DSHoaDonBUS.getInstance();
        loiGanNhat = "";
    }

    public String getLoiGanNhat() {
        return loiGanNhat;
    }

    // ==================== KHU VỰC TÍNH TIỀN ====================
    /** Cộng thành tiền của tất cả dòng trong giỏ hàng. */
    public double tinhTongTien(List<ChiTietHoaDonDTO> gioHang) {
        double tong = 0;
        if (gioHang == null) {
            return tong;
        }
        for (ChiTietHoaDonDTO ct : gioHang) {
            if (ct != null) {
                tong += ct.getThanhTien();
            }
        }
        return tong;
    }

    // ==================== KHU VỰC KIỂM TRA ====================
    /**
     * Kiểm tra giỏ hàng và thông tin thanh toán trước khi lập hóa đơn.
     *
     * @return null nếu hợp lệ, ngược lại là thông báo lỗi.
     */
    public String kiemTraThanhToan(List<ChiTietHoaDonDTO> gioHang,
            String tienKhachDuaText, String phuongThucTT) {
        if (!Auth.isLoggedIn()) {
            return "Phiên đăng nhập đã hết. Vui lòng đăng nhập lại.";
        }
        if (gioHang == null || gioHang.isEmpty()) {
            return "Đơn hàng chưa có món nào để thanh toán.";
        }

        for (ChiTietHoaDonDTO ct : gioHang) {
            if (ct == null || Validator.rong(ct.getMaMon())) {
                return "Có món không hợp lệ trong đơn hàng.";
            }
            String loiSoLuong = Validator.kiemTraSoLuong(ct.getSoLuong());
            if (loiSoLuong != null) {
                return "Món " + ct.getTenMon() + ": " + loiSoLuong;
            }
            if (ct.getDonGia() <= 0) {
                return "Món " + ct.getTenMon() + " có đơn giá không hợp lệ.";
            }
        }

        double tienKhachDua = Validator.docSoTien(tienKhachDuaText);
        if (tienKhachDua < 0) {
            return "Tiền khách đưa không hợp lệ. Vui lòng nhập số tiền.";
        }

        return Validator.kiemTraThanhToan(tinhTongTien(gioHang), tienKhachDua, phuongThucTT);
    }

    // ==================== KHU VỰC THANH TOÁN ====================
    /**
     * Lập hóa đơn từ giỏ hàng hiện tại.
     * Trước khi gọi nên gọi kiemTraThanhToan() để lấy thông báo lỗi cụ thể.
     *
     * @return hóa đơn vừa lập, hoặc null nếu thất bại (xem getLoiGanNhat()).
     */
    public HoaDonDTO thanhToan(List<ChiTietHoaDonDTO> gioHang,
            String tienKhachDuaText, String phuongThucTT, String ghiChu) {
        String loi = kiemTraThanhToan(gioHang, tienKhachDuaText, phuongThucTT);
        if (loi != null) {
            loiGanNhat = loi;
            return null;
        }

        double tongTien = tinhTongTien(gioHang);
        double tienKhachDua = Validator.docSoTien(tienKhachDuaText);

        HoaDonDTO hd = new HoaDonDTO();
        hd.setMaHD(dsHoaDonBUS.sinhMaHoaDon());
        hd.setNgayLap(dsHoaDonBUS.layThoiGianHienTai());
        hd.setMaNV(Auth.getCurrentUser().getMaNV());
        hd.setTongTien(tongTien);
        hd.setTienKhachDua(tienKhachDua);
        hd.setTienThoi(tienKhachDua - tongTien);
        hd.setPhuongThucTT(phuongThucTT);
        hd.setGhiChu(Validator.chuanHoa(ghiChu));

        // Sao chép chi tiết để không làm thay đổi giỏ hàng của giao diện
        List<ChiTietHoaDonDTO> dsChiTiet = new ArrayList<>();
        for (ChiTietHoaDonDTO ct : gioHang) {
            dsChiTiet.add(new ChiTietHoaDonDTO(hd.getMaHD(), ct.getMaMon(),
                    ct.getTenMon(), ct.getSoLuong(), ct.getDonGia()));
        }

        if (!dsHoaDonBUS.themHoaDon(hd, dsChiTiet)) {
            loiGanNhat = "Không thể lưu hóa đơn. Vui lòng thử lại.";
            return null;
        }

        loiGanNhat = "";
        return hd;
    }

    // ==================== KHU VỰC IN / XUẤT HÓA ĐƠN ====================
    /** Tạo nội dung hóa đơn dạng văn bản để hiển thị hoặc in. */
    public String taoNoiDungHoaDon(HoaDonDTO hd) {
        if (hd == null) {
            return "";
        }
        return XuatHoaDon.taoNoiDung(hd, dsHoaDonBUS.layChiTiet(hd.getMaHD()), layTenNhanVien(hd));
    }

    /** Xuất hóa đơn ra file văn bản. */
    public boolean xuatHoaDonRaFile(HoaDonDTO hd, String duongDan) {
        if (hd == null) {
            return false;
        }
        return XuatHoaDon.xuatFile(hd, dsHoaDonBUS.layChiTiet(hd.getMaHD()), layTenNhanVien(hd), duongDan);
    }

    /**
     * Lấy tên nhân viên lập hóa đơn.
     * Ưu tiên nhân viên ghi trên hóa đơn, nếu không tìm thấy mới dùng người đang đăng nhập.
     */
    private String layTenNhanVien(HoaDonDTO hd) {
        NhanVienDTO nv = NhanVienBUS.getInstance().timTheoMa(hd.getMaNV());
        if (nv != null) {
            return nv.getTenNV();
        }
        if (Auth.getCurrentUser() != null) {
            return Auth.getCurrentUser().getTenNV();
        }
        return hd.getMaNV();
    }
}
