package newDayCoffee.bus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.utils.Validator;

/**
 * Xử lý nghiệp vụ trên danh sách hóa đơn.
 *
 * Lớp này giữ toàn bộ hóa đơn và chi tiết hóa đơn trong bộ nhớ,
 * đồng thời cung cấp các số liệu thống kê cho màn hình Tổng quan.
 * HoaDonBUS (xử lý một phiên bán hàng) sẽ gọi lớp này để lưu hóa đơn.
 */
public class DSHoaDonBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<HoaDonDTO> dsHoaDon;
    private final List<ChiTietHoaDonDTO> dsChiTiet;

    private static final DateTimeFormatter DINH_DANG_NGAY =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DINH_DANG_MA =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public DSHoaDonBUS() {
        dsHoaDon = new ArrayList<>();
        dsChiTiet = new ArrayList<>();
    }

    // ==================== KHU VỰC MÃ HÓA ĐƠN ====================
    /**
     * Sinh mã hóa đơn duy nhất theo ngày hiện tại.
     * Ví dụ: HD20261101001, HD20261101002...
     */
    public String taoMaHoaDon() {
        String tienTo = "HD" + LocalDate.now().format(DINH_DANG_MA);
        int soThuTu = 1;

        // Đếm số hóa đơn đã có cùng tiền tố ngày để chọn số thứ tự kế tiếp
        for (int i = 0; i < dsHoaDon.size(); i++) {
            String maDangCo = dsHoaDon.get(i).getMaHD();
            if (maDangCo.startsWith(tienTo)) {
                soThuTu++;
            }
        }
        return tienTo + String.format("%03d", soThuTu);
    }

    // ==================== KHU VỰC ĐỌC DỮ LIỆU ====================
    /** Trả về bản sao danh sách hóa đơn để giao diện không sửa trực tiếp dữ liệu gốc. */
    public List<HoaDonDTO> layDanhSachHoaDon() {
        return new ArrayList<>(dsHoaDon);
    }

    /** Trả về bản sao chi tiết của một hóa đơn. */
    public List<ChiTietHoaDonDTO> layChiTiet(String maHD) {
        List<ChiTietHoaDonDTO> ketQua = new ArrayList<>();
        String ma = Validator.chuanHoa(maHD);
        for (int i = 0; i < dsChiTiet.size(); i++) {
            ChiTietHoaDonDTO chiTiet = dsChiTiet.get(i);
            if (chiTiet.getMaHD().equalsIgnoreCase(ma)) {
                ketQua.add(chiTiet);
            }
        }
        return ketQua;
    }

    public HoaDonDTO timTheoMa(String maHD) {
        String ma = Validator.chuanHoa(maHD);
        for (int i = 0; i < dsHoaDon.size(); i++) {
            if (dsHoaDon.get(i).getMaHD().equalsIgnoreCase(ma)) {
                return dsHoaDon.get(i);
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

    // ==================== KHU VỰC THÊM HÓA ĐƠN ====================
    /**
     * Lưu một hóa đơn mới cùng chi tiết của nó.
     *
     * @return true nếu lưu thành công.
     */
    public boolean themHoaDon(HoaDonDTO hoaDon, List<ChiTietHoaDonDTO> dsChiTietMoi) {
        if (hoaDon == null || dsChiTietMoi == null || dsChiTietMoi.isEmpty()) {
            return false;
        }
        if (timTheoMa(hoaDon.getMaHD()) != null) {
            return false;
        }

        dsHoaDon.add(hoaDon);
        for (int i = 0; i < dsChiTietMoi.size(); i++) {
            ChiTietHoaDonDTO chiTiet = dsChiTietMoi.get(i);
            chiTiet.setMaHD(hoaDon.getMaHD());
            dsChiTiet.add(chiTiet);
        }
        return true;
    }

    // ==================== KHU VỰC THỐNG KÊ HÔM NAY ====================
    /** Kiểm tra hóa đơn có được lập trong ngày hôm nay hay không. */
    private boolean laHomNay(HoaDonDTO hoaDon) {
        if (hoaDon == null || Validator.rong(hoaDon.getNgayLap())) {
            return false;
        }
        String ngayHomNay = LocalDate.now().format(DINH_DANG_NGAY);
        return hoaDon.getNgayLap().startsWith(ngayHomNay);
    }

    /** Tổng doanh thu của các hóa đơn lập hôm nay. */
    public double doanhThuHomNay() {
        double tong = 0;
        for (int i = 0; i < dsHoaDon.size(); i++) {
            HoaDonDTO hoaDon = dsHoaDon.get(i);
            if (laHomNay(hoaDon)) {
                tong = tong + hoaDon.getTongTien();
            }
        }
        return tong;
    }

    /** Số hóa đơn đã hoàn tất trong hôm nay. */
    public int soDonHomNay() {
        int dem = 0;
        for (int i = 0; i < dsHoaDon.size(); i++) {
            if (laHomNay(dsHoaDon.get(i))) {
                dem++;
            }
        }
        return dem;
    }

    /** Tổng số lượng món đã bán trong hôm nay. */
    public int soLuongMonHomNay() {
        int tong = 0;
        for (int i = 0; i < dsChiTiet.size(); i++) {
            ChiTietHoaDonDTO chiTiet = dsChiTiet.get(i);
            HoaDonDTO hoaDon = timTheoMa(chiTiet.getMaHD());
            if (laHomNay(hoaDon)) {
                tong = tong + chiTiet.getSoLuong();
            }
        }
        return tong;
    }

    /**
     * Tổng hợp số lượng bán ra của từng món trong hôm nay.
     * Trả về danh sách ChiTietHoaDonDTO chỉ dùng tenMon và soLuong.
     */
    public List<ChiTietHoaDonDTO> thongKeMonBanChay() {
        List<ChiTietHoaDonDTO> ketQua = new ArrayList<>();

        for (int i = 0; i < dsChiTiet.size(); i++) {
            ChiTietHoaDonDTO chiTiet = dsChiTiet.get(i);
            HoaDonDTO hoaDon = timTheoMa(chiTiet.getMaHD());
            if (!laHomNay(hoaDon)) {
                continue;
            }

            ChiTietHoaDonDTO dongDaCo = timDongThongKe(ketQua, chiTiet.getMaMon());
            if (dongDaCo == null) {
                ketQua.add(new ChiTietHoaDonDTO("", chiTiet.getMaMon(),
                        chiTiet.getTenMon(), chiTiet.getSoLuong(), chiTiet.getDonGia()));
            } else {
                dongDaCo.setSoLuong(dongDaCo.getSoLuong() + chiTiet.getSoLuong());
            }
        }
        return ketQua;
    }

    private ChiTietHoaDonDTO timDongThongKe(List<ChiTietHoaDonDTO> ds, String maMon) {
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaMon().equalsIgnoreCase(maMon)) {
                return ds.get(i);
            }
        }
        return null;
    }

    /**
     * Danh sách món bán chạy hôm nay, đã sắp xếp theo số lượng.
     *
     * @param giamDan true: bán chạy nhất trước, false: bán chậm nhất trước.
     */
    public List<ChiTietHoaDonDTO> layMonBanChay(boolean giamDan) {
        List<ChiTietHoaDonDTO> ketQua = thongKeMonBanChay();

        // Sắp xếp nổi bọt cho dễ hiểu, số lượng món trong ngày không lớn
        for (int i = 0; i < ketQua.size() - 1; i++) {
            for (int j = i + 1; j < ketQua.size(); j++) {
                boolean canDoi = false;
                if (giamDan) {
                    canDoi = ketQua.get(j).getSoLuong() > ketQua.get(i).getSoLuong();
                } else {
                    canDoi = ketQua.get(j).getSoLuong() < ketQua.get(i).getSoLuong();
                }
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
    /** Định dạng tiền Việt Nam để hiển thị: 15000 -> 15.000Đ. */
    public static String dinhDangTien(double soTien) {
        return Validator.dinhDangTien(soTien);
    }
}
