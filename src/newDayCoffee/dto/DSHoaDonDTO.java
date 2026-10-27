package newDayCoffee.dto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DSHoaDonDTO {
    private List<HoaDonDTO> dsHoaDon = new ArrayList<>();

    public DSHoaDonDTO() {
    }

    public DSHoaDonDTO(List<HoaDonDTO> dsHoaDon) {
        if (dsHoaDon != null) {
            this.dsHoaDon = dsHoaDon;
        }
    }

    public boolean themHoaDon(HoaDonDTO hd) {
        if (hd == null || hd.getMaHD() == null) {
            return false;
        }
        for (HoaDonDTO h : dsHoaDon) {
            if (h.getMaHD().equalsIgnoreCase(hd.getMaHD())) {
                return false;
            }
        }
        dsHoaDon.add(hd);
        return true;
    }

    public double tinhDoanhThuHomNay() {
        LocalDate homNay = LocalDate.now();
        double tong = 0;
        for (HoaDonDTO hd : dsHoaDon) {
            if (laHoanTat(hd) && homNay.equals(docNgay(hd.getNgayLap()))) {
                tong += hd.getTongTien();
            }
        }
        return tong;
    }

    public int demSoDonHoanTat() {
        int dem = 0;
        for (HoaDonDTO hd : dsHoaDon) {
            if (laHoanTat(hd)) {
                dem++;
            }
        }
        return dem;
    }


    public List<DoUongDTO> timMonBanChayNhat() {
        Map<String, Integer> tongSoLuong = new LinkedHashMap<>();
        Map<String, DoUongDTO> thongTinMon = new LinkedHashMap<>();

        for (HoaDonDTO hd : dsHoaDon) {
            if (!laHoanTat(hd)) {
                continue;
            }
            for (ChiTietHoaDonDTO ct : hd.getDsChiTiet()) {
                String ma = ct.getMaMon();
                tongSoLuong.merge(ma, ct.getSoLuong(), Integer::sum);
                thongTinMon.putIfAbsent(ma, new DoUongDTO(ma, ct.getTenMon(), ct.getDonGia()));
            }
        }

        int max = 0;
        for (int sl : tongSoLuong.values()) {
            if (sl > max) {
                max = sl;
            }
        }

        List<DoUongDTO> kq = new ArrayList<>();
        if (max == 0) {
            return kq;
        }
        for (Map.Entry<String, Integer> e : tongSoLuong.entrySet()) {
            if (e.getValue() == max) {
                kq.add(thongTinMon.get(e.getKey()));
            }
        }
        return kq;
    }

    public List<HoaDonDTO> getDsHoaDon() {
        return dsHoaDon;
    }

    public boolean luuVaoCSDL() {
        return true;
    }


    private boolean laHoanTat(HoaDonDTO hd) {
        return hd.getDsChiTiet() != null
                && !hd.getDsChiTiet().isEmpty()
                && hd.getTienKhachDua() >= hd.getTongTien();
    }


    private LocalDate docNgay(String s) {
        if (s == null) {
            return null;
        }
        String phanNgay = s.trim().split("[ T]")[0];
        try {
            return LocalDate.parse(phanNgay);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}