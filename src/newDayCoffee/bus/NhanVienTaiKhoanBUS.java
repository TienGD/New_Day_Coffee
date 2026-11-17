package newDayCoffee.bus;

import java.util.List;

import newDayCoffee.dao.NhanVienDAO;
import newDayCoffee.dao.TaiKhoanDAO;
import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.dto.TaiKhoanDTO;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.Validator;

/**
 * Lớp điều phối nghiệp vụ giữa nhân viên (DSNhanVienBUS) và tài khoản (DSTaiKhoanBUS).
 *
 * Bảo đảm kiến trúc phụ thuộc một chiều (DAG), giữ ràng buộc 1 nhân viên - 0..1 tài khoản
 * và chính sách RESTRICT khi xóa nhân viên đang có tài khoản.
 */
public class NhanVienTaiKhoanBUS {

    private final DSNhanVienBUS dsNhanVienBUS;
    private final DSTaiKhoanBUS dsTaiKhoanBUS;
    private DSHoaDonBUS dsHoaDonBUS;

    public NhanVienTaiKhoanBUS(DSNhanVienBUS dsNhanVienBUS, DSTaiKhoanBUS dsTaiKhoanBUS) {
        this.dsNhanVienBUS = dsNhanVienBUS != null ? dsNhanVienBUS : new DSNhanVienBUS();
        this.dsTaiKhoanBUS = dsTaiKhoanBUS != null ? dsTaiKhoanBUS : new DSTaiKhoanBUS();
    }

    public void setDsHoaDonBUS(DSHoaDonBUS dsHoaDonBUS) {
        this.dsHoaDonBUS = dsHoaDonBUS;
    }

    public DSNhanVienBUS getDsNhanVienBUS() {
        return dsNhanVienBUS;
    }

    public DSTaiKhoanBUS getDsTaiKhoanBUS() {
        return dsTaiKhoanBUS;
    }

    /** Tự động tải dữ liệu nhân viên và tài khoản từ CSDL cho phiên làm việc. */
    public void khoiTaoDuLieu() {
        if (this.dsNhanVienBUS == null || this.dsTaiKhoanBUS == null
                || (this.dsNhanVienBUS.laySoLuongNhanVien() > 0 && this.dsTaiKhoanBUS.laySoLuongTaiKhoan() > 0)) {
            return;
        }
        napDuLieuTuCSDL(this.dsNhanVienBUS, this.dsTaiKhoanBUS);
    }

    public static boolean napDuLieuTuCSDL(DSNhanVienBUS dsNhanVienBUS, DSTaiKhoanBUS dsTaiKhoanBUS) {
        try {
            List<NhanVienDTO> dsNhanVienDB = new NhanVienDAO().getAll();
            if (dsNhanVienDB == null || dsNhanVienDB.isEmpty()) {
                return false;
            }
            List<TaiKhoanDTO> dsTaiKhoanDB = new TaiKhoanDAO().getAll();
            if (!coTaiKhoanHopLeTrongCSDL(dsNhanVienDB, dsTaiKhoanDB)) {
                return false;
            }
            if (!dsNhanVienBUS.napDuLieuTuCSDL(dsNhanVienDB)) {
                return false;
            }
            return dsTaiKhoanBUS.napDuLieuTuCSDL(dsTaiKhoanDB, dsNhanVienBUS);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean coTaiKhoanHopLeTrongCSDL(List<NhanVienDTO> dsNhanVienDB, List<TaiKhoanDTO> dsTaiKhoanDB) {
        if (dsNhanVienDB == null || dsNhanVienDB.isEmpty() || dsTaiKhoanDB == null || dsTaiKhoanDB.isEmpty()) {
            return false;
        }
        for (TaiKhoanDTO tk : dsTaiKhoanDB) {
            if (tk == null || tk.getMaNV() == null || tk.getMatKhau() == null || tk.getMatKhau().trim().isEmpty()) {
                continue;
            }
            String maNV = tk.getMaNV().getMaNV() == null ? "" : tk.getMaNV().getMaNV().trim();
            if (maNV.isEmpty()) {
                continue;
            }
            for (NhanVienDTO nv : dsNhanVienDB) {
                if (nv != null && nv.getMaNV() != null && nv.getMaNV().trim().equalsIgnoreCase(maNV)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Xác thực đăng nhập và lưu nhân viên vào phiên Auth. */
    public String dangNhap(String maNV, String matKhau) {
        if (dsNhanVienBUS.laySoLuongNhanVien() == 0 || dsTaiKhoanBUS.laySoLuongTaiKhoan() == 0) {
            khoiTaoDuLieu();
        }
        String loi = dsTaiKhoanBUS.xacThucDangNhap(maNV, matKhau);
        if (loi != null) {
            return loi;
        }
        NhanVienDTO nhanVien = dsNhanVienBUS.timTheoMa(maNV);
        if (nhanVien == null) {
            return "Không tìm thấy thông tin nhân viên.";
        }
        Auth.dangNhap(nhanVien);
        return null;
    }

    /** Kết thúc phiên đăng nhập hiện tại. */
    public void dangXuat() {
        Auth.dangXuat();
    }

    /** Đặt lại mật khẩu theo email nhân viên và đồng bộ xuống CSDL. */
    public String datLaiMatKhau(String email, String matKhauMoi) {
        if (dsNhanVienBUS.laySoLuongNhanVien() == 0 || dsTaiKhoanBUS.laySoLuongTaiKhoan() == 0) {
            khoiTaoDuLieu();
        }
        String thuDienTu = chuanHoa(email);
        NhanVienDTO nv = dsNhanVienBUS.timTheoEmail(thuDienTu);
        if (nv == null) {
            return "Không tìm thấy nhân viên với email này.";
        }
        String loi = dsTaiKhoanBUS.doiMatKhau(nv.getMaNV(), matKhauMoi);
        if (loi != null) {
            return loi;
        }
        dsTaiKhoanBUS.luuVaoCSDL();
        return null;
    }

    public NhanVienDTO timNhanVien(String maNV) {
        return dsNhanVienBUS.timTheoMa(maNV);
    }

    public boolean nhanVienDaCoTaiKhoan(String maNV) {
        return dsTaiKhoanBUS.timTheoMa(maNV) != null;
    }

    /** Thêm tài khoản mới cho nhân viên đã tồn tại (mật khẩu bắt buộc). */
    public String themTaiKhoan(String maNV, String matKhau) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền thêm tài khoản.";
        }
        String ma = chuanHoa(maNV);
        if (ma.isEmpty()) {
            return "Vui lòng nhập mã nhân viên.";
        }
        NhanVienDTO nhanVien = dsNhanVienBUS.timTheoMa(ma);
        if (nhanVien == null) {
            return "Không tìm thấy nhân viên với mã này.";
        }
        if (dsTaiKhoanBUS.timTheoMa(nhanVien.getMaNV()) != null) {
            return "Tài khoản cho mã nhân viên này đã tồn tại.";
        }
        String mk = chuanHoa(matKhau);
        if (mk.isEmpty()) {
            return "Vui lòng nhập mật khẩu.";
        }
        if (!mk.matches(Validator.MAT_KHAU)) {
            return "Mật khẩu phải có từ 6 đến 32 ký tự.";
        }
        return dsTaiKhoanBUS.themTaiKhoan(new TaiKhoanDTO(nhanVien, mk));
    }

    /** Cập nhật mật khẩu cho tài khoản đã có. */
    public String suaTaiKhoan(String maNV, String matKhauMoi) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền sửa tài khoản.";
        }
        String mk = chuanHoa(matKhauMoi);
        if (mk.isEmpty()) {
            return "Vui lòng nhập mật khẩu.";
        }
        if (!mk.matches(Validator.MAT_KHAU)) {
            return "Mật khẩu phải có từ 6 đến 32 ký tự.";
        }
        return dsTaiKhoanBUS.doiMatKhau(maNV, mk);
    }

    /** Xóa nhân viên theo chính sách RESTRICT: từ chối nếu đang đăng nhập hoặc còn tài khoản. */
    public String xoaNhanVien(String maNV) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền xóa nhân viên.";
        }
        NhanVienDTO nv = dsNhanVienBUS.timTheoMa(maNV);
        if (nv == null) {
            return "Không tìm thấy nhân viên cần xóa.";
        }
        if (Auth.getNguoiDung() != null
                && chuanHoa(Auth.getNguoiDung().getMaNV()).equalsIgnoreCase(chuanHoa(nv.getMaNV()))) {
            return "Không thể xóa nhân viên đang đăng nhập.";
        }
        if (dsTaiKhoanBUS.timTheoMa(nv.getMaNV()) != null) {
            return "Không thể xóa nhân viên đang có tài khoản. Vui lòng xóa tài khoản trước.";
        }
        if (dsHoaDonBUS != null && dsHoaDonBUS.coHoaDonCuaNhanVien(nv.getMaNV())) {
            return "Không thể xóa nhân viên đã lập hóa đơn.";
        }
        return dsNhanVienBUS.xoaNhanVien(nv.getMaNV());
    }

    /** Xóa tài khoản. Chặn xóa tài khoản đang đăng nhập. */
    public String xoaTaiKhoan(String maNV) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền xóa tài khoản.";
        }
        String ma = chuanHoa(maNV);
        if (ma.isEmpty()) {
            return "Vui lòng chọn hoặc nhập mã tài khoản cần xóa.";
        }
        TaiKhoanDTO tk = dsTaiKhoanBUS.timTheoMa(ma);
        if (tk == null) {
            return "Không tìm thấy tài khoản cần xóa.";
        }
        if (Auth.getNguoiDung() != null && tk.getMaNV() != null
                && chuanHoa(Auth.getNguoiDung().getMaNV()).equalsIgnoreCase(chuanHoa(tk.getMaNV().getMaNV()))) {
            return "Không thể xóa tài khoản đang đăng nhập.";
        }
        if (!dsTaiKhoanBUS.xoaTaiKhoan(tk.getMaNV().getMaNV())) {
            return "Không tìm thấy tài khoản cần xóa.";
        }
        return null;
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
