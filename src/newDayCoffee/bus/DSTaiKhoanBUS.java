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
 * Thông tin cá nhân của nhân viên nằm ở DSNhanVienBUS.
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
        if (ma.isEmpty()) {
            return null;
        }
        for (TaiKhoanDTO tk : dsTaiKhoan) {
            if (tk.getMaNV() != null && chuanHoa(tk.getMaNV().getMaNV()).equalsIgnoreCase(ma)) {
                return tk;
            }
        }
        return null;
    }

    /** Kiểm tra thông tin đăng nhập. */
    public String xacThucDangNhap(String maNV, String matKhau) {
        TaiKhoanDTO tk = timTheoMa(maNV);
        if (tk == null || matKhau == null || tk.getMatKhau() == null || !tk.getMatKhau().equals(matKhau)) {
            return "Mã nhân viên hoặc mật khẩu không đúng.";
        }
        return null;
    }

    public int laySoLuongTaiKhoan() {
        return dsTaiKhoan.size();
    }

    // ==================== KHU VỰC THÊM / XÓA TÀI KHOẢN ====================
    /** Thêm tài khoản mới. Mật khẩu bắt buộc phải hợp lệ, không gán mặc định. */
    public String themTaiKhoan(TaiKhoanDTO tk) {
        if (tk == null || tk.getMaNV() == null) {
            return "Thông tin tài khoản không hợp lệ.";
        }

        String maNV = chuanHoa(tk.getMaNV().getMaNV());
        if (maNV.isEmpty()) {
            return "Vui lòng nhập mã nhân viên.";
        }
        if (timTheoMa(maNV) != null) {
            return "Tài khoản cho mã nhân viên này đã tồn tại.";
        }

        String matKhau = chuanHoa(tk.getMatKhau());
        if (matKhau.isEmpty()) {
            return "Vui lòng nhập mật khẩu.";
        }
        if (!matKhau.matches(Validator.MAT_KHAU)) {
            return "Mật khẩu phải có từ 6 đến 32 ký tự.";
        }
        tk.setMatKhau(matKhau);
        dsTaiKhoan.add(tk);
        return null;
    }

    /** Xóa tài khoản theo mã nhân viên. */
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
        String matKhau = chuanHoa(matKhauMoi);
        if (matKhau.isEmpty()) {
            return "Vui lòng nhập mật khẩu mới.";
        }
        if (!matKhau.matches(Validator.MAT_KHAU)) {
            return "Mật khẩu phải có từ 6 đến 32 ký tự.";
        }
        tk.setMatKhau(matKhau);
        return null;
    }

    /** Nạp danh sách tài khoản mẫu khi dữ liệu còn trống. */
    public boolean napDuLieuMau(List<TaiKhoanDTO> dsTaiKhoanMau) {
        if (laySoLuongTaiKhoan() > 0 || dsTaiKhoanMau == null || dsTaiKhoanMau.isEmpty()) {
            return false;
        }
        List<String> dsMaDaCo = new ArrayList<>();
        List<String> dsMatKhauChuan = new ArrayList<>();
        for (TaiKhoanDTO taiKhoan : dsTaiKhoanMau) {
            if (taiKhoan == null || taiKhoan.getMaNV() == null) {
                return false;
            }
            String maNV = chuanHoa(taiKhoan.getMaNV().getMaNV()).toLowerCase();
            String matKhau = chuanHoa(taiKhoan.getMatKhau());
            if (maNV.isEmpty() || dsMaDaCo.contains(maNV) || !matKhau.matches(Validator.MAT_KHAU)) {
                return false;
            }
            dsMaDaCo.add(maNV);
            dsMatKhauChuan.add(matKhau);
        }
        for (int i = 0; i < dsTaiKhoanMau.size(); i++) {
            TaiKhoanDTO taiKhoan = dsTaiKhoanMau.get(i);
            taiKhoan.setMatKhau(dsMatKhauChuan.get(i));
            dsTaiKhoan.add(taiKhoan);
        }
        return true;
    }

    /** Nạp danh sách tài khoản đọc từ CSDL khi bộ nhớ đang trống, liên kết tham chiếu nhân viên. */
    public boolean napDuLieuTuCSDL(List<TaiKhoanDTO> dsTaiKhoanDB, DSNhanVienBUS dsNhanVienBUS) {
        if (laySoLuongTaiKhoan() > 0 || dsTaiKhoanDB == null || dsTaiKhoanDB.isEmpty()) {
            return false;
        }
        List<String> dsMaDaCo = new ArrayList<>();
        List<TaiKhoanDTO> hopLe = new ArrayList<>();
        for (TaiKhoanDTO taiKhoan : dsTaiKhoanDB) {
            if (taiKhoan == null || taiKhoan.getMaNV() == null) {
                continue;
            }
            String maNV = chuanHoa(taiKhoan.getMaNV().getMaNV());
            String khoaMa = maNV.toLowerCase();
            String matKhau = chuanHoa(taiKhoan.getMatKhau());
            if (khoaMa.isEmpty() || matKhau.isEmpty() || dsMaDaCo.contains(khoaMa)) {
                continue;
            }
            NhanVienDTO nhanVien = taiKhoan.getMaNV();
            if (dsNhanVienBUS != null) {
                NhanVienDTO nvTrongBus = dsNhanVienBUS.timTheoMa(maNV);
                if (nvTrongBus == null) {
                    continue;
                }
                nhanVien = nvTrongBus;
            }
            dsMaDaCo.add(khoaMa);
            hopLe.add(new TaiKhoanDTO(nhanVien, matKhau));
        }
        if (hopLe.isEmpty()) {
            return false;
        }
        dsTaiKhoan.addAll(hopLe);
        return true;
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
