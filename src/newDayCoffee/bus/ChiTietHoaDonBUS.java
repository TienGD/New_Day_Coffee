package newDayCoffee.bus;

import java.util.List;
import java.util.Map;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.dto.HoaDonDTO;

/**
 * Xử lý nghiệp vụ trên một đối tượng chi tiết hóa đơn (Nhóm A: tinhThanhTien),
 * đồng thời ủy quyền các thao tác danh sách cho DSChiTietHoaDonBus (Nhóm B).
 *
 * Bảo đảm toàn hệ thống chỉ giữ một danh sách chi tiết hóa đơn duy nhất tại DSChiTietHoaDonBus.
 */
public class ChiTietHoaDonBUS {

    // ==================== KHU VỰC DỮ LIỆU ĐỐI TƯỢNG ĐƠN (NHÓM A) ====================
    private ChiTietHoaDonDTO chiTietHoaDon;
    private DoUongDTO doUong;

    // ==================== KHU VỰC ỦY QUYỀN DANH SÁCH (NHÓM B) ====================
    private final DSChiTietHoaDonBus dsChiTietHoaDonBus;

    public ChiTietHoaDonBUS() {
        this(new DSChiTietHoaDonBus());
    }

    public ChiTietHoaDonBUS(DSChiTietHoaDonBus dsChiTietHoaDonBus) {
        this.dsChiTietHoaDonBus = dsChiTietHoaDonBus != null ? dsChiTietHoaDonBus : new DSChiTietHoaDonBus();
    }

    public ChiTietHoaDonBUS(ChiTietHoaDonDTO chiTietHoaDon, DoUongDTO doUong) {
        this.dsChiTietHoaDonBus = null;
        this.chiTietHoaDon = chiTietHoaDon;
        this.doUong = doUong;
    }

    public DSChiTietHoaDonBus getDsChiTietHoaDonBus() {
        return dsChiTietHoaDonBus;
    }

    private DSChiTietHoaDonBus layQuanLyDanhSach() {
        return dsChiTietHoaDonBus != null ? dsChiTietHoaDonBus : new DSChiTietHoaDonBus();
    }

    public ChiTietHoaDonDTO getChiTietHoaDon() {
        return chiTietHoaDon;
    }

    public void setChiTietHoaDon(ChiTietHoaDonDTO chiTietHoaDon) {
        this.chiTietHoaDon = chiTietHoaDon;
    }

    public DoUongDTO getDoUong() {
        return doUong;
    }

    public void setDoUong(DoUongDTO doUong) {
        this.doUong = doUong;
    }

    /** Tính thành tiền cho chi tiết hóa đơn hiện tại: Số lượng x Đơn giá */
    public double tinhThanhTien() {
        if (chiTietHoaDon == null || chiTietHoaDon.getSoLuong() <= 0) {
            return 0;
        }
        DoUongDTO mon = (doUong != null) ? doUong : chiTietHoaDon.getDoUong();
        if (mon != null) {
            return chiTietHoaDon.getSoLuong() * mon.getDonGia();
        }
        return chiTietHoaDon.getSoLuong() * chiTietHoaDon.getDonGia();
    }

    /** Tính thành tiền cho một dòng chi tiết hóa đơn truyền vào. */
    public double tinhThanhTien(ChiTietHoaDonDTO chiTiet) {
        if (chiTiet == null) {
            return 0;
        }
        return new ChiTietHoaDonBUS(chiTiet, chiTiet.getDoUong()).tinhThanhTien();
    }

    // ==================== KHU VỰC ỦY QUYỀN GIỎ HÀNG SANG DSChiTietHoaDonBus ====================
    public List<ChiTietHoaDonDTO> layGioHang() {
        return layQuanLyDanhSach().layGioHang();
    }

    public ChiTietHoaDonDTO timTrongGio(String maMon) {
        return layQuanLyDanhSach().timTrongGio(maMon);
    }

    public boolean themChiTiet(ChiTietHoaDonDTO chiTiet) {
        return layQuanLyDanhSach().themChiTiet(chiTiet);
    }

    public boolean giamChiTiet(String maMon) {
        return layQuanLyDanhSach().giamChiTiet(maMon);
    }

    public boolean capNhatSoLuong(String maMon, int soLuong) {
        return layQuanLyDanhSach().capNhatSoLuong(maMon, soLuong);
    }

    public boolean xoaChiTiet(String maMon) {
        return layQuanLyDanhSach().xoaChiTiet(maMon);
    }

    public void xoaGioHang() {
        layQuanLyDanhSach().xoaGioHang();
    }

    public double tongTienGioHang() {
        return layQuanLyDanhSach().tongTienGioHang();
    }

    public int tongSoLuongGioHang() {
        return layQuanLyDanhSach().tongSoLuongGioHang();
    }

    public boolean gioHangRong() {
        return layQuanLyDanhSach().gioHangRong();
    }

    // ==================== KHU VỰC ỦY QUYỀN CHI TIẾT HÓA ĐƠN SANG DSChiTietHoaDonBus ====================
    public List<ChiTietHoaDonDTO> layTheoHoaDon(String maHD) {
        return layQuanLyDanhSach().layTheoHoaDon(maHD);
    }

    public double tongTienTheoHoaDon(String maHD) {
        return layQuanLyDanhSach().tongTienTheoHoaDon(maHD);
    }

    public boolean coDoUongTrongChiTiet(String maMon) {
        return layQuanLyDanhSach().coDoUongTrongChiTiet(maMon);
    }

    public int tongSoLuongTheoHoaDon(List<String> dsMaHD) {
        return layQuanLyDanhSach().tongSoLuongTheoHoaDon(dsMaHD);
    }

    public Map<String, Integer> thongKeMonBanChay(List<String> dsMaHD) {
        return layQuanLyDanhSach().thongKeMonBanChay(dsMaHD);
    }

    public List<ChiTietHoaDonDTO> thongKeMonTheoMa(List<String> dsMaHD) {
        return layQuanLyDanhSach().thongKeMonTheoMa(dsMaHD);
    }

    public void ganHoaDon(HoaDonDTO hoaDon, List<ChiTietHoaDonDTO> dsChiTietMoi) {
        if (hoaDon == null || dsChiTietMoi == null) {
            return;
        }
        if (!layQuanLyDanhSach().ganHoaDon(hoaDon, dsChiTietMoi)) {
            for (ChiTietHoaDonDTO chiTiet : dsChiTietMoi) {
                if (chiTiet != null) {
                    chiTiet.setHoaDon(hoaDon);
                }
            }
        }
    }

    public int napDuLieuTuCSDL(DSHoaDonBUS dsHoaDonBUS, DSDoUongBUS dsDoUongBUS) {
        return layQuanLyDanhSach().napDuLieuTuCSDL(dsHoaDonBUS, dsDoUongBUS);
    }

    public int napDuLieuTuCSDL(List<ChiTietHoaDonDTO> dsTuDB,
            DSHoaDonBUS dsHoaDonBUS, DSDoUongBUS dsDoUongBUS) {
        return layQuanLyDanhSach().napDuLieuTuCSDLCount(dsTuDB, dsHoaDonBUS, dsDoUongBUS);
    }
}
