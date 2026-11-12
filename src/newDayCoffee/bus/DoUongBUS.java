package newDayCoffee.bus;

import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.utils.Validator;

/**
 * Xử lý nghiệp vụ cho một món đồ uống.
 *
 * Lớp này kiểm tra dữ liệu, chuyển dữ liệu từ form thành DTO và định dạng giá.
 * Các thao tác trên cả danh sách nằm ở DSDoUongBUS.
 */
public class DoUongBUS {

    // ==================== KHU VỰC KIỂM TRA DỮ LIỆU ====================
    /** Kiểm tra dữ liệu form thực đơn. */
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
    /** Tạo DTO từ dữ liệu đã được kiểm tra hợp lệ. */
    public DoUongDTO taoDoUong(String maMon, String tenMon, String donGiaText, String moTa) {
        if (kiemTraThongTin(maMon, tenMon, donGiaText) != null) {
            return null;
        }
        double donGia = Validator.docSoTien(donGiaText);
        return new DoUongDTO(Validator.chuanHoa(maMon), Validator.chuanHoa(tenMon), donGia,
                Validator.chuanHoa(moTa));
    }

    /** Đọc giá bán từ chuỗi người dùng nhập. */
    public double docDonGia(String donGiaText) {
        return Validator.docSoTien(donGiaText);
    }

    /** Định dạng tiền Việt Nam để hiển thị: 15000 -> 15.000Đ. */
    public static String dinhDangGia(double soTien) {
        return Validator.dinhDangTien(soTien);
    }

    /** Định dạng số tiền không kèm ký hiệu, dùng để đưa ngược lên form sửa. */
    public static String dinhDangGiaNhap(double soTien) {
        return Validator.dinhDangTienNhap(soTien);
    }
}
