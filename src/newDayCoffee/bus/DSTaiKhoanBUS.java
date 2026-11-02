package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.TaiKhoanDTO;
import newDayCoffee.utils.Validator;

/**
 * Xử lý nghiệp vụ tài khoản đăng nhập.
 *
 * Lớp này chỉ quản lý cặp mã nhân viên - mật khẩu.
 * Thông tin cá nhân của nhân viên nằm ở NhanVienBUS.
 */
public class DSTaiKhoanBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<TaiKhoanDTO> dsTaiKhoan;

    public DSTaiKhoanBUS() {
        dsTaiKhoan = new ArrayList<>();
        taoDuLieuMau();
    }

    /** Tài khoản mẫu dùng khi chưa nối cơ sở dữ liệu. */
    private void taoDuLieuMau() {
        dsTaiKhoan.add(new TaiKhoanDTO("NV01", "123456"));
        dsTaiKhoan.add(new TaiKhoanDTO("NV02", "123456"));
        dsTaiKhoan.add(new TaiKhoanDTO("NV03", "123456"));
        dsTaiKhoan.add(new TaiKhoanDTO("NV04", "123456"));
    }

    // ==================== KHU VỰC ĐỌC / KIỂM TRA ====================
    /** Tìm tài khoản theo mã nhân viên (không phân biệt hoa thường). */
    public TaiKhoanDTO timTheoMa(String maNV) {
        String ma = Validator.chuanHoa(maNV);
        for (TaiKhoanDTO tk : dsTaiKhoan) {
            if (tk.getMaNhanVien().equalsIgnoreCase(ma)) {
                return tk;
            }
        }
        return null;
    }

    /**
     * Kiểm tra thông tin đăng nhập.
     *
     * @return null nếu đúng, ngược lại là thông báo lỗi.
     */
    public String kiemTraDangNhap(String maNV, String matKhau) {
        String loi = Validator.kiemTraDangNhap(maNV, matKhau);
        if (loi != null) {
            return loi;
        }
        TaiKhoanDTO tk = timTheoMa(maNV);
        if (tk == null || !tk.getMatKhau().equals(matKhau)) {
            return "Mã nhân viên hoặc mật khẩu không đúng.";
        }
        return null;
    }

    public int laySoLuongTaiKhoan() {
        return dsTaiKhoan.size();
    }

    // ==================== KHU VỰC THÊM / XÓA TÀI KHOẢN ====================
    /**
     * Thêm tài khoản mới cho nhân viên.
     * Mật khẩu mặc định do NhanVienBUS truyền vào.
     */
    public boolean themTaiKhoan(String maNV, String matKhau) {
        if (timTheoMa(maNV) != null) {
            return false;
        }
        String ma = Validator.chuanHoa(maNV);
        String mk = Validator.rong(matKhau) ? "123456" : Validator.chuanHoa(matKhau);
        dsTaiKhoan.add(new TaiKhoanDTO(ma, mk));
        return true;
    }

    /** Xóa tài khoản khi xóa nhân viên. */
    public boolean xoaTaiKhoan(String maNV) {
        TaiKhoanDTO tk = timTheoMa(maNV);
        if (tk == null) {
            return false;
        }
        dsTaiKhoan.remove(tk);
        return true;
    }

    /** Đổi mật khẩu của một tài khoản đã có. */
    public boolean doiMatKhau(String maNV, String matKhauMoi) {
        TaiKhoanDTO tk = timTheoMa(maNV);
        if (tk == null || Validator.rong(matKhauMoi) || matKhauMoi.length() < 6) {
            return false;
        }
        tk.setMatKhau(matKhauMoi);
        return true;
    }
}
