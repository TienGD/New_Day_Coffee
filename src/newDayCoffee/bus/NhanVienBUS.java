package newDayCoffee.bus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.Validator;

/**
 * Xử lý nghiệp vụ nhân viên và đăng nhập.
 *
 * Lớp này gọi Validator để kiểm tra dữ liệu, DSTaiKhoanBUS để quản lý
 * tài khoản và Auth để lưu / xóa phiên đăng nhập.
 */
public class NhanVienBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<NhanVienDTO> dsNhanVien;
    private final DSTaiKhoanBUS taiKhoanBUS;

    public NhanVienBUS(DSTaiKhoanBUS taiKhoanBUS) {
        this.taiKhoanBUS = taiKhoanBUS;
        this.dsNhanVien = new ArrayList<>();
    }

    // ==================== KHU VỰC ĐĂNG NHẬP / ĐĂNG XUẤT ====================
    /** Kiểm tra tài khoản, nếu đúng thì lưu nhân viên vào Auth. */
    public String dangNhap(String maNV, String matKhau) {
        String loi = taiKhoanBUS.kiemTraDangNhap(maNV, matKhau);
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
        String ma = Validator.chuanHoa(maNV);
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
        String maTim = Validator.chuanHoa(maNV).toLowerCase();
        String tenTim = Validator.chuanHoa(tenNV).toLowerCase();

        for (NhanVienDTO nv : dsNhanVien) {
            boolean dungMa = maTim.isEmpty()
                    || Validator.chuanHoa(nv.getMaNV()).toLowerCase().equals(maTim);
            boolean dungTen = tenTim.isEmpty()
                    || Validator.chuanHoa(nv.getTenNV()).toLowerCase().contains(tenTim);
            if (dungMa && dungTen) {
                ketQua.add(nv);
            }
        }
        return ketQua;
    }

    // ==================== KHU VỰC CHUYỂN ĐỔI TỪ FORM ====================
    /** Tạo DTO từ dữ liệu người dùng nhập trên form. */
    public NhanVienDTO taoNhanVienTuForm(String maNV, String tenNV, String ngaySinhText,
            String sdt, String email, String chucVu, String gioiTinh) {
        LocalDate ngaySinh = Validator.docNgay(ngaySinhText);
        return new NhanVienDTO(Validator.chuanHoa(maNV), Validator.chuanHoa(tenNV), ngaySinh,
                Validator.chuanHoa(sdt), Validator.chuanHoa(email),
                Validator.chuanHoa(chucVu), Validator.chuanHoa(gioiTinh), "");
    }

    /** Định dạng ngày sinh để hiển thị trên bảng: dd-MM-yyyy. */
    public static String dinhDangNgay(LocalDate ngaySinh) {
        return Validator.dinhDangNgay(ngaySinh);
    }

    // ==================== KHU VỰC THÊM / SỬA / XÓA ====================
    /** Thêm nhân viên mới và tạo tài khoản với mật khẩu mặc định. */
    public String themNhanVien(NhanVienDTO nv) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền thêm nhân viên.";
        }
        String loi = Validator.kiemTraNhanVien(nv);
        if (loi != null) {
            return loi;
        }
        if (timTheoMa(nv.getMaNV()) != null) {
            return "Mã nhân viên đã tồn tại.";
        }
        if (taiKhoanBUS.timTheoMa(nv.getMaNV()) != null) {
            return "Tài khoản cho mã nhân viên này đã tồn tại.";
        }

        dsNhanVien.add(nv);
        String loiTaiKhoan = taiKhoanBUS.themTaiKhoan(nv.getMaNV(), "123456");
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
        String loi = Validator.kiemTraNhanVien(nv);
        if (loi != null) {
            return loi;
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
        String thuDienTu = Validator.chuanHoa(email);
        if (thuDienTu.isEmpty()) {
            return "Vui lòng nhập email nhân viên.";
        }
        String loi = Validator.kiemTraEmail(thuDienTu);
        if (loi != null) {
            return loi;
        }
        loi = Validator.kiemTraMatKhau(matKhauMoi);
        if (loi != null) {
            return loi;
        }

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
        String thuDienTu = Validator.chuanHoa(email);
        for (NhanVienDTO nv : dsNhanVien) {
            if (nv.getEmail().equalsIgnoreCase(thuDienTu)) {
                return nv;
            }
        }
        return null;
    }

    // ==================== KHU VỰC DỮ LIỆU MẪU (TẠM THỜI) ====================
    /**
     * Thêm 2 tài khoản mẫu để test đăng nhập:
     * ql01 / 123456 (quản lí), nv01 / 123456 (thu ngân).
     */
    public void taoDuLieuMau() {
        if (laySoLuongNhanVien() > 0 || taiKhoanBUS.laySoLuongTaiKhoan() > 0) {
            return;
        }

        NhanVienDTO quanLy = new NhanVienDTO("ql01", "Nguyễn Quản Lý",
                LocalDate.of(1990, 1, 1), "0901234567", "quanly@newdaycoffee.vn",
                "Quản lí", "Nam", "123456");
        NhanVienDTO thuNgan = new NhanVienDTO("nv01", "Trần Thu Ngân",
                LocalDate.of(2000, 1, 1), "0901234568", "thungan@newdaycoffee.vn",
                "Thu ngân", "Nữ", "123456");

        dsNhanVien.add(quanLy);
        dsNhanVien.add(thuNgan);
        taiKhoanBUS.themTaiKhoan(quanLy.getMaNV(), "123456");
        taiKhoanBUS.themTaiKhoan(thuNgan.getMaNV(), "123456");
    }
}
