package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;

/**
 * Xử lý nghiệp vụ trên chi tiết hóa đơn.
 *
 * Lớp này giữ giỏ hàng hiện tại và toàn bộ chi tiết của các hóa đơn đã lưu.
 * DTO do giao diện tạo và truyền vào; lớp này không tự tạo đối tượng DTO.
 */
public class ChiTietHoaDonBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<ChiTietHoaDonDTO> dsChiTiet;

    public ChiTietHoaDonBUS() {
        dsChiTiet = new ArrayList<>();
    }

    // ==================== KHU VỰC GIỎ HÀNG ====================
    /** Trả về bản sao các dòng đang nằm trong giỏ hàng. */
    public List<ChiTietHoaDonDTO> layGioHang() {
        List<ChiTietHoaDonDTO> ketQua = new ArrayList<>();
        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (laTrongGio(chiTiet)) {
                ketQua.add(chiTiet);
            }
        }
        return ketQua;
    }

    /** Tìm một dòng trong giỏ hàng theo mã món. */
    public ChiTietHoaDonDTO timTrongGio(String maMon) {
        String ma = chuanHoa(maMon);
        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (laTrongGio(chiTiet) && chiTiet.getMaMon().equalsIgnoreCase(ma)) {
                return chiTiet;
            }
        }
        return null;
    }

    /** Thêm một dòng chi tiết vào giỏ. Nếu món đã có thì tăng số lượng. */
    public boolean themChiTiet(ChiTietHoaDonDTO chiTiet) {
        if (chiTiet == null || chiTiet.getDoUong() == null || chiTiet.getSoLuong() <= 0) {
            return false;
        }

        ChiTietHoaDonDTO dongDaCo = timTrongGio(chiTiet.getMaMon());
        if (dongDaCo != null) {
            dongDaCo.setSoLuong(dongDaCo.getSoLuong() + chiTiet.getSoLuong());
        } else {
            dsChiTiet.add(chiTiet);
        }
        return true;
    }

    /** Giảm số lượng một món. Nếu chỉ còn 1 thì xóa khỏi giỏ. */
    public boolean giamChiTiet(String maMon) {
        ChiTietHoaDonDTO dong = timTrongGio(maMon);
        if (dong == null) {
            return false;
        }
        if (dong.getSoLuong() > 1) {
            dong.setSoLuong(dong.getSoLuong() - 1);
        } else {
            dsChiTiet.remove(dong);
        }
        return true;
    }

    /** Cập nhật số lượng của một món. Số lượng <= 0 thì xóa khỏi giỏ. */
    public boolean capNhatSoLuong(String maMon, int soLuong) {
        ChiTietHoaDonDTO dong = timTrongGio(maMon);
        if (dong == null) {
            return false;
        }
        if (soLuong <= 0) {
            dsChiTiet.remove(dong);
        } else {
            dong.setSoLuong(soLuong);
        }
        return true;
    }

    /** Xóa một món khỏi giỏ. */
    public boolean xoaChiTiet(String maMon) {
        ChiTietHoaDonDTO dong = timTrongGio(maMon);
        if (dong == null) {
            return false;
        }
        dsChiTiet.remove(dong);
        return true;
    }

    /** Xóa toàn bộ giỏ hàng, không ảnh hưởng chi tiết của hóa đơn đã lưu. */
    public void xoaGioHang() {
        for (int i = dsChiTiet.size() - 1; i >= 0; i--) {
            if (laTrongGio(dsChiTiet.get(i))) {
                dsChiTiet.remove(i);
            }
        }
    }

    /** Tổng tiền của giỏ hàng hiện tại. */
    public double tongTienGioHang() {
        double tong = 0;
        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (laTrongGio(chiTiet)) {
                tong = tong + chiTiet.getThanhTien();
            }
        }
        return tong;
    }

    /** Tổng số lượng món trong giỏ. */
    public int tongSoLuongGioHang() {
        int tong = 0;
        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (laTrongGio(chiTiet)) {
                tong = tong + chiTiet.getSoLuong();
            }
        }
        return tong;
    }

    public boolean gioHangRong() {
        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (laTrongGio(chiTiet)) {
                return false;
            }
        }
        return true;
    }

    // ==================== KHU VỰC CHI TIẾT HÓA ĐƠN ====================
    /** Trả về bản sao chi tiết của một hóa đơn đã lưu. */
    public List<ChiTietHoaDonDTO> layTheoHoaDon(String maHD) {
        List<ChiTietHoaDonDTO> ketQua = new ArrayList<>();
        String ma = chuanHoa(maHD);
        if (ma.isEmpty()) {
            return ketQua;
        }

        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (layMaHD(chiTiet).equalsIgnoreCase(ma)) {
                ketQua.add(chiTiet);
            }
        }
        return ketQua;
    }

    /** Tổng tiền của một hóa đơn đã lưu. */
    public double tongTienTheoHoaDon(String maHD) {
        double tong = 0;
        List<ChiTietHoaDonDTO> ds = layTheoHoaDon(maHD);
        for (ChiTietHoaDonDTO chiTiet : ds) {
            tong = tong + chiTiet.getThanhTien();
        }
        return tong;
    }

    /** Tổng số lượng món thuộc các hóa đơn được truyền vào. */
    public int tongSoLuongTheoHoaDon(List<String> dsMaHD) {
        int tong = 0;
        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (thuocHoaDon(chiTiet, dsMaHD)) {
                tong = tong + chiTiet.getSoLuong();
            }
        }
        return tong;
    }

    /**
     * Tổng hợp số lượng bán ra của từng món trong các hóa đơn được truyền vào.
     * Khóa của kết quả là tên món, giá trị là tổng số lượng.
     */
    public Map<String, Integer> thongKeMonBanChay(List<String> dsMaHD) {
        Map<String, Integer> ketQua = new LinkedHashMap<>();

        for (ChiTietHoaDonDTO chiTiet : dsChiTiet) {
            if (!thuocHoaDon(chiTiet, dsMaHD)) {
                continue;
            }

            String tenMon = chuanHoa(chiTiet.getTenMon());
            if (tenMon.isEmpty()) {
                tenMon = chuanHoa(chiTiet.getMaMon());
            }
            Integer soLuongDaCo = ketQua.get(tenMon);
            if (soLuongDaCo == null) {
                ketQua.put(tenMon, chiTiet.getSoLuong());
            } else {
                ketQua.put(tenMon, soLuongDaCo + chiTiet.getSoLuong());
            }
        }
        return ketQua;
    }

    /** Gán hóa đơn cho các dòng chi tiết khi lưu hóa đơn. */
    public void ganHoaDon(HoaDonDTO hoaDon, List<ChiTietHoaDonDTO> dsChiTietMoi) {
        if (hoaDon == null || dsChiTietMoi == null) {
            return;
        }
        for (ChiTietHoaDonDTO chiTiet : dsChiTietMoi) {
            chiTiet.setHoaDon(hoaDon);
        }
    }

    // ==================== KHU VỰC HỖ TRỢ ====================
    /** Một dòng thuộc giỏ hàng khi chưa được gán mã hóa đơn. */
    private boolean laTrongGio(ChiTietHoaDonDTO chiTiet) {
        return chiTiet != null && layMaHD(chiTiet).isEmpty();
    }

    private boolean thuocHoaDon(ChiTietHoaDonDTO chiTiet, List<String> dsMaHD) {
        if (chiTiet == null || dsMaHD == null) {
            return false;
        }
        String maHD = layMaHD(chiTiet);
        if (maHD.isEmpty()) {
            return false;
        }
        for (String ma : dsMaHD) {
            if (chuanHoa(ma).equalsIgnoreCase(maHD)) {
                return true;
            }
        }
        return false;
    }

    private String layMaHD(ChiTietHoaDonDTO chiTiet) {
        if (chiTiet == null || chiTiet.getHoaDon() == null) {
            return "";
        }
        return chuanHoa(chiTiet.getHoaDon().getMaHD());
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
