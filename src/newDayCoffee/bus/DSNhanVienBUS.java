package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.utils.Auth;

/**
 * Xử lý nghiệp vụ trên danh sách nhân viên.
 *
 * Lớp này chỉ quản lý dữ liệu nhân viên (độc lập với DSTaiKhoanBUS).
 * Việc điều phối nghiệp vụ liên quan cả nhân viên và tài khoản nằm ở NhanVienTaiKhoanBUS.
 */
public class DSNhanVienBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<NhanVienDTO> dsNhanVien;

    public DSNhanVienBUS() {
        this.dsNhanVien = new ArrayList<>();
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
        if (ma.isEmpty()) {
            return null;
        }
        for (NhanVienDTO nv : dsNhanVien) {
            if (chuanHoa(nv.getMaNV()).equalsIgnoreCase(ma)) {
                return nv;
            }
        }
        return null;
    }

    public NhanVienDTO timTheoEmail(String email) {
        String thuDienTu = chuanHoa(email);
        if (thuDienTu.isEmpty()) {
            return null;
        }
        for (NhanVienDTO nv : dsNhanVien) {
            if (chuanHoa(nv.getEmail()).equalsIgnoreCase(thuDienTu)) {
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
    /** Thêm nhân viên mới. DTO do giao diện tạo và truyền vào. */
    public String themNhanVien(NhanVienDTO nv) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền thêm nhân viên.";
        }
        if (nv == null || chuanHoa(nv.getMaNV()).isEmpty()) {
            return "Thông tin nhân viên không hợp lệ.";
        }
        if (timTheoMa(nv.getMaNV()) != null) {
            return "Mã nhân viên đã tồn tại.";
        }

        dsNhanVien.add(nv);
        return null;
    }

    /** Cập nhật thông tin trực tiếp trên đối tượng nhân viên cũ (giữ nguyên vị trí & tham chiếu). */
    public String suaNhanVien(NhanVienDTO nv) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền sửa nhân viên.";
        }
        if (nv == null || chuanHoa(nv.getMaNV()).isEmpty()) {
            return "Thông tin nhân viên không hợp lệ.";
        }

        NhanVienDTO cu = timTheoMa(nv.getMaNV());
        if (cu == null) {
            return "Không tìm thấy nhân viên cần sửa.";
        }

        cu.setTenNV(nv.getTenNV());
        cu.setNgaySinh(nv.getNgaySinh());
        cu.setSdt(nv.getSdt());
        cu.setEmail(nv.getEmail());
        cu.setChucVu(nv.getChucVu());
        cu.setGioiTinh(nv.getGioiTinh());

        // Cập nhật lại phiên nếu đang sửa chính người đăng nhập
        if (Auth.getNguoiDung() != null
                && chuanHoa(Auth.getNguoiDung().getMaNV()).equalsIgnoreCase(chuanHoa(cu.getMaNV()))) {
            Auth.dangNhap(cu);
        }
        return null;
    }

    /** Xóa nhân viên khỏi danh sách. Không xóa dây chuyền tài khoản. */
    public String xoaNhanVien(String maNV) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền xóa nhân viên.";
        }

        NhanVienDTO nv = timTheoMa(maNV);
        if (nv == null) {
            return "Không tìm thấy nhân viên cần xóa.";
        }
        if (Auth.getNguoiDung() != null
                && chuanHoa(Auth.getNguoiDung().getMaNV()).equalsIgnoreCase(chuanHoa(nv.getMaNV()))) {
            return "Không thể xóa nhân viên đang đăng nhập.";
        }

        dsNhanVien.remove(nv);
        return null;
    }

    /** Lưu danh sách vào CSDL. Hiện dữ liệu vẫn nằm trong bộ nhớ. */
    public boolean luuVaoCSDL() {
        return true;
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }

    // ==================== KHU VỰC DỮ LIỆU MẪU (TẠM THỜI) ====================
    /** Nạp danh sách nhân viên mẫu khi dữ liệu còn trống. */
    public boolean napDuLieuMau(List<NhanVienDTO> dsNhanVienMau) {
        if (laySoLuongNhanVien() > 0 || dsNhanVienMau == null || dsNhanVienMau.isEmpty()) {
            return false;
        }
        List<String> dsMaDaCo = new ArrayList<>();
        for (NhanVienDTO nhanVien : dsNhanVienMau) {
            if (nhanVien == null) {
                return false;
            }
            String maNV = chuanHoa(nhanVien.getMaNV()).toLowerCase();
            if (maNV.isEmpty() || dsMaDaCo.contains(maNV)) {
                return false;
            }
            dsMaDaCo.add(maNV);
        }
        dsNhanVien.addAll(dsNhanVienMau);
        return true;
    }
}
