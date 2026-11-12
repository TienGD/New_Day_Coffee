package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.dto.TaiKhoanDTO;
import newDayCoffee.utils.Auth;

/**
 * Xử lý nghiệp vụ nhân viên và đăng nhập.
 *
 * Lớp này quản lý dữ liệu nhân viên, gọi DSTaiKhoanBUS để quản lý tài khoản
 * và Auth để lưu / xóa phiên đăng nhập. Việc kiểm tra dữ liệu form do giao diện thực hiện.
 */
public class DSNhanVienBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<NhanVienDTO> dsNhanVien;
    private final DSTaiKhoanBUS taiKhoanBUS;

    public DSNhanVienBUS(DSTaiKhoanBUS taiKhoanBUS) {
        this.taiKhoanBUS = taiKhoanBUS;
        this.dsNhanVien = new ArrayList<>();
    }

    // ==================== KHU VỰC ĐĂNG NHẬP / ĐĂNG XUẤT ====================
    /** Kiểm tra tài khoản, nếu đúng thì lưu nhân viên vào Auth. */
    public String dangNhap(String maNV, String matKhau) {
        String loi = taiKhoanBUS.xacThucDangNhap(maNV, matKhau);
        if (loi != null) {
            return loi;
        }

        NhanVienDTO nhanVien = timTheoMa(maNV);
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

    /** Kiểm tra người đang đăng nhập có phải quản lí hay không. */
    public boolean laQuanLy() {
        return Auth.laQuanLy();
    }

    // ==================== KHU VỰC ĐỌC DỮ LIỆU ====================
    /** Trả về bản sao danh sách để giao diện không sửa dữ liệu gốc. */
    public List<NhanVienDTO> layDanhSachNhanVien() {
        return new ArrayList<>(dsNhanVien);
    }

    public NhanVienDTO timTheoMa(String maNV) {
        String ma = chuanHoa(maNV);
        for (NhanVienDTO nv : dsNhanVien) {
            if (nv.getMaNV().equalsIgnoreCase(ma)) {
                return nv;
            }
        }
        return null;
    }

    public int laySoLuongNhanVien() {
        return dsNhanVien.size();
    }

    /** Tìm nhân viên theo mã và/hoặc tên. Ô bỏ trống thì không xét điều kiện. */
    public List<NhanVienDTO> timKiem(String maNV, String tenNV) {
        List<NhanVienDTO> ketQua = new ArrayList<>();
        String maTim = chuanHoa(maNV).toLowerCase();
        String tenTim = chuanHoa(tenNV).toLowerCase();

        for (NhanVienDTO nv : dsNhanVien) {
            boolean dungMa = maTim.isEmpty()
                    || chuanHoa(nv.getMaNV()).toLowerCase().equals(maTim);
            boolean dungTen = tenTim.isEmpty()
                    || chuanHoa(nv.getTenNV()).toLowerCase().contains(tenTim);
            if (dungMa && dungTen) {
                ketQua.add(nv);
            }
        }
        return ketQua;
    }

    // ==================== KHU VỰC THÊM / SỬA / XÓA ====================
    /** Thêm nhân viên mới và tài khoản đi kèm. DTO do giao diện tạo và truyền vào. */
    public String themNhanVien(NhanVienDTO nv, TaiKhoanDTO tk) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền thêm nhân viên.";
        }
        if (nv == null || tk == null || tk.getMaNV() == null) {
            return "Thông tin nhân viên không hợp lệ.";
        }
        if (!chuanHoa(tk.getMaNV().getMaNV()).equalsIgnoreCase(chuanHoa(nv.getMaNV()))) {
            return "Tài khoản không khớp với mã nhân viên.";
        }
        if (timTheoMa(nv.getMaNV()) != null) {
            return "Mã nhân viên đã tồn tại.";
        }
        if (taiKhoanBUS.timTheoMa(nv.getMaNV()) != null) {
            return "Tài khoản cho mã nhân viên này đã tồn tại.";
        }

        dsNhanVien.add(nv);
        String loiTaiKhoan = taiKhoanBUS.themTaiKhoan(tk);
        if (loiTaiKhoan != null) {
            dsNhanVien.remove(nv);
            return loiTaiKhoan;
        }
        return null;
    }

    /** Cập nhật thông tin nhân viên. Mã nhân viên không được đổi khi sửa. */
    public String suaNhanVien(NhanVienDTO nv) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền sửa nhân viên.";
        }
        if (nv == null) {
            return "Thông tin nhân viên không hợp lệ.";
        }

        NhanVienDTO cu = timTheoMa(nv.getMaNV());
        if (cu == null) {
            return "Không tìm thấy nhân viên cần sửa.";
        }

        dsNhanVien.remove(cu);
        dsNhanVien.add(nv);

        // Cập nhật lại phiên nếu đang sửa chính người đăng nhập
        if (Auth.getNguoiDung() != null
                && Auth.getNguoiDung().getMaNV().equalsIgnoreCase(nv.getMaNV())) {
            Auth.dangNhap(nv);
        }
        return null;
    }

    /** Xóa nhân viên và tài khoản. Không cho xóa tài khoản đang đăng nhập. */
    public String xoaNhanVien(String maNV) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền xóa nhân viên.";
        }

        NhanVienDTO nv = timTheoMa(maNV);
        if (nv == null) {
            return "Không tìm thấy nhân viên cần xóa.";
        }
        if (Auth.getNguoiDung() != null
                && Auth.getNguoiDung().getMaNV().equalsIgnoreCase(nv.getMaNV())) {
            return "Không thể xóa tài khoản đang đăng nhập.";
        }

        dsNhanVien.remove(nv);
        taiKhoanBUS.xoaTaiKhoan(nv.getMaNV());
        return null;
    }

    // ==================== KHU VỰC ĐẶT LẠI MẬT KHẨU ====================
    /** Đặt lại mật khẩu theo email nhân viên. */
    public String datLaiMatKhau(String email, String matKhauMoi) {
        String thuDienTu = chuanHoa(email);
        NhanVienDTO nv = timTheoEmail(thuDienTu);
        if (nv == null) {
            return "Không tìm thấy nhân viên với email này.";
        }
        return taiKhoanBUS.doiMatKhau(nv.getMaNV(), matKhauMoi);
    }

    /** Lưu danh sách vào CSDL. Hiện dữ liệu vẫn nằm trong bộ nhớ. */
    public boolean luuVaoCSDL() {
        return true;
    }

    private NhanVienDTO timTheoEmail(String email) {
        String thuDienTu = chuanHoa(email);
        for (NhanVienDTO nv : dsNhanVien) {
            if (nv.getEmail().equalsIgnoreCase(thuDienTu)) {
                return nv;
            }
        }
        return null;
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }

    // ==================== KHU VỰC DỮ LIỆU MẪU (TẠM THỜI) ====================
    /**
     * Nạp 2 nhân viên mẫu cùng tài khoản của họ để test đăng nhập.
     * DTO do giao diện tạo và truyền vào, BUS chỉ lưu dữ liệu.
     */
    public void napDuLieuMau(NhanVienDTO quanLy, NhanVienDTO thuNgan,
            TaiKhoanDTO taiKhoanQuanLy, TaiKhoanDTO taiKhoanThuNgan) {
        if (laySoLuongNhanVien() > 0 || taiKhoanBUS.laySoLuongTaiKhoan() > 0) {
            return;
        }
        if (quanLy == null || thuNgan == null
                || taiKhoanQuanLy == null || taiKhoanThuNgan == null) {
            return;
        }

        dsNhanVien.add(quanLy);
        dsNhanVien.add(thuNgan);
        taiKhoanBUS.themTaiKhoan(taiKhoanQuanLy);
        taiKhoanBUS.themTaiKhoan(taiKhoanThuNgan);
    }
}
