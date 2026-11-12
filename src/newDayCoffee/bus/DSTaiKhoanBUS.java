package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.NhanVienDTO;
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
    }

    // ==================== KHU VỰC ĐỌC / KIỂM TRA ====================
    /** Trả về bản sao danh sách tài khoản để giao diện hiển thị. */
    public List<TaiKhoanDTO> layDanhSach() {
        return new ArrayList<>(dsTaiKhoan);
    }

    /** Tìm tài khoản theo mã nhân viên (không phân biệt hoa thường). */
    public TaiKhoanDTO timTheoMa(String maNV) {
        String ma = Validator.chuanHoa(maNV);
        for (TaiKhoanDTO tk : dsTaiKhoan) {
            if (tk.getMaNV().getMaNV().equalsIgnoreCase(ma)) {
                return tk;
            }
        }
        return null;
    }

    /** Kiểm tra thông tin đăng nhập. */
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
    /** Thêm tài khoản mới. Bỏ trống mật khẩu thì dùng mật khẩu mặc định. */
    public String themTaiKhoan(String maNV, String matKhau) {
        String loi = Validator.kiemTraMaNV(maNV);
        if (loi != null) {
            return loi;
        }
        if (timTheoMa(maNV) != null) {
            return "Tài khoản cho mã nhân viên này đã tồn tại.";
        }

        String mk = Validator.rong(matKhau) ? "123456" : matKhau;
        loi = Validator.kiemTraMatKhau(mk);
        if (loi != null) {
            return loi;
        }

        TaiKhoanDTO tk = new TaiKhoanDTO(new NhanVienDTO(Validator.chuanHoa(maNV)), mk);
        dsTaiKhoan.add(tk);
        return null;
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

    /** Lưu danh sách vào CSDL. Hiện dữ liệu vẫn nằm trong bộ nhớ. */
    public boolean luuVaoCSDL() {
        return true;
    }

    /** Đổi mật khẩu của một tài khoản đã có. */
    public String doiMatKhau(String maNV, String matKhauMoi) {
        TaiKhoanDTO tk = timTheoMa(maNV);
        if (tk == null) {
            return "Không tìm thấy tài khoản cần đổi mật khẩu.";
        }
        String loi = Validator.kiemTraMatKhau(matKhauMoi);
        if (loi != null) {
            return loi;
        }
        tk.setMatKhau(matKhauMoi);
        return null;
    }
}
