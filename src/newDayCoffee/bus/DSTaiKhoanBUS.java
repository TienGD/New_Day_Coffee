package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.TaiKhoanDTO;

/**
 * Xử lý nghiệp vụ tài khoản đăng nhập.
 *
 * Lớp này chỉ quản lý cặp mã nhân viên - mật khẩu.
 * Thông tin cá nhân của nhân viên nằm ở NhanVienBUS.
 * Việc kiểm tra dữ liệu form do giao diện thực hiện.
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
        String ma = chuanHoa(maNV);
        for (TaiKhoanDTO tk : dsTaiKhoan) {
            if (tk.getMaNV().getMaNV().equalsIgnoreCase(ma)) {
                return tk;
            }
        }
        return null;
    }

    /** Kiểm tra thông tin đăng nhập. */
    public String xacThucDangNhap(String maNV, String matKhau) {
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
    /** Thêm tài khoản mới. DTO do giao diện tạo và truyền vào. */
    public String themTaiKhoan(TaiKhoanDTO tk) {
        if (tk == null || tk.getMaNV() == null) {
            return "Thông tin tài khoản không hợp lệ.";
        }

        String maNV = chuanHoa(tk.getMaNV().getMaNV());
        if (timTheoMa(maNV) != null) {
            return "Tài khoản cho mã nhân viên này đã tồn tại.";
        }

        if (chuanHoa(tk.getMatKhau()).isEmpty()) {
            tk.setMatKhau("123456");
        }
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
        tk.setMatKhau(matKhauMoi);
        return null;
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
