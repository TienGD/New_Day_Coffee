package newDayCoffee.bus;

import newDayCoffee.dao.DoUongDAO;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.utils.Validator;

import java.util.ArrayList;
import java.util.List;

import java.util.Locale;

import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.utils.Validator;

/**
 * Xử lý nghiệp vụ cho một món đồ uống.
 *
 * Lớp này tập trung vào việc kiểm tra dữ liệu (thông qua Validator),
 * chuyển dữ liệu từ form nhập liệu thành DTO và định dạng giá để hiển thị.
 * Các thao tác trên cả danh sách nằm ở DSDoUongBUS.
 */
public class DoUongBUS {

    // ==================== KHU VỰC KIỂM TRA DỮ LIỆU ====================
    /**
     * Kiểm tra dữ liệu form thực đơn.
     *
     * @return null nếu hợp lệ, ngược lại là thông báo lỗi.
     */
    public String kiemTraThongTin(String maMon, String tenMon, String donGiaText) {
        return Validator.kiemTraDoUong(maMon, tenMon, donGiaText);
    }

    /** Kiểm tra mã món trước khi tìm kiếm / xóa. */
    public String kiemTraMaMon(String maMon) {
        if (Validator.rong(maMon)) {
            return "Vui lòng nhập mã món.";
        }
        return null;
    }

    // ==================== KHU VỰC CHUYỂN ĐỔI DỮ LIỆU ====================
    /**
     * Tạo DTO từ dữ liệu đã được kiểm tra hợp lệ.
     * Nếu dữ liệu chưa hợp lệ, trả về null.
     */
    public DoUongDTO taoDoUong(String maMon, String tenMon, String donGiaText) {
        if (kiemTraThongTin(maMon, tenMon, donGiaText) != null) {
            return null;
        }
        double donGia = Validator.docSoTien(donGiaText);
        return new DoUongDTO(Validator.chuanHoa(maMon), Validator.chuanHoa(tenMon), donGia, "");
    }

    /** Đọc giá bán từ chuỗi người dùng nhập. */
    public double docDonGia(String donGiaText) {
        return Validator.docSoTien(donGiaText);
    }

    /** Định dạng tiền Việt Nam để hiển thị: 15000 -> 15.000Đ. */
    public static String dinhDangGia(double soTien) {
        return String.format(Locale.US, "%,.0f", soTien).replace(',', '.') + "Đ";
    }

    /** Định dạng số tiền không kèm ký hiệu, dùng để đưa ngược lên form sửa. */
    public static String dinhDangGiaNhap(double soTien) {
        return String.format(Locale.US, "%,.0f", soTien).replace(",", "");
    }
}
