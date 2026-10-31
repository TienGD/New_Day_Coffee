package newDayCoffee.bus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.DSNhanVienDTO;
import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.Validator;

/**
 * Xử lý nghiệp vụ nhân viên và đăng nhập.
 *
 * Lớp này là cầu nối giữa giao diện và dữ liệu:
 *  - Gọi Validator để kiểm tra dữ liệu.
 *  - Gọi Auth để lưu / xóa phiên đăng nhập.
 *  - Quản lý danh sách nhân viên trong bộ nhớ (DAO hiện chưa hoàn thiện).
 */
public class NhanVienBUS {

    // ==================== KHU VỰC SINGLETON ====================
    private static NhanVienBUS instance;

    /** Các panel dùng chung một đối tượng BUS để dữ liệu không bị lệch nhau. */
    public static synchronized NhanVienBUS getInstance() {
        if (instance == null) {
            instance = new NhanVienBUS();
        }
        return instance;
    }

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final DSNhanVienDTO dsNhanVien;

    private NhanVienBUS() {
        dsNhanVien = new DSNhanVienDTO();
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
        NhanVienDTO nv = new NhanVienDTO(maNV, tenNV, ngaySinh, sdt, email, chucVu, gioiTinh);
        dsNhanVien.them(nv);
    }

    // ==================== KHU VỰC ĐĂNG NHẬP / ĐĂNG XUẤT ====================
    /**
     * Kiểm tra thông tin đăng nhập, nếu đúng thì lưu phiên vào Auth.
     *
     * @return null nếu đăng nhập thành công, ngược lại là thông báo lỗi.
     */
    public String dangNhap(String maNV, String matKhau) {
        String loi = Validator.kiemTraDangNhap(maNV, matKhau);
        if (loi != null) {
            return loi;
        }

        NhanVienDTO nv = dsNhanVien.kiemTraDangNhap(Validator.chuanHoa(maNV), matKhau);
        if (nv == null) {
            return "Mã nhân viên hoặc mật khẩu không đúng.";
        }

        Auth.login(nv);
        return null;
    }

    /** Kết thúc phiên đăng nhập hiện tại. */
    public void dangXuat() {
        Auth.logout();
    }

    /** Kiểm tra người đang đăng nhập có phải quản lí hay không. */
    public boolean laQuanLy() {
        return Auth.isManager();
    }

    // ==================== KHU VỰC ĐỌC DỮ LIỆU ====================
    /** Trả về bản sao danh sách để giao diện không sửa trực tiếp dữ liệu gốc. */
    public List<NhanVienDTO> layDanhSachNhanVien() {
        return new ArrayList<>(dsNhanVien.getDsNhanVien());
    }

    public NhanVienDTO timTheoMa(String maNV) {
        return dsNhanVien.timTheoMa(Validator.chuanHoa(maNV));
    }

    public int laySoLuongNhanVien() {
        return dsNhanVien.getDsNhanVien().size();
    }

    /**
     * Tìm nhân viên theo mã và/hoặc tên.
     * Ô nào bỏ trống thì không xét điều kiện tương ứng.
     */
    public List<NhanVienDTO> timKiem(String maNV, String tenNV) {
        List<NhanVienDTO> ketQua = new ArrayList<>();
        String maTim = Validator.chuanHoa(maNV).toLowerCase();
        String tenTim = Validator.chuanHoa(tenNV).toLowerCase();

        for (NhanVienDTO nv : dsNhanVien.getDsNhanVien()) {
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

    // ==================== KHU VỰC THÊM / SỬA / XÓA ====================
    /**
     * Thêm nhân viên mới. Mật khẩu mặc định là 123456 nếu chưa nhập.
     *
     * @return null nếu thành công, ngược lại là thông báo lỗi.
     */
    public String themNhanVien(NhanVienDTO nv) {
        if (!Auth.isManager()) {
            return "Chỉ quản lí mới có quyền thêm nhân viên.";
        }
        String loi = Validator.kiemTraNhanVien(nv);
        if (loi != null) {
            return loi;
        }
        if (dsNhanVien.kiemTraTrungMa(nv.getMaNV())) {
            return "Mã nhân viên đã tồn tại.";
        }
        if (Validator.rong(nv.getMatKhau())) {
            nv.setMatKhau("123456");
        }
        if (!dsNhanVien.them(nv)) {
            return "Không thể thêm nhân viên.";
        }
        dsNhanVien.luuVaoCSDL();
        return null;
    }

    /**
     * Cập nhật thông tin nhân viên. Giữ nguyên mật khẩu cũ nếu DTO không truyền mật khẩu.
     */
    public String suaNhanVien(NhanVienDTO nv) {
        if (!Auth.isManager()) {
            return "Chỉ quản lí mới có quyền sửa nhân viên.";
        }
        String loi = Validator.kiemTraNhanVien(nv);
        if (loi != null) {
            return loi;
        }
        NhanVienDTO cu = dsNhanVien.timTheoMa(nv.getMaNV());
        if (cu == null) {
            return "Không tìm thấy nhân viên cần sửa.";
        }
        if (Validator.rong(nv.getMatKhau())) {
            nv.setMatKhau(cu.getMatKhau());
        }
        if (!dsNhanVien.sua(nv)) {
            return "Không thể cập nhật nhân viên.";
        }
        dsNhanVien.luuVaoCSDL();
        return null;
    }

    /** Xóa nhân viên theo mã. Không cho phép xóa chính tài khoản đang đăng nhập. */
    public String xoaNhanVien(String maNV) {
        if (!Auth.isManager()) {
            return "Chỉ quản lí mới có quyền xóa nhân viên.";
        }
        NhanVienDTO nv = dsNhanVien.timTheoMa(Validator.chuanHoa(maNV));
        if (nv == null) {
            return "Không tìm thấy nhân viên cần xóa.";
        }
        if (Auth.getCurrentUser() != null
                && Auth.getCurrentUser().getMaNV().equalsIgnoreCase(nv.getMaNV())) {
            return "Không thể xóa tài khoản đang đăng nhập.";
        }
        if (!dsNhanVien.xoa(maNV)) {
            return "Không thể xóa nhân viên.";
        }
        dsNhanVien.luuVaoCSDL();
        return null;
    }
}
