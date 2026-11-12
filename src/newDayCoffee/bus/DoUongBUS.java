package newDayCoffee.bus;

import java.util.Locale;

import newDayCoffee.dto.DoUongDTO;

/**
 * Xử lý nghiệp vụ cho một món đồ uống.
 *
 * Lớp này chuyển dữ liệu từ form thành DTO và định dạng giá.
 * Việc kiểm tra dữ liệu form do giao diện thực hiện.
 */
public class DoUongBUS {

    // ==================== KHU VỰC CHUYỂN ĐỔI DỮ LIỆU ====================
    /** Tạo DTO từ dữ liệu đã được kiểm tra hợp lệ. */
    public DoUongDTO taoDoUong(String maMon, String tenMon, double donGia, String moTa) {
        return new DoUongDTO(chuanHoa(maMon), chuanHoa(tenMon), donGia,
                chuanHoa(moTa));
    }

    // ==================== KHU VỰC ĐỊNH DẠNG ====================
    /** Định dạng tiền Việt Nam để hiển thị: 15000 -> 15.000Đ. */
    public static String dinhDangGia(double soTien) {
        return String.format(Locale.US, "%,.0f", soTien).replace(',', '.') + "Đ";
    }

    /** Định dạng số tiền không kèm ký hiệu, dùng để đưa ngược lên form sửa. */
    public static String dinhDangGiaNhap(double soTien) {
        return String.format(Locale.US, "%,.0f", soTien).replace(",", "");
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
