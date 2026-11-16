package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;

public class DSChiTietHoaDonBus {
    private List<ChiTietHoaDonDTO> chiTietHoaDonBus;
    private DSDoUongBUS dsDoUongBUS;

    public List<ChiTietHoaDonDTO> getChiTietHoaDonBus() {
        return new ArrayList<>(chiTietHoaDonBus);
    }

    public void setChiTietHoaDonBus(List<ChiTietHoaDonDTO> chiTietHoaDonBus) {
        this.chiTietHoaDonBus = chiTietHoaDonBus == null
                ? new ArrayList<>() : new ArrayList<>(chiTietHoaDonBus);
    }

    public DSChiTietHoaDonBus() {
        chiTietHoaDonBus = new ArrayList<>();
    }

    public DSChiTietHoaDonBus(DSDoUongBUS dsDoUongBUS) {
        this();
        this.dsDoUongBUS = dsDoUongBUS;
    }

    public List<ChiTietHoaDonDTO> layGioHang() {
        List<ChiTietHoaDonDTO> ketQua = new ArrayList<>();
        for (ChiTietHoaDonDTO ct : chiTietHoaDonBus) {
            if (ct != null && ct.getHoaDon() == null) ketQua.add(ct);
        }
        return ketQua;
    }

    public ChiTietHoaDonDTO timTrongGio(String maMon) {
        for (ChiTietHoaDonDTO ct : layGioHang()) {
            if (cungMa(ct.getMaMon(), maMon)) return ct;
        }
        return null;
    }

    public boolean themChiTiet(ChiTietHoaDonDTO chiTiet) {
        if (chiTiet == null || chiTiet.getSoLuong() <= 0 || chiTiet.getDoUong() == null || chiTiet.getHoaDon() != null
                || (dsDoUongBUS != null && dsDoUongBUS.timTheoMa(chiTiet.getMaMon()) == null)) {
            return false;
        }
        ChiTietHoaDonDTO cu = timTrongGio(chiTiet.getMaMon());
        if (cu != null) {
            if (cu == chiTiet || chiTiet.getSoLuong() > Integer.MAX_VALUE - cu.getSoLuong()) return false;
            int soLuong = cu.getSoLuong() + chiTiet.getSoLuong();
            if (!Double.isFinite(cu.getDonGia() * soLuong)) return false;
            cu.setSoLuong(soLuong);
        } else {
            chiTietHoaDonBus.add(chiTiet);
        }
        return true;
    }

    public boolean capNhatSoLuong(String maMon, int soLuong) {
        ChiTietHoaDonDTO ct = timTrongGio(maMon);
        if (ct == null || soLuong < 0 || !Double.isFinite(ct.getDonGia() * soLuong)) return false;
        if (soLuong == 0) return xoaChiTiet(maMon);
        ct.setSoLuong(soLuong);
        return true;
    }

    public boolean giamChiTiet(String maMon) {
        ChiTietHoaDonDTO ct = timTrongGio(maMon);
        return ct != null && capNhatSoLuong(maMon, ct.getSoLuong() - 1);
    }

    public boolean xoaChiTiet(String maMon) {
        boolean daXoa = false;
        for (int i = chiTietHoaDonBus.size() - 1; i >= 0; i--) {
            ChiTietHoaDonDTO ct = chiTietHoaDonBus.get(i);
            if (ct != null && ct.getHoaDon() == null && cungMa(ct.getMaMon(), maMon)) {
                chiTietHoaDonBus.remove(i);
                daXoa = true;
            }
        }
        return daXoa;
    }

    public boolean gioHangRong() {
        return layGioHang().isEmpty();
    }

    public double tongTienGioHang() {
        double tong = 0;
        for (ChiTietHoaDonDTO ct : layGioHang()) tong += ct.getThanhTien();
        return tong;
    }

    public List<ChiTietHoaDonDTO> layTheoHoaDon(String maHD) {
        List<ChiTietHoaDonDTO> ketQua = new ArrayList<>();
        for (ChiTietHoaDonDTO ct : chiTietHoaDonBus) {
            if (ct != null && cungMa(ct.getMaHD(), maHD)) ketQua.add(ct);
        }
        return ketQua;
    }

    public double tongTienTheoHoaDon(String maHD) {
        double tong = 0;
        for (ChiTietHoaDonDTO ct : layTheoHoaDon(maHD)) tong += ct.getThanhTien();
        return tong;
    }

    public boolean ganHoaDon(HoaDonDTO hoaDon, List<ChiTietHoaDonDTO> dsChiTiet) {
        if (hoaDon == null || hoaDon.getMaHD() == null || hoaDon.getMaHD().trim().isEmpty()
                || dsChiTiet == null || dsChiTiet.isEmpty() || !layTheoHoaDon(hoaDon.getMaHD()).isEmpty()) return false;
        List<ChiTietHoaDonDTO> gioHang = layGioHang();
        if (gioHang.size() != dsChiTiet.size()) return false;
        List<String> dsMa = new ArrayList<>();
        for (ChiTietHoaDonDTO ct : dsChiTiet) {
            if (ct == null || ct.getSoLuong() <= 0 || ct.getHoaDon() != null || !gioHang.contains(ct)
                    || (dsDoUongBUS != null && dsDoUongBUS.timTheoMa(ct.getMaMon()) == null)) return false;
            String ma = ct.getMaMon().trim().toLowerCase(java.util.Locale.ROOT);
            if (dsMa.contains(ma)) return false;
            dsMa.add(ma);
        }
        for (ChiTietHoaDonDTO ct : dsChiTiet) ct.setHoaDon(hoaDon);
        return true;
    }

    public boolean napDuLieuTuCSDL(List<ChiTietHoaDonDTO> dsTuDB, DSHoaDonBUS dsHoaDonBUS,
            DSDoUongBUS dsDoUongBUS) {
        if (dsTuDB == null || dsHoaDonBUS == null || dsDoUongBUS == null) return false;
        this.dsDoUongBUS = dsDoUongBUS;
        boolean daNap = false;
        for (ChiTietHoaDonDTO ct : dsTuDB) {
            if (ct == null || ct.getSoLuong() <= 0) continue;
            HoaDonDTO hd = dsHoaDonBUS.timTheoMa(ct.getMaHD());
            if (hd == null || dsDoUongBUS.timTheoMa(ct.getMaMon()) == null) continue;
            boolean trung = false;
            for (ChiTietHoaDonDTO cu : layTheoHoaDon(hd.getMaHD())) {
                if (cungMa(cu.getMaMon(), ct.getMaMon())) { trung = true; break; }
            }
            if (trung) continue;
            ct.setHoaDon(hd);
            ct.setDoUong(dsDoUongBUS.timTheoMa(ct.getMaMon()));
            chiTietHoaDonBus.add(ct);
            daNap = true;
        }
        return daNap;
    }

    public int tongSoLuongTheoHoaDon(List<String> dsMaHD) {
        int tong = 0;
        for (ChiTietHoaDonDTO ct : thongKeMonTheoMa(dsMaHD)) tong += ct.getSoLuong();
        return tong;
    }

    public List<ChiTietHoaDonDTO> thongKeMonTheoMa(List<String> dsMaHD) {
        Map<String, ChiTietHoaDonDTO> thongKe = new LinkedHashMap<>();
        if (dsMaHD != null) for (String maHD : dsMaHD) {
            for (ChiTietHoaDonDTO ct : layTheoHoaDon(maHD)) {
                String ma = ct.getMaMon().trim().toLowerCase(java.util.Locale.ROOT);
                ChiTietHoaDonDTO tong = thongKe.get(ma);
                if (tong == null) {
                    tong = new ChiTietHoaDonDTO(ct.getDoUong(), ct.getSoLuong());
                    thongKe.put(ma, tong);
                } else tong.setSoLuong(tong.getSoLuong() + ct.getSoLuong());
            }
        }
        return new ArrayList<>(thongKe.values());
    }

    public Map<String, Integer> thongKeMonBanChay(List<String> dsMaHD) {
        Map<String, Integer> ketQua = new LinkedHashMap<>();
        for (ChiTietHoaDonDTO ct : thongKeMonTheoMa(dsMaHD)) {
            String ten = ct.getTenMon();
            Integer hienCo = ketQua.get(ten);
            if (hienCo == null) {
                ketQua.put(ten, ct.getSoLuong());
            } else {
                ketQua.put(ten, hienCo + ct.getSoLuong());
            }
        }
        return ketQua;
    }

    private static boolean cungMa(String a, String b) {
        return a != null && b != null && !a.trim().isEmpty() && a.trim().equalsIgnoreCase(b.trim());
    }
}
