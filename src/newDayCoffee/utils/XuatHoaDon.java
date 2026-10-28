package newDayCoffee.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;


public class XuatHoaDon {
    private static final int RONG = 45; // số ký tự mỗi dòng
    private static final int TEN_MON_RONG = 18;

    public static String taoNoiDung(HoaDonDTO hd, List<ChiTietHoaDonDTO> dsChiTiet, String tenNV) {
        if (hd == null) {
            return "";
        }
        if (dsChiTiet == null) {
            dsChiTiet = new ArrayList<>();
        }
        String xuongDong = System.lineSeparator();
        String gach = "-".repeat(RONG);
        StringBuilder sb = new StringBuilder();

        sb.append(canGiua("NEW DAY COFFEE", RONG)).append(xuongDong);
        sb.append(canGiua("HÓA ĐƠN BÁN HÀNG", RONG)).append(xuongDong);
        sb.append(gach).append(xuongDong);
        sb.append("Mã hóa đơn : ").append(hd.getMaHD()).append(xuongDong);
        sb.append("Ngày lập   : ").append(hd.getNgayLap()).append(xuongDong);
        sb.append("Nhân viên  : ").append(tenNV != null ? tenNV : hd.getMaNV()).append(xuongDong);
        sb.append("Thanh toán : ").append(hd.getPhuongThucTT()).append(xuongDong);
        if (hd.getGhiChu() != null && !hd.getGhiChu().trim().isEmpty()) {
            sb.append("Ghi chú    : ").append(hd.getGhiChu().trim()).append(xuongDong);
        }
        sb.append(gach).append(xuongDong);
        sb.append(String.format("%-" + TEN_MON_RONG + "s %3s %10s %11s", "Tên món", "SL", "Đơn giá", "Thành tiền"))
                .append(xuongDong);

        for (ChiTietHoaDonDTO ct : dsChiTiet) {
            String ten = ct.getTenMon();
            if (ten.length() > TEN_MON_RONG) {
                ten = ten.substring(0, TEN_MON_RONG);
            }
            sb.append(String.format("%-" + TEN_MON_RONG + "s %3d %10s %11s",
                    ten, ct.getSoLuong(), tien(ct.getDonGia()), tien(ct.getThanhTien())))
                    .append(xuongDong);
        }

        sb.append(gach).append(xuongDong);
        sb.append(dongTong("Tổng tiền:", hd.getTongTien())).append(xuongDong);
        sb.append(dongTong("Tiền khách đưa:", hd.getTienKhachDua())).append(xuongDong);
        sb.append(dongTong("Tiền thối:", hd.getTienThoi())).append(xuongDong);
        sb.append(gach).append(xuongDong);
        sb.append(canGiua("Cảm ơn quý khách!", RONG)).append(xuongDong);
        return sb.toString();
    }

    public static boolean xuatFile(HoaDonDTO hd, List<ChiTietHoaDonDTO> dsChiTiet, String tenNV, String duongDanFile) {
        if (hd == null || duongDanFile == null) {
            return false;
        }
        try {
            Path path = Path.of(duongDanFile);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, taoNoiDung(hd, dsChiTiet, tenNV), StandardCharsets.UTF_8);
            return true;
        } catch (IOException | InvalidPathException e) {
            return false;
        }
    }


    private static String dongTong(String nhan, double soTien) {
        return String.format("%-20s %24s", nhan, tien(soTien));
    }

    private static String tien(double soTien) {
        return String.format(Locale.US, "%,.0f", soTien).replace(',', '.') + "Đ";
    }

    private static String canGiua(String s, int rong) {
        int le = Math.max(0, (rong - s.length()) / 2);
        return " ".repeat(le) + s;
    }
}