package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dao.TaiKhoanDAO;
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
    private final List<TaiKhoanDTO> dsThemMoi;
    private final List<TaiKhoanDTO> dsCapNhat;
    private final List<String> dsMaXoa;

    public DSTaiKhoanBUS() {
        dsTaiKhoan = new ArrayList<>();
        dsThemMoi = new ArrayList<>();
        dsCapNhat = new ArrayList<>();
        dsMaXoa = new ArrayList<>();
    }

    // ==================== KHU VỰC ĐỌC / KIỂM TRA ====================
    /** Trả về bản sao danh sách tài khoản để giao diện hiển thị. */
    public List<TaiKhoanDTO> layDanhSach() {
        return new ArrayList<>(dsTaiKhoan);
    }

    /** Tìm tài khoản theo mã nhân viên / mã đăng nhập (không phân biệt hoa thường). */
    public TaiKhoanDTO timTheoMa(String maNV) {
        String ma = chuanHoa(maNV);
        if (ma.isEmpty()) {
            return null;
        }
        for (TaiKhoanDTO tk : dsTaiKhoan) {
            if (tk == null) {
                continue;
            }
            if (tk.getMaNV() != null && chuanHoa(tk.getMaNV().getMaNV()).equalsIgnoreCase(ma)) {
                return tk;
            }
            if (chuanHoa(tk.getMaDangNhap()).equalsIgnoreCase(ma)) {
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
        if (chuanHoa(tk.getMaDangNhap()).isEmpty()) {
            tk.setMaDangNhap(maNV);
        }
        dsTaiKhoan.add(tk);
        ghiNhanThemMoi(tk);
        return null;
    }

    /** Xóa tài khoản theo mã nhân viên. */
    public boolean xoaTaiKhoan(String maNV) {
        TaiKhoanDTO tk = timTheoMa(maNV);
        if (tk == null) {
            return false;
        }
        dsTaiKhoan.remove(tk);
        ghiNhanXoa(tk.getMaDangNhap());
        return true;
    }

    /**
     * Lưu toàn bộ thay đổi chờ lưu (thêm, sửa, xóa) xuống SQL Server thông qua TaiKhoanDAO
     * và đồng bộ lại danh sách tài khoản với dữ liệu thực tế trong database.
     */
    public boolean luuVaoCSDL() {
        TaiKhoanDAO dao = new TaiKhoanDAO();
        if (dsThemMoi.isEmpty() && dsCapNhat.isEmpty() && dsMaXoa.isEmpty()) {
            List<TaiKhoanDTO> dsHienTai = dao.getAll();
            if (dsHienTai == null || (dsHienTai.isEmpty() && !dsTaiKhoan.isEmpty())) {
                return false;
            }
            return true;
        }

        while (!dsThemMoi.isEmpty()) {
            TaiKhoanDTO tk = dsThemMoi.get(0);
            if (!dao.insert(tk)) {
                return false;
            }
            dsThemMoi.remove(0);
        }

        while (!dsCapNhat.isEmpty()) {
            TaiKhoanDTO tk = dsCapNhat.get(0);
            if (!dao.update(tk)) {
                return false;
            }
            dsCapNhat.remove(0);
        }

        while (!dsMaXoa.isEmpty()) {
            String ma = dsMaXoa.get(0);
            if (!dao.delete(ma)) {
                return false;
            }
            dsMaXoa.remove(0);
        }

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
        ghiNhanCapNhat(tk);
        return null;
    }

    private void ghiNhanThemMoi(TaiKhoanDTO tk) {
        String ma = chuanHoa(tk.getMaDangNhap()).toLowerCase();
        if (dsMaXoa.remove(ma)) {
            dsCapNhat.add(tk);
        } else {
            dsThemMoi.add(tk);
        }
    }

    private void ghiNhanCapNhat(TaiKhoanDTO tk) {
        String ma = chuanHoa(tk.getMaDangNhap()).toLowerCase();
        for (int i = 0; i < dsThemMoi.size(); i++) {
            if (chuanHoa(dsThemMoi.get(i).getMaDangNhap()).equalsIgnoreCase(ma)) {
                dsThemMoi.set(i, tk);
                return;
            }
        }
        for (int i = 0; i < dsCapNhat.size(); i++) {
            if (chuanHoa(dsCapNhat.get(i).getMaDangNhap()).equalsIgnoreCase(ma)) {
                dsCapNhat.set(i, tk);
                return;
            }
        }
        dsCapNhat.add(tk);
    }

    private void ghiNhanXoa(String maDangNhap) {
        String ma = chuanHoa(maDangNhap).toLowerCase();
        for (int i = dsThemMoi.size() - 1; i >= 0; i--) {
            if (chuanHoa(dsThemMoi.get(i).getMaDangNhap()).equalsIgnoreCase(ma)) {
                dsThemMoi.remove(i);
                return;
            }
        }
        for (int i = dsCapNhat.size() - 1; i >= 0; i--) {
            if (chuanHoa(dsCapNhat.get(i).getMaDangNhap()).equalsIgnoreCase(ma)) {
                dsCapNhat.remove(i);
            }
        }
        if (!dsMaXoa.contains(ma)) {
            dsMaXoa.add(ma);
        }
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
        dsThemMoi.clear();
        dsCapNhat.clear();
        dsMaXoa.clear();
        return true;
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
