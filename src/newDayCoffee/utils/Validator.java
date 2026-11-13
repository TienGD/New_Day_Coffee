package newDayCoffee.utils;

/**
 * Chứa các biểu thức chính quy dùng để kiểm tra dữ liệu nhập trên giao diện.
 *
 * Việc kiểm tra được thực hiện tại các sự kiện của giao diện bằng if và
 * phương thức matches(). Lớp này chỉ lưu hằng số regex, không xử lý nghiệp vụ.
 */
public class Validator {

    // ==================== KHU VỰC HẰNG SỐ REGEX ====================
    public static final String MA_NHAN_VIEN = "(?i)^(nv|ql)\\d{2}$";
    public static final String MA_MON = "(?i)^(cof|tea|mil|bak)\\d{2}$";
    public static final String SO_DIEN_THOAI = "^0\\d{9}$";
    public static final String EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String MAT_KHAU = "^.{6,}$";
    public static final String SO_TIEN = "^\\d[\\d.,]*$";
    public static final String NGAY_SINH = "^\\d{1,2}[-/]\\d{1,2}[-/]\\d{4}$|^\\d{4}-\\d{1,2}-\\d{1,2}$";
    public static final String MA_CA_TRUC = "^CA[0-9]{2}$";
    public static final String NGAY_TRUC = "^[0-9]{2}-[0-9]{2}-[0-9]{4}$";
    public static final String SO_LUONG_CA_TRUC = "^[0-9]+$";

    private Validator() {
        // Lớp tiện ích, không cho phép khởi tạo
    }
}
