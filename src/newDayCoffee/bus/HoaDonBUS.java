package newDayCoffee.bus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.utils.Auth;

/**
 * Xử lý nghiệp vụ của một phiên bán hàng (thanh toán và xuất hóa đơn).
 *
 * Lớp này nhận DSHoaDonBUS để lưu hóa đơn và ChiTietHoaDonBUS để lấy giỏ hàng.
 * HoaDonDTO do giao diện tạo và truyền vào; lớp này không tự tạo đối tượng DTO.
 */
public class HoaDonBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final DSHoaDonBUS dsHoaDonBUS;
    private final ChiTietHoaDonBUS chiTietHoaDonBUS;
    private HoaDonDTO hoaDonVuaTao;

    public HoaDonBUS(DSHoaDonBUS dsHoaDonBUS, ChiTietHoaDonBUS chiTietHoaDonBUS) {
        this.dsHoaDonBUS = dsHoaDonBUS;
        this.chiTietHoaDonBUS = chiTietHoaDonBUS;
        this.hoaDonVuaTao = null;
    }

    // ==================== KHU VỰC THANH TOÁN ====================
    /** Lập hóa đơn cho giỏ hàng hiện tại và lưu vào DSHoaDonBUS. */
    public String thanhToan(HoaDonDTO hoaDon) {
        hoaDonVuaTao = null;

        if (chiTietHoaDonBUS.gioHangRong()) {
            return "Đơn hàng chưa có món nào để thanh toán.";
        }
        if (!Auth.isDangNhap()) {
            return "Vui lòng đăng nhập trước khi thanh toán.";
        }
        if (hoaDon == null) {
            return "Thông tin hóa đơn không hợp lệ.";
        }

        hoaDon.setMaHD(dsHoaDonBUS.taoMaHoaDon());
        hoaDon.setNgayLap(LocalDate.now());
        hoaDon.setMaNV(Auth.getNguoiDung());
        hoaDon.setGhiChu(chuanHoa(hoaDon.getGhiChu()));
        hoaDon.setTrangThai("Đã thanh toán");

        if (!dsHoaDonBUS.themHoaDon(hoaDon)) {
            return "Không thể lưu hóa đơn. Vui lòng thử lại.";
        }

        hoaDonVuaTao = hoaDon;
        return null;
    }

    /** Hóa đơn vừa thanh toán thành công, dùng để in phiếu. */
    public HoaDonDTO layHoaDonVuaTao() {
        return hoaDonVuaTao;
    }

    // ==================== KHU VỰC XUẤT HÓA ĐƠN ====================
    /** Xuất nội dung hóa đơn ra file văn bản UTF-8 trong thư mục "hoadon". */
    public String xuatHoaDonRaFile(HoaDonDTO hoaDon, String noiDung) {
        if (hoaDon == null || noiDung == null) {
            return null;
        }

        try {
            Path thuMuc = Paths.get("hoadon");
            Files.createDirectories(thuMuc);
            Path file = thuMuc.resolve(hoaDon.getMaHD() + ".txt");
            Files.write(file, noiDung.getBytes(StandardCharsets.UTF_8));
            return file.toAbsolutePath().toString();
        } catch (IOException e) {
            return null;
        }
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
