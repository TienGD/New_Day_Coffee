package newDayCoffee.bus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import newDayCoffee.dao.ChiTietHoaDonDAO;
import newDayCoffee.dao.HoaDonDAO;
import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.dto.NhanVienDTO;

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

    /** Tính thành tiền của một dòng chi tiết hóa đơn thông qua ChiTietHoaDonBUS. */
    public double tinhThanhTienChiTiet(ChiTietHoaDonDTO chiTiet) {
        if (chiTiet == null || chiTietHoaDonBUS == null) {
            return 0;
        }
        return chiTietHoaDonBUS.tinhThanhTien(chiTiet);
    }

    /** Kiểm tra nhân viên đã từng lập hóa đơn nào trong danh sách hay chưa. */
    public boolean coHoaDonCuaNhanVien(String maNV) {
        String ma = chuanHoa(maNV);
        if (ma.isEmpty()) {
            return false;
        }
        for (HoaDonDTO hoaDon : dsHoaDon) {
            if (hoaDon != null && hoaDon.getMaNV() != null
                    && chuanHoa(hoaDon.getMaNV().getMaNV()).equalsIgnoreCase(ma)) {
                return true;
            }
        }
        return false;
    }

    // ==================== KHU VỰC THÊM HÓA ĐƠN ====================
    /** Nạp hóa đơn và chi tiết hóa đơn từ CSDL thông qua DAO khi bộ nhớ đang trống. */
    public boolean napDuLieuTuCSDL(DSNhanVienBUS dsNhanVienBUS, DSDoUongBUS dsDoUongBUS) {
        if (!dsHoaDon.isEmpty()) {
            return false;
        }
        try {
            List<HoaDonDTO> dsTuDB = new HoaDonDAO().getAll();
            if (dsTuDB != null && !dsTuDB.isEmpty() && napDuLieuTuCSDL(dsTuDB, dsNhanVienBUS)) {
                if (chiTietHoaDonBUS != null) {
                    chiTietHoaDonBUS.napDuLieuTuCSDL(this, dsDoUongBUS);
                }
                return true;
            }
        } catch (Exception e) {
            // Bắt ngoại lệ an toàn nếu cơ sở dữ liệu chưa sẵn sàng
        }
        return false;
    }

    /** Nạp danh sách hóa đơn đọc từ CSDL khi bộ nhớ đang trống. */
    public boolean napDuLieuTuCSDL(List<HoaDonDTO> dsTuDB, DSNhanVienBUS dsNhanVienBUS) {
        if (!dsHoaDon.isEmpty() || dsTuDB == null || dsTuDB.isEmpty()) {
            return false;
        }
        for (HoaDonDTO hoaDon : dsTuDB) {
            if (hoaDon == null || chuanHoa(hoaDon.getMaHD()).isEmpty()
                    || timTheoMa(hoaDon.getMaHD()) != null) {
                continue;
            }
            if (dsNhanVienBUS != null && hoaDon.getMaNV() != null) {
                NhanVienDTO nvBus = dsNhanVienBUS.timTheoMa(hoaDon.getMaNV().getMaNV());
                if (nvBus != null) {
                    hoaDon.setMaNV(nvBus);
                }
            }
            dsHoaDon.add(hoaDon);
        }
        return !dsHoaDon.isEmpty();
    }

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

        HoaDonDAO hoaDonDAO = new HoaDonDAO();
        if (hoaDonDAO.insert(hoaDon)) {
            ChiTietHoaDonDAO chiTietDAO = new ChiTietHoaDonDAO();
            for (ChiTietHoaDonDTO ct : dsChiTietMoi) {
                chiTietDAO.insert(ct);
            }
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

    /** Danh sách thống kê số lượng từng món bán được trong ngày hôm nay (kèm mã món). */
    public List<ChiTietHoaDonDTO> layDanhSachThongKeMonHomNay() {
        return chiTietHoaDonBUS.thongKeMonTheoMa(layMaHoaDonHomNay());
    }

    /** Danh sách các hóa đơn được lập trong ngày hôm nay. */
    public List<HoaDonDTO> layDanhSachHoaDonHomNay() {
        List<HoaDonDTO> ketQua = new ArrayList<>();
        for (HoaDonDTO hoaDon : dsHoaDon) {
            if (laHomNay(hoaDon)) {
                ketQua.add(hoaDon);
            }
        }
        return ketQua;
    }

    /** Sắp xếp danh sách thống kê món theo số lượng tăng hoặc giảm dần. */
    public List<ChiTietHoaDonDTO> sapXepThongKeMon(List<ChiTietHoaDonDTO> nguon, final boolean giamDan) {
        List<ChiTietHoaDonDTO> banSao = nguon == null ? new ArrayList<ChiTietHoaDonDTO>() : new ArrayList<ChiTietHoaDonDTO>(nguon);
        Collections.sort(banSao, new Comparator<ChiTietHoaDonDTO>() {
            @Override
            public int compare(ChiTietHoaDonDTO a, ChiTietHoaDonDTO b) {
                int cmp = giamDan
                        ? Integer.compare(b.getSoLuong(), a.getSoLuong())
                        : Integer.compare(a.getSoLuong(), b.getSoLuong());
                if (cmp != 0) {
                    return cmp;
                }
                String maA = a.getMaMon() == null ? "" : a.getMaMon();
                String maB = b.getMaMon() == null ? "" : b.getMaMon();
                cmp = maA.compareToIgnoreCase(maB);
                if (cmp != 0) {
                    return cmp;
                }
                cmp = maA.compareTo(maB);
                if (cmp != 0) {
                    return cmp;
                }
                String tenA = a.getTenMon() == null ? "" : a.getTenMon();
                String tenB = b.getTenMon() == null ? "" : b.getTenMon();
                return tenA.compareToIgnoreCase(tenB);
            }
        });
        return banSao;
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
