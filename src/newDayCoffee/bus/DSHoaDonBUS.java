package newDayCoffee.bus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;

/**
 * Xử lý nghiệp vụ trên danh sách hóa đơn.
 *
 * Lớp này giữ toàn bộ hóa đơn trong bộ nhớ và ủy quyền phần chi tiết hóa đơn
 * cho ChiTietHoaDonBUS. DTO do giao diện tạo và truyền vào; lớp này không tự
 * tạo đối tượng DTO.
 */
public class DSHoaDonBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<HoaDonDTO> dsHoaDon;
    private final ChiTietHoaDonBUS chiTietHoaDonBUS;

    private static final DateTimeFormatter DINH_DANG_MA =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public DSHoaDonBUS(ChiTietHoaDonBUS chiTietHoaDonBUS) {
        this.dsHoaDon = new ArrayList<>();
        this.chiTietHoaDonBUS = chiTietHoaDonBUS;
    }

    // ==================== KHU VỰC MÃ HÓA ĐƠN ====================
    /** Sinh mã hóa đơn duy nhất theo ngày hiện tại: HD20261101001... */
    public String taoMaHoaDon() {
        String tienTo = "HD" + LocalDate.now().format(DINH_DANG_MA);
        int soThuTu = 1;

        for (HoaDonDTO hoaDon : dsHoaDon) {
            if (hoaDon.getMaHD().startsWith(tienTo)) {
                soThuTu++;
            }
        }
        return tienTo + String.format("%03d", soThuTu);
    }

    // ==================== KHU VỰC ĐỌC DỮ LIỆU ====================
    /** Trả về bản sao danh sách hóa đơn để giao diện hiển thị. */
    public List<HoaDonDTO> layDanhSachHoaDon() {
        return new ArrayList<>(dsHoaDon);
    }

    /** Trả về bản sao chi tiết của một hóa đơn. */
    public List<ChiTietHoaDonDTO> layChiTiet(String maHD) {
        return chiTietHoaDonBUS.layTheoHoaDon(maHD);
    }

    public HoaDonDTO timTheoMa(String maHD) {
        String ma = chuanHoa(maHD);
        for (HoaDonDTO hoaDon : dsHoaDon) {
            if (hoaDon.getMaHD().equalsIgnoreCase(ma)) {
                return hoaDon;
            }
        }
        return null;
    }

    /** Hóa đơn được tạo gần nhất, dùng để in lại hóa đơn. */
    public HoaDonDTO layHoaDonGanNhat() {
        if (dsHoaDon.isEmpty()) {
            return null;
        }
        return dsHoaDon.get(dsHoaDon.size() - 1);
    }

    /** Tổng tiền của một hóa đơn, tính từ danh sách chi tiết. */
    public double tongTienHoaDon(HoaDonDTO hoaDon) {
        if (hoaDon == null) {
            return 0;
        }
        return chiTietHoaDonBUS.tongTienTheoHoaDon(hoaDon.getMaHD());
    }

    // ==================== KHU VỰC THÊM HÓA ĐƠN ====================
    /** Lưu một hóa đơn mới cùng chi tiết đang nằm trong giỏ hàng. */
    public boolean themHoaDon(HoaDonDTO hoaDon) {
        if (hoaDon == null || timTheoMa(hoaDon.getMaHD()) != null) {
            return false;
        }

        List<ChiTietHoaDonDTO> dsChiTietMoi = chiTietHoaDonBUS.layGioHang();
        if (dsChiTietMoi.isEmpty()) {
            return false;
        }

        dsHoaDon.add(hoaDon);
        chiTietHoaDonBUS.ganHoaDon(hoaDon, dsChiTietMoi);
        return true;
    }

    // ==================== KHU VỰC THỐNG KÊ HÔM NAY ====================
    /** Kiểm tra hóa đơn có được lập trong ngày hôm nay hay không. */
    private boolean laHomNay(HoaDonDTO hoaDon) {
        if (hoaDon == null || hoaDon.getNgayLap() == null) {
            return false;
        }
        return LocalDate.now().equals(hoaDon.getNgayLap());
    }

    /** Tổng doanh thu của các hóa đơn lập hôm nay. */
    public double doanhThuHomNay() {
        double tong = 0;
        for (HoaDonDTO hoaDon : dsHoaDon) {
            if (laHomNay(hoaDon)) {
                tong = tong + tongTienHoaDon(hoaDon);
            }
        }
        return tong;
    }

    /** Số hóa đơn đã hoàn tất trong hôm nay. */
    public int soDonHomNay() {
        int dem = 0;
        for (HoaDonDTO hoaDon : dsHoaDon) {
            if (laHomNay(hoaDon)) {
                dem++;
            }
        }
        return dem;
    }

    /** Tổng số lượng món đã bán trong hôm nay. */
    public int soLuongMonHomNay() {
        return chiTietHoaDonBUS.tongSoLuongTheoHoaDon(layMaHoaDonHomNay());
    }

    /**
     * Danh sách món bán chạy hôm nay, đã sắp xếp theo số lượng.
     * Mỗi phần tử gồm tên món (khóa) và tổng số lượng (giá trị).
     */
    public List<Map.Entry<String, Integer>> layMonBanChay(boolean giamDan) {
        Map<String, Integer> thongKe = chiTietHoaDonBUS.thongKeMonBanChay(layMaHoaDonHomNay());
        List<Map.Entry<String, Integer>> ketQua = new ArrayList<>(thongKe.entrySet());

        // Sắp xếp nổi bọt vì số lượng món trong ngày không lớn
        for (int i = 0; i < ketQua.size() - 1; i++) {
            for (int j = i + 1; j < ketQua.size(); j++) {
                boolean canDoi = giamDan
                        ? ketQua.get(j).getValue() > ketQua.get(i).getValue()
                        : ketQua.get(j).getValue() < ketQua.get(i).getValue();
                if (canDoi) {
                    Map.Entry<String, Integer> tam = ketQua.get(i);
                    ketQua.set(i, ketQua.get(j));
                    ketQua.set(j, tam);
                }
            }
        }
        return ketQua;
    }

    /** Mã các hóa đơn được lập trong ngày hôm nay. */
    private List<String> layMaHoaDonHomNay() {
        List<String> ketQua = new ArrayList<>();
        for (HoaDonDTO hoaDon : dsHoaDon) {
            if (laHomNay(hoaDon)) {
                ketQua.add(hoaDon.getMaHD());
            }
        }
        return ketQua;
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
