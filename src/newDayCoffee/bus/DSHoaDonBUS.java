package newDayCoffee.bus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;

/**
 * Xử lý nghiệp vụ trên danh sách hóa đơn.
 *
 * Lớp này giữ toàn bộ hóa đơn và chi tiết hóa đơn trong bộ nhớ,
 * đồng thời cung cấp số liệu thống kê cho màn hình Tổng quan.
 */
public class DSHoaDonBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<HoaDonDTO> dsHoaDon;
    private final List<ChiTietHoaDonDTO> dsChiTiet;

    private static final DateTimeFormatter DINH_DANG_MA =
            DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter DINH_DANG_NGAY =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public DSHoaDonBUS() {
        dsHoaDon = new ArrayList<>();
        dsChiTiet = new ArrayList<>();
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
        List<ChiTietHoaDonDTO> ketQua = new ArrayList<>();
        String ma = chuanHoa(maHD);
        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (chiTiet.getMaHD().equalsIgnoreCase(ma)) {
                ketQua.add(chiTiet);
            }
        }
        return ketQua;
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
        double tong = 0;
        List<ChiTietHoaDonDTO> ds = layChiTiet(hoaDon.getMaHD());
        for (ChiTietHoaDonDTO chiTiet : ds) {
            tong = tong + chiTiet.getThanhTien();
        }
        return tong;
    }

    // ==================== KHU VỰC THÊM HÓA ĐƠN ====================
    /** Lưu một hóa đơn mới cùng chi tiết của nó. */
    public boolean themHoaDon(HoaDonDTO hoaDon, List<ChiTietHoaDonDTO> dsChiTietMoi) {
        if (hoaDon == null || dsChiTietMoi == null || dsChiTietMoi.isEmpty()) {
            return false;
        }
        if (timTheoMa(hoaDon.getMaHD()) != null) {
            return false;
        }

        dsHoaDon.add(hoaDon);
        for (ChiTietHoaDonDTO chiTiet : dsChiTietMoi) {
            chiTiet.setHoaDon(hoaDon);
            dsChiTiet.add(chiTiet);
        }
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
        int tong = 0;
        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (laHomNay(timTheoMa(chiTiet.getMaHD()))) {
                tong = tong + chiTiet.getSoLuong();
            }
        }
        return tong;
    }

    /**
     * Tổng hợp số lượng bán ra của từng món trong hôm nay.
     * Kết quả chỉ dùng tên món và số lượng.
     */
    public List<ChiTietHoaDonDTO> thongKeMonBanChay() {
        List<ChiTietHoaDonDTO> ketQua = new ArrayList<>();

        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (!laHomNay(timTheoMa(chiTiet.getMaHD()))) {
                continue;
            }

            ChiTietHoaDonDTO dongDaCo = timDongThongKe(ketQua, chiTiet.getMaMon());
            if (dongDaCo == null) {
                ketQua.add(new ChiTietHoaDonDTO(new HoaDonDTO(), chiTiet.getDoUong(),
                        chiTiet.getSoLuong(), chiTiet.getDonGia()));
            } else {
                dongDaCo.setSoLuong(dongDaCo.getSoLuong() + chiTiet.getSoLuong());
            }
        }
        return ketQua;
    }

    private ChiTietHoaDonDTO timDongThongKe(List<ChiTietHoaDonDTO> ds, String maMon) {
        for (ChiTietHoaDonDTO chiTiet : ds) {
            if (chiTiet.getMaMon().equalsIgnoreCase(maMon)) {
                return chiTiet;
            }
        }
        return null;
    }

    /** Danh sách món bán chạy hôm nay, đã sắp xếp theo số lượng. */
    public List<ChiTietHoaDonDTO> layMonBanChay(boolean giamDan) {
        List<ChiTietHoaDonDTO> ketQua = thongKeMonBanChay();

        // Sắp xếp nổi bọt vì số lượng món trong ngày không lớn
        for (int i = 0; i < ketQua.size() - 1; i++) {
            for (int j = i + 1; j < ketQua.size(); j++) {
                boolean canDoi = giamDan
                        ? ketQua.get(j).getSoLuong() > ketQua.get(i).getSoLuong()
                        : ketQua.get(j).getSoLuong() < ketQua.get(i).getSoLuong();
                if (canDoi) {
                    ChiTietHoaDonDTO tam = ketQua.get(i);
                    ketQua.set(i, ketQua.get(j));
                    ketQua.set(j, tam);
                }
            }
        }
        return ketQua;
    }

    // ==================== KHU VỰC ĐỊNH DẠNG ====================
    /** Định dạng ngày hiển thị: dd-MM-yyyy. */
    public static String dinhDangNgay(LocalDate ngay) {
        if (ngay == null) {
            return "";
        }
        return ngay.format(DINH_DANG_NGAY);
    }

    /** Định dạng tiền Việt Nam để hiển thị: 15000 -> 15.000Đ. */
    public static String dinhDangTien(double soTien) {
        return String.format(Locale.US, "%,.0f", soTien).replace(',', '.') + "Đ";
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
