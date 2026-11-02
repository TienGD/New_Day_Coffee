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
 * Lớp này là cầu nối giữa giao diện và dữ liệu:
 *  - Gọi Validator để kiểm tra dữ liệu.
 *  - Gọi TaiKhoanBUS để kiểm tra tài khoản đăng nhập.
 *  - Gọi Auth để lưu / xóa phiên đăng nhập.
 */
public class NhanVienBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<NhanVienDTO> dsNhanVien;
    private final DSTaiKhoanBUS taiKhoanBUS;

    /**
     * Nhận TaiKhoanBUS từ nơi tạo (LoginFrame/MainFrame) để mọi màn hình
     * dùng chung một danh sách tài khoản, không cần singleton.
     */
    public NhanVienBUS(DSTaiKhoanBUS taiKhoanBUS) {
        this.taiKhoanBUS = taiKhoanBUS;
        this.dsNhanVien = new ArrayList<>();
        taoDuLieuMau();
    }

    /** Dữ liệu mẫu để hệ thống chạy được ngay khi chưa nối CSDL. */
    private void taoDuLieuMau() {
        themNhanVienMau("NV01", "Lâm Nguyễn Như Ý", LocalDate.of(2000, 1, 1),
                "0901234567", "lam@gmail.com", "Quản lí", "Nữ");
        themNhanVienMau("NV02", "Nguyễn Quang Toàn", LocalDate.of(2002, 3, 15),
                "0912345678", "toan@gmail.com", "Thu ngân", "Nam");
        themNhanVienMau("NV03", "Trần Xuân Tiền", LocalDate.of(2001, 7, 20),
                "0923456789", "tien@gmail.com", "Pha chế", "Nam");
        themNhanVienMau("NV04", "Trần Đăng Khoa", LocalDate.of(2003, 11, 2),
                "0934567890", "", "Pha chế", "Nam");
    }

    private void themNhanVienMau(String maNV, String tenNV, LocalDate ngaySinh,
            String sdt, String email, String chucVu, String gioiTinh) {
        dsNhanVien.add(new NhanVienDTO(maNV, tenNV, ngaySinh, sdt, email, chucVu, gioiTinh, "123456"));
    }

    // ==================== KHU VỰC ĐĂNG NHẬP / ĐĂNG XUẤT ====================
    /**
     * Kiểm tra tài khoản, nếu đúng thì lưu nhân viên vào Auth.
     *
     * @return null nếu đăng nhập thành công, ngược lại là thông báo lỗi.
     */
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
    /** Trả về bản sao danh sách để giao diện không sửa trực tiếp dữ liệu gốc. */
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

    /**
     * Tìm nhân viên theo mã và/hoặc tên.
     * Ô nào bỏ trống thì không xét điều kiện tương ứng.
     */
    public List<NhanVienDTO> timKiem(String maNV, String tenNV) {
        List<NhanVienDTO> ketQua = new ArrayList<>();
        String maTim = Validator.chuanHoa(maNV).toLowerCase();
        String tenTim = Validator.chuanHoa(tenNV).toLowerCase();

        for (NhanVienDTO nv : dsNhanVien) {
            boolean dungMa = maTim.isEmpty()
                    || Validator.chuanHoa(nv.getMaNV()).toLowerCase().contains(maTim);
            boolean dungTen = tenTim.isEmpty()
                    || Validator.chuanHoa(nv.getTenNV()).toLowerCase().contains(tenTim);
            if (dungMa && dungTen) {
                ketQua.add(nv);
            }
        }
        return ketQua;
    }

    // ==================== KHU VỰC CHUYỂN ĐỔI TỪ FORM ====================
    /**
     * Tạo DTO từ dữ liệu người dùng nhập trên form.
     * Ngày sinh được đọc qua Validator; nếu sai định dạng thì giá trị ngày là null
     * và Validator.kiemTraNhanVien sẽ trả về thông báo lỗi tương ứng.
     */
    public NhanVienDTO taoNhanVienTuForm(String maNV, String tenNV, String ngaySinhText,
            String sdt, String email, String chucVu, String gioiTinh) {
        LocalDate ngaySinh = Validator.docNgay(ngaySinhText);
        return new NhanVienDTO(Validator.chuanHoa(maNV), Validator.chuanHoa(tenNV), ngaySinh,
                Validator.chuanHoa(sdt), Validator.chuanHoa(email),
                Validator.chuanHoa(chucVu), Validator.chuanHoa(gioiTinh));
    }

    /** Định dạng ngày sinh để hiển thị trên bảng: dd-MM-yyyy. */
    public static String dinhDangNgay(LocalDate ngaySinh) {
        return Validator.dinhDangNgay(ngaySinh);
    }

    // ==================== KHU VỰC THÊM / SỬA / XÓA ====================
    /**
     * Thêm nhân viên mới và tạo tài khoản tương ứng.
     *
     * @return null nếu thành công, ngược lại là thông báo lỗi.
     */
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

        if (Validator.rong(nv.getMatKhau())) {
            nv.setMatKhau("123456");
        }

        dsNhanVien.add(nv);
        if (!taiKhoanBUS.themTaiKhoan(nv.getMaNV(), nv.getMatKhau())) {
            dsNhanVien.remove(nv);
            return "Không thể tạo tài khoản cho nhân viên.";
        }
        return null;
    }

    /**
     * Cập nhật thông tin nhân viên.
     * Mã nhân viên dùng để tìm bản ghi cũ nên không được đổi khi sửa.
     */
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

        // Giữ nguyên mật khẩu cũ vì form nhân viên không có chức năng đổi mật khẩu
        nv.setMatKhau(cu.getMatKhau());
        dsNhanVien.remove(cu);
        dsNhanVien.add(nv);

        // Nếu sửa chính người đang đăng nhập thì cập nhật lại phiên làm việc
        if (Auth.getNguoiDung() != null
                && Auth.getNguoiDung().getMaNV().equalsIgnoreCase(nv.getMaNV())) {
            Auth.dangNhap(nv);
        }
        return null;
    }

    /** Xóa nhân viên và tài khoản. Không cho phép xóa chính tài khoản đang đăng nhập. */
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
}
