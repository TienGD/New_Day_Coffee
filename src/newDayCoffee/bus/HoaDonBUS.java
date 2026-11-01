package newDayCoffee.bus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.ChiTietHoaDonDTO;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.dto.HoaDonDTO;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.Validator;

/**
 * Xử lý nghiệp vụ của một phiên bán hàng (giỏ hàng và thanh toán).
 *
 * Lớp này nhận DSHoaDonBUS để lưu hóa đơn và NhanVienBUS để lấy thông tin
 * nhân viên đang làm việc. Mọi kiểm tra dữ liệu đều đi qua Validator,
 * phiên đăng nhập đi qua Auth.
 */
public class HoaDonBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final DSHoaDonBUS dsHoaDonBUS;
    private final NhanVienBUS nhanVienBUS;
    private final List<ChiTietHoaDonDTO> gioHang;
    private HoaDonDTO hoaDonVuaTao;

    private static final DateTimeFormatter DINH_DANG_THOI_GIAN =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * Nhận các BUS cần thiết từ nơi tạo (MainFrame) để dùng chung dữ liệu,
     * không dùng singleton.
     */
    public HoaDonBUS(DSHoaDonBUS dsHoaDonBUS, NhanVienBUS nhanVienBUS) {
        this.dsHoaDonBUS = dsHoaDonBUS;
        this.nhanVienBUS = nhanVienBUS;
        this.gioHang = new ArrayList<>();
        this.hoaDonVuaTao = null;
    }

    // ==================== KHU VỰC GIỎ HÀNG ====================
    /** Trả về bản sao giỏ hàng để giao diện hiển thị. */
    public List<ChiTietHoaDonDTO> layGioHang() {
        return new ArrayList<>(gioHang);
    }

    /** Tìm một dòng trong giỏ hàng theo mã món. */
    public ChiTietHoaDonDTO timTrongGio(String maMon) {
        String ma = Validator.chuanHoa(maMon);
        for (int i = 0; i < gioHang.size(); i++) {
            if (gioHang.get(i).getMaMon().equalsIgnoreCase(ma)) {
                return gioHang.get(i);
            }
        }
        return null;
    }

    /**
     * Thêm một món vào giỏ. Nếu món đã có thì tăng số lượng thêm 1.
     *
     * @return true nếu thêm được.
     */
    public boolean themMon(DoUongDTO doUong) {
        if (doUong == null) {
            return false;
        }
        return themMon(doUong, 1);
    }

    /** Thêm một món với số lượng chỉ định. */
    public boolean themMon(DoUongDTO doUong, int soLuong) {
        if (doUong == null) {
            return false;
        }
        if (Validator.kiemTraSoLuong(soLuong) != null) {
            return false;
        }

        ChiTietHoaDonDTO dongDaCo = timTrongGio(doUong.getMaMon());
        if (dongDaCo != null) {
            dongDaCo.setSoLuong(dongDaCo.getSoLuong() + soLuong);
        } else {
            gioHang.add(new ChiTietHoaDonDTO("", doUong.getMaMon(), doUong.getTenMon(),
                    soLuong, doUong.getDonGia()));
        }
        return true;
    }

    /**
     * Giảm số lượng một món. Nếu chỉ còn 1 thì xóa khỏi giỏ.
     *
     * @return true nếu tìm thấy món trong giỏ.
     */
    public boolean giamMon(String maMon) {
        ChiTietHoaDonDTO dong = timTrongGio(maMon);
        if (dong == null) {
            return false;
        }
        if (dong.getSoLuong() > 1) {
            dong.setSoLuong(dong.getSoLuong() - 1);
        } else {
            gioHang.remove(dong);
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
            gioHang.remove(dong);
        } else {
            dong.setSoLuong(soLuong);
        }
        return true;
    }

    /** Xóa một món khỏi giỏ. */
    public boolean xoaMon(String maMon) {
        ChiTietHoaDonDTO dong = timTrongGio(maMon);
        if (dong == null) {
            return false;
        }
        gioHang.remove(dong);
        return true;
    }

    /** Xóa toàn bộ giỏ hàng. */
    public void xoaGioHang() {
        gioHang.clear();
    }

    /** Tổng tiền của giỏ hàng hiện tại. */
    public double tongTien() {
        double tong = 0;
        for (int i = 0; i < gioHang.size(); i++) {
            tong = tong + gioHang.get(i).getThanhTien();
        }
        return tong;
    }

    /** Tổng số lượng món trong giỏ. */
    public int tongSoLuong() {
        int tong = 0;
        for (int i = 0; i < gioHang.size(); i++) {
            tong = tong + gioHang.get(i).getSoLuong();
        }
        return tong;
    }

    public boolean gioHangRong() {
        return gioHang.isEmpty();
    }

    // ==================== KHU VỰC THANH TOÁN ====================
    /**
     * Kiểm tra thông tin thanh toán trước khi lập hóa đơn.
     *
     * @return null nếu hợp lệ, ngược lại là thông báo lỗi.
     */
    public String kiemTraThanhToan(String tienKhachDuaText, String phuongThucTT) {
        if (gioHangRong()) {
            return "Đơn hàng chưa có món nào để thanh toán.";
        }
        double tienKhachDua = Validator.docSoTien(tienKhachDuaText);
        if (tienKhachDua < 0) {
            return "Tiền khách đưa không hợp lệ.";
        }
        return Validator.kiemTraThanhToan(tongTien(), tienKhachDua, phuongThucTT);
    }

    /**
     * Lập hóa đơn cho giỏ hàng hiện tại và lưu vào DSHoaDonBUS.
     *
     * @return null nếu thành công, ngược lại là thông báo lỗi.
     */
    public String thanhToan(String tienKhachDuaText, String phuongThucTT, String ghiChu) {
        hoaDonVuaTao = null;

        String loi = kiemTraThanhToan(tienKhachDuaText, phuongThucTT);
        if (loi != null) {
            return loi;
        }
        if (!Auth.isDangNhap()) {
            return "Vui lòng đăng nhập trước khi thanh toán.";
        }

        double tienKhachDua = Validator.docSoTien(tienKhachDuaText);
        double tongTien = tongTien();

        HoaDonDTO hoaDon = new HoaDonDTO();
        hoaDon.setMaHD(dsHoaDonBUS.taoMaHoaDon());
        hoaDon.setNgayLap(LocalDateTime.now().format(DINH_DANG_THOI_GIAN));
        hoaDon.setMaNV(Auth.getNguoiDung().getMaNV());
        hoaDon.setTongTien(tongTien);
        hoaDon.setTienKhachDua(tienKhachDua);
        hoaDon.setTienThoi(tienKhachDua - tongTien);
        hoaDon.setPhuongThucTT(Validator.chuanHoa(phuongThucTT));
        hoaDon.setGhiChu(Validator.chuanHoa(ghiChu));

        // Sao chép giỏ hàng sang chi tiết hóa đơn để giỏ hàng có thể dùng tiếp
        List<ChiTietHoaDonDTO> dsChiTiet = new ArrayList<>();
        for (int i = 0; i < gioHang.size(); i++) {
            ChiTietHoaDonDTO dong = gioHang.get(i);
            dsChiTiet.add(new ChiTietHoaDonDTO(hoaDon.getMaHD(), dong.getMaMon(),
                    dong.getTenMon(), dong.getSoLuong(), dong.getDonGia()));
        }

        if (!dsHoaDonBUS.themHoaDon(hoaDon, dsChiTiet)) {
            return "Không thể lưu hóa đơn. Vui lòng thử lại.";
        }

        hoaDonVuaTao = hoaDon;
        xoaGioHang();
        return null;
    }

    /** Hóa đơn vừa thanh toán thành công, dùng để in phiếu. */
    public HoaDonDTO layHoaDonVuaTao() {
        return hoaDonVuaTao;
    }

    // ==================== KHU VỰC IN / XUẤT HÓA ĐƠN ====================
    /**
     * Tạo nội dung hóa đơn dạng văn bản để hiển thị hoặc ghi file.
     */
    public String taoNoiDungHoaDon(HoaDonDTO hoaDon) {
        if (hoaDon == null) {
            return "";
        }

        StringBuilder noiDung = new StringBuilder();
        noiDung.append("        NEW DAY COFFEE\n");
        noiDung.append("      HÓA ĐƠN THANH TOÁN\n");
        noiDung.append("------------------------------------------\n");
        noiDung.append("Mã hóa đơn : ").append(hoaDon.getMaHD()).append("\n");
        noiDung.append("Ngày lập   : ").append(hoaDon.getNgayLap()).append("\n");
        noiDung.append("Nhân viên  : ").append(tenNhanVien(hoaDon.getMaNV())).append("\n");
        noiDung.append("------------------------------------------\n");

        List<ChiTietHoaDonDTO> dsChiTiet = dsHoaDonBUS.layChiTiet(hoaDon.getMaHD());
        for (int i = 0; i < dsChiTiet.size(); i++) {
            ChiTietHoaDonDTO chiTiet = dsChiTiet.get(i);
            noiDung.append(chiTiet.getTenMon())
                    .append("  x").append(chiTiet.getSoLuong())
                    .append("  ").append(dinhDangTien(chiTiet.getDonGia()))
                    .append(" = ").append(dinhDangTien(chiTiet.getThanhTien()))
                    .append("\n");
        }

        noiDung.append("------------------------------------------\n");
        noiDung.append("Tổng tiền   : ").append(dinhDangTien(hoaDon.getTongTien())).append("\n");
        noiDung.append("Khách đưa   : ").append(dinhDangTien(hoaDon.getTienKhachDua())).append("\n");
        noiDung.append("Tiền thối   : ").append(dinhDangTien(hoaDon.getTienThoi())).append("\n");
        noiDung.append("Phương thức : ").append(hoaDon.getPhuongThucTT()).append("\n");

        if (!Validator.rong(hoaDon.getGhiChu())) {
            noiDung.append("Ghi chú     : ").append(hoaDon.getGhiChu()).append("\n");
        }

        noiDung.append("------------------------------------------\n");
        noiDung.append("Cảm ơn quý khách! Hẹn gặp lại.\n");
        return noiDung.toString();
    }

    /**
     * Xuất hóa đơn ra file văn bản UTF-8 trong thư mục "hoadon".
     *
     * @return đường dẫn file nếu ghi thành công, null nếu thất bại.
     */
    public String xuatHoaDonRaFile(HoaDonDTO hoaDon) {
        if (hoaDon == null) {
            return null;
        }

        try {
            Path thuMuc = Paths.get("hoadon");
            Files.createDirectories(thuMuc);
            Path file = thuMuc.resolve(hoaDon.getMaHD() + ".txt");
            Files.write(file, taoNoiDungHoaDon(hoaDon).getBytes(StandardCharsets.UTF_8));
            return file.toAbsolutePath().toString();
        } catch (IOException e) {
            return null;
        }
    }

    /** Lấy tên nhân viên lập hóa đơn, ưu tiên người đang đăng nhập. */
    private String tenNhanVien(String maNV) {
        if (nhanVienBUS != null) {
            if (Auth.getNguoiDung() != null
                    && Auth.getNguoiDung().getMaNV().equalsIgnoreCase(Validator.chuanHoa(maNV))) {
                return Auth.getTenHienThi();
            }
            if (nhanVienBUS.timTheoMa(maNV) != null) {
                return nhanVienBUS.timTheoMa(maNV).getTenNV();
            }
        }
        return Validator.chuanHoa(maNV);
    }

    // ==================== KHU VỰC ĐỊNH DẠNG ====================
    /** Định dạng tiền Việt Nam để hiển thị: 15000 -> 15.000Đ. */
    public static String dinhDangTien(double soTien) {
        return Validator.dinhDangTien(soTien);
    }
}
