package newDayCoffee.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Pattern;

import newDayCoffee.dto.NhanVienDTO;

/**
 * Tầng kiểm tra dữ liệu dùng chung cho toàn bộ hệ thống.
 *
 * Quy ước:
 *  - Các hàm kiemTra...() trả về null nếu dữ liệu hợp lệ.
 *  - Trả về chuỗi thông báo lỗi (tiếng Việt) nếu dữ liệu không hợp lệ.
 * Nhờ vậy tầng BUS có thể chuyển thẳng thông báo lỗi cho giao diện.
 */
public class Validator {

    // ==================== KHU VỰC HẰNG SỐ ====================
    private static final Pattern MA_NHAN_VIEN = Pattern.compile("^[A-Za-z]{1,4}\\d{2,4}$");
    private static final Pattern MA_MON = Pattern.compile("^[A-Za-z]{2,4}\\d{2,4}$");
    private static final Pattern SO_DIEN_THOAI = Pattern.compile("^0\\d{8,10}$");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private Validator() {
        // Lớp tiện ích, không cho phép khởi tạo
    }

    // ==================== KHU VỰC CHUẨN HÓA CHUỖI ====================
    /** Cắt khoảng trắng thừa, biến null thành chuỗi rỗng. */
    public static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }

    /** Kiểm tra chuỗi rỗng hoặc chỉ chứa khoảng trắng. */
    public static boolean rong(String giaTri) {
        return chuanHoa(giaTri).isEmpty();
    }

    // ==================== KHU VỰC KIỂM TRA ĐĂNG NHẬP ====================
    /** Kiểm tra dữ liệu nhập ở màn hình đăng nhập. */
    public static String kiemTraDangNhap(String maNV, String matKhau) {
        if (rong(maNV)) {
            return "Vui lòng nhập mã nhân viên.";
        }
        if (rong(matKhau)) {
            return "Vui lòng nhập mật khẩu.";
        }
        if (chuanHoa(matKhau).length() < 6) {
            return "Mật khẩu phải có ít nhất 6 ký tự.";
        }
        return null;
    }

    // ==================== KHU VỰC KIỂM TRA NHÂN VIÊN ====================
    /** Kiểm tra toàn bộ thông tin nhân viên trước khi thêm/sửa. */
    public static String kiemTraNhanVien(NhanVienDTO nv) {
        if (nv == null) {
            return "Thông tin nhân viên không hợp lệ.";
        }
        String loi = kiemTraMaNV(nv.getMaNV());
        if (loi != null) {
            return loi;
        }
        loi = kiemTraTen(nv.getTenNV());
        if (loi != null) {
            return loi;
        }
        loi = kiemTraNgaySinh(nv.getNgaySinh());
        if (loi != null) {
            return loi;
        }
        loi = kiemTraSDT(nv.getSdt());
        if (loi != null) {
            return loi;
        }
        loi = kiemTraEmail(nv.getEmail());
        if (loi != null) {
            return loi;
        }
        loi = kiemTraChucVu(nv.getChucVu());
        if (loi != null) {
            return loi;
        }
        return kiemTraGioiTinh(nv.getGioiTinh());
    }

    public static String kiemTraMaNV(String maNV) {
        String ma = chuanHoa(maNV);
        if (ma.isEmpty()) {
            return "Vui lòng nhập mã nhân viên.";
        }
        if (!MA_NHAN_VIEN.matcher(ma).matches()) {
            return "Mã nhân viên gồm 1-4 chữ cái và 2-4 chữ số (ví dụ NV01).";
        }
        return null;
    }

    public static String kiemTraTen(String ten) {
        String t = chuanHoa(ten);
        if (t.isEmpty()) {
            return "Vui lòng nhập tên nhân viên.";
        }
        if (t.length() < 2 || t.length() > 50) {
            return "Tên nhân viên phải có từ 2 đến 50 ký tự.";
        }
        if (t.matches(".*\\d.*")) {
            return "Tên nhân viên không được chứa chữ số.";
        }
        return null;
    }

    public static String kiemTraNgaySinh(LocalDate ngaySinh) {
        if (ngaySinh == null) {
            return "Ngày sinh không hợp lệ (định dạng dd-MM-yyyy).";
        }
        if (ngaySinh.isAfter(LocalDate.now())) {
            return "Ngày sinh không được lớn hơn ngày hiện tại.";
        }
        if (ngaySinh.getYear() < 1900) {
            return "Năm sinh phải từ 1900 trở lại đây.";
        }
        return null;
    }

    public static String kiemTraSDT(String sdt) {
        String so = chuanHoa(sdt).replace(" ", "").replace("-", "");
        if (so.isEmpty()) {
            return "Vui lòng nhập số điện thoại.";
        }
        if (!SO_DIEN_THOAI.matcher(so).matches()) {
            return "Số điện thoại phải bắt đầu bằng 0 và có 9-11 chữ số.";
        }
        return null;
    }

    public static String kiemTraEmail(String email) {
        String e = chuanHoa(email);
        if (e.isEmpty()) {
            // Email không bắt buộc, cho phép bỏ trống
            return null;
        }
        if (!EMAIL.matcher(e).matches()) {
            return "Email không đúng định dạng (ví dụ ten@gmail.com).";
        }
        return null;
    }

    public static String kiemTraChucVu(String chucVu) {
        if (rong(chucVu)) {
            return "Vui lòng chọn chức vụ.";
        }
        return null;
    }

    public static String kiemTraGioiTinh(String gioiTinh) {
        String gt = chuanHoa(gioiTinh);
        if (gt.isEmpty()) {
            return "Vui lòng chọn giới tính.";
        }
        if (!gt.equals("Nam") && !gt.equals("Nữ") && !gt.equals("Khác")) {
            return "Giới tính chỉ nhận giá trị Nam, Nữ hoặc Khác.";
        }
        return null;
    }

    // ==================== KHU VỰC KIỂM TRA ĐỒ UỐNG ====================
    /** Kiểm tra dữ liệu form thực đơn (giá nhập dưới dạng chuỗi). */
    public static String kiemTraDoUong(String maMon, String tenMon, String donGiaText) {
        String ma = chuanHoa(maMon);
        if (ma.isEmpty()) {
            return "Vui lòng nhập mã món.";
        }
        if (!MA_MON.matcher(ma).matches()) {
            return "Mã món gồm 2-4 chữ cái và 2-4 chữ số (ví dụ cof01, tea02).";
        }
        String ten = chuanHoa(tenMon);
        if (ten.isEmpty()) {
            return "Vui lòng nhập tên món.";
        }
        if (ten.length() < 2 || ten.length() > 50) {
            return "Tên món phải có từ 2 đến 50 ký tự.";
        }
        double donGia = docSoTien(donGiaText);
        if (donGia < 0) {
            return "Giá bán không hợp lệ.";
        }
        if (donGia <= 0) {
            return "Giá bán phải lớn hơn 0.";
        }
        if (donGia > 10_000_000) {
            return "Giá bán không được vượt quá 10.000.000Đ.";
        }
        return null;
    }

    // ==================== KHU VỰC KIỂM TRA HÓA ĐƠN / THANH TOÁN ====================
    public static String kiemTraSoLuong(int soLuong) {
        if (soLuong <= 0) {
            return "Số lượng món phải lớn hơn 0.";
        }
        return null;
    }

    /** Kiểm tra thông tin thanh toán trước khi lập hóa đơn. */
    public static String kiemTraThanhToan(double tongTien, double tienKhachDua, String phuongThucTT) {
        if (tongTien <= 0) {
            return "Đơn hàng chưa có món nào để thanh toán.";
        }
        if (rong(phuongThucTT)) {
            return "Vui lòng chọn phương thức thanh toán.";
        }
        if (tienKhachDua < 0) {
            return "Tiền khách đưa không hợp lệ.";
        }
        if (tienKhachDua < tongTien) {
            return "Tiền khách đưa không đủ. Còn thiếu "
                    + String.format(Locale.US, "%,.0f", tongTien - tienKhachDua).replace(',', '.') + "Đ.";
        }
        return null;
    }

    // ==================== KHU VỰC HỖ TRỢ NGÀY / TIỀN ====================
    /**
     * Đọc ngày từ chuỗi người dùng nhập.
     * Hỗ trợ dd-MM-yyyy, dd/MM/yyyy, yyyy-MM-dd.
     *
     * @return LocalDate nếu đọc được, null nếu sai định dạng.
     */
    public static LocalDate docNgay(String chuoiNgay) {
        String s = chuanHoa(chuoiNgay);
        if (s.isEmpty()) {
            return null;
        }
        String[] dsDinhDang = {"d-M-yyyy", "d/M/yyyy", "yyyy-M-d"};
        for (int i = 0; i < dsDinhDang.length; i++) {
            try {
                return LocalDate.parse(s, DateTimeFormatter.ofPattern(dsDinhDang[i]));
            } catch (DateTimeParseException e) {
                // Thử định dạng tiếp theo
            }
        }
        return null;
    }

    /** Chuyển ngày sang chuỗi hiển thị dd-MM-yyyy. */
    public static String dinhDangNgay(LocalDate ngay) {
        if (ngay == null) {
            return "";
        }
        return ngay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    /**
     * Đọc số tiền người dùng nhập. Chấp nhận các dạng 15000, 15.000, 15,000.
     *
     * @return số tiền, hoặc -1 nếu chuỗi không hợp lệ.
     */
    public static double docSoTien(String chuoiTien) {
        String s = chuanHoa(chuoiTien);
        if (s.isEmpty()) {
            return -1;
        }
        s = s.replace("đ", "").replace("Đ", "").replace(" ", "");
        if (!s.matches("\\d[\\d.,]*")) {
            return -1;
        }
        try {
            s = s.replace(".", "").replace(",", "");
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
