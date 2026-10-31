package newDayCoffee.bus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.DSHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;

/**
 * Xử lý nghiệp vụ trên danh sách hóa đơn.
 *
 * Chức năng chính:
 *  - Sinh mã hóa đơn duy nhất.
 *  - Lưu hóa đơn + chi tiết hóa đơn.
 *  - Thống kê doanh thu, số đơn và số lượng món bán ra cho màn hình Tổng quan.
 */
public class DSHoaDonBUS {

    // ==================== KHU VỰC SINGLETON ====================
    private static DSHoaDonBUS instance;

    public static synchronized DSHoaDonBUS getInstance() {
        if (instance == null) {
            instance = new DSHoaDonBUS();
        }
        return instance;
    }

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final DSHoaDonDTO dsHoaDon;

    private DSHoaDonBUS() {
        dsHoaDon = new DSHoaDonDTO();
        taoDuLieuMau();
    }

    /** Tạo vài hóa đơn mẫu trong ngày để màn hình Tổng quan có số liệu. */
    private void taoDuLieuMau() {
        String homNay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        themHoaDonMau("NV02", homNay + " 08:15:00", 100000, "Tiền mặt", "Khách quen",
                new Object[][] {
                    {"cof01", "Cà phê đen", 1, 15000},
                    {"tea01", "Trà Đào", 2, 25000}
                });

        themHoaDonMau("NV01", homNay + " 09:30:00", 100000, "Tiền mặt", "",
                new Object[][] {
                    {"mil01", "Trà Sữa Thái", 2, 25000},
                    {"bak01", "Bánh Tiramisu", 2, 20000}
                });

        themHoaDonMau("NV02", homNay + " 14:00:00", 50000, "Chuyển khoản", "",
                new Object[][] {
                    {"cof02", "Bạc sỉu", 1, 20000},
                    {"tea03", "Trà Vải", 1, 28000}
                });
    }

    /**
     * Tạo một hóa đơn mẫu.
     *
     * @param dsMon mỗi phần tử gồm {mã món, tên món, số lượng, đơn giá}
     */
    private void themHoaDonMau(String maNV, String ngayLap, double tienKhachDua,
            String phuongThucTT, String ghiChu, Object[][] dsMon) {
        double tongTien = 0;
        for (int i = 0; i < dsMon.length; i++) {
            tongTien += ((Integer) dsMon[i][2]) * ((Integer) dsMon[i][3]);
        }

        HoaDonDTO hd = new HoaDonDTO(sinhMaHoaDon(), ngayLap, maNV, tongTien,
                tienKhachDua, tienKhachDua - tongTien, phuongThucTT, ghiChu);

        List<ChiTietHoaDonDTO> dsChiTiet = new ArrayList<>();
        for (int i = 0; i < dsMon.length; i++) {
            dsChiTiet.add(new ChiTietHoaDonDTO(hd.getMaHD(), (String) dsMon[i][0],
                    (String) dsMon[i][1], (Integer) dsMon[i][2], (Integer) dsMon[i][3]));
        }
        themHoaDon(hd, dsChiTiet);
    }

    // ==================== KHU VỰC SINH MÃ ====================
    /** Sinh mã hóa đơn theo dạng HDyyyyMMddXXX, XXX là số thứ tự trong ngày. */
    public String sinhMaHoaDon() {
        String ngay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int soThuTu = 1;
        while (true) {
            String ma = "HD" + ngay + String.format("%03d", soThuTu);
            if (dsHoaDon.timHoaDon(ma) == null) {
                return ma;
            }
            soThuTu++;
        }
    }

    // ==================== KHU VỰC LƯU HÓA ĐƠN ====================
    /**
     * Lưu hóa đơn và toàn bộ chi tiết của nó.
     *
     * @return true nếu lưu thành công.
     */
    public boolean themHoaDon(HoaDonDTO hd, List<ChiTietHoaDonDTO> dsChiTiet) {
        if (hd == null || dsChiTiet == null || dsChiTiet.isEmpty()) {
            return false;
        }
        if (!dsHoaDon.themHoaDon(hd)) {
            return false;
        }
        for (ChiTietHoaDonDTO ct : dsChiTiet) {
            if (ct == null) {
                return false;
            }
            ct.setMaHD(hd.getMaHD());
            if (!dsHoaDon.themChiTiet(ct)) {
                return false;
            }
        }
        dsHoaDon.luuVaoCSDL();
        return true;
    }

    // ==================== KHU VỰC ĐỌC DỮ LIỆU ====================
    public List<HoaDonDTO> layDanhSachHoaDon() {
        return new ArrayList<>(dsHoaDon.getDsHoaDon());
    }

    public List<ChiTietHoaDonDTO> layChiTiet(String maHD) {
        return dsHoaDon.layChiTiet(maHD);
    }

    public HoaDonDTO timHoaDon(String maHD) {
        return dsHoaDon.timHoaDon(maHD);
    }

    // ==================== KHU VỰC THỐNG KÊ ====================
    public double doanhThuHomNay() {
        return dsHoaDon.tinhDoanhThuHomNay();
    }

    public int demDonHoanTat() {
        return dsHoaDon.demSoDonHoanTat();
    }

    /**
     * Một hóa đơn được xem là hoàn tất khi có ít nhất một chi tiết
     * và tiền khách đưa đủ cho tổng tiền.
     */
    public boolean laHoaDonHoanTat(HoaDonDTO hd) {
        if (hd == null) {
            return false;
        }
        return !dsHoaDon.layChiTiet(hd.getMaHD()).isEmpty()
                && hd.getTienKhachDua() >= hd.getTongTien();
    }

    /**
     * Thống kê tổng số lượng từng món đã bán trong các hóa đơn hoàn tất.
     * Trả về danh sách ChiTietHoaDonDTO (tận dụng lại DTO) đã sắp xếp giảm dần,
     * trong đó trường soLuong là tổng số lượng đã bán.
     */
    public List<ChiTietHoaDonDTO> thongKeSoLuongMon() {
        Map<String, Integer> tongSoLuong = new LinkedHashMap<>();
        Map<String, ChiTietHoaDonDTO> thongTinMon = new LinkedHashMap<>();

        for (HoaDonDTO hd : dsHoaDon.getDsHoaDon()) {
            if (!laHoaDonHoanTat(hd)) {
                continue;
            }
            for (ChiTietHoaDonDTO ct : dsHoaDon.layChiTiet(hd.getMaHD())) {
                String ma = ct.getMaMon();
                Integer cu = tongSoLuong.get(ma);
                tongSoLuong.put(ma, cu == null ? ct.getSoLuong() : cu + ct.getSoLuong());

                if (!thongTinMon.containsKey(ma)) {
                    thongTinMon.put(ma, new ChiTietHoaDonDTO("", ma, ct.getTenMon(), 0, ct.getDonGia()));
                }
            }
        }

        List<ChiTietHoaDonDTO> ketQua = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : tongSoLuong.entrySet()) {
            ChiTietHoaDonDTO ct = thongTinMon.get(entry.getKey());
            ct.setSoLuong(entry.getValue());
            ketQua.add(ct);
        }
        ketQua.sort((a, b) -> Integer.compare(b.getSoLuong(), a.getSoLuong()));
        return ketQua;
    }

    /** Số hóa đơn đã lập trong ngày hôm nay (kể cả chưa hoàn tất). */
    public int demHoaDonHomNay() {
        int dem = 0;
        String homNay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        for (HoaDonDTO hd : dsHoaDon.getDsHoaDon()) {
            if (hd.getNgayLap() != null && hd.getNgayLap().startsWith(homNay)) {
                dem++;
            }
        }
        return dem;
    }

    /** Lấy thời gian hiện tại theo định dạng lưu trong hóa đơn. */
    public String layThoiGianHienTai() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
