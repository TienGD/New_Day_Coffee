package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dao.NhanVienDAO;
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
    private final List<NhanVienDTO> dsThemMoi;
    private final List<NhanVienDTO> dsCapNhat;
    private final List<String> dsMaXoa;

    public DSNhanVienBUS() {
        this.dsNhanVien = new ArrayList<>();
        this.dsThemMoi = new ArrayList<>();
        this.dsCapNhat = new ArrayList<>();
        this.dsMaXoa = new ArrayList<>();
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

    public NhanVienDTO timTheoSDT(String sdt) {
        String soDT = chuanHoa(sdt);
        if (soDT.isEmpty()) {
            return null;
        }
        for (NhanVienDTO nv : dsNhanVien) {
            if (chuanHoa(nv.getSdt()).equalsIgnoreCase(soDT)) {
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
        String ma = chuanHoa(nv.getMaNV()).toLowerCase();
        String chucVu = chuanHoa(nv.getChucVu());
        boolean laChucVuQuanLy = "Quản lí".equalsIgnoreCase(chucVu) || "Quản lý".equalsIgnoreCase(chucVu);
        if (ma.startsWith("ql") && !chucVu.isEmpty() && !laChucVuQuanLy) {
            return "Mã nhân viên bắt đầu bằng ql phải có chức vụ Quản lí.";
        }
        if (ma.startsWith("nv") && laChucVuQuanLy) {
            return "Mã nhân viên bắt đầu bằng nv không được có chức vụ Quản lí.";
        }
        if (timTheoMa(nv.getMaNV()) != null) {
            return "Mã nhân viên đã tồn tại.";
        }
        String sdt = chuanHoa(nv.getSdt());
        if (!sdt.isEmpty() && timTheoSDT(sdt) != null) {
            return "Số điện thoại đã tồn tại.";
        }
        String email = chuanHoa(nv.getEmail());
        if (!email.isEmpty() && timTheoEmail(email) != null) {
            return "Email nhân viên đã tồn tại.";
        }

        dsNhanVien.add(nv);
        ghiNhanThemMoi(nv);
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

        String ma = chuanHoa(nv.getMaNV()).toLowerCase();
        String chucVu = chuanHoa(nv.getChucVu());
        boolean laChucVuQuanLy = "Quản lí".equalsIgnoreCase(chucVu) || "Quản lý".equalsIgnoreCase(chucVu);
        if (ma.startsWith("ql") && !chucVu.isEmpty() && !laChucVuQuanLy) {
            return "Mã nhân viên bắt đầu bằng ql phải có chức vụ Quản lí.";
        }
        if (ma.startsWith("nv") && laChucVuQuanLy) {
            return "Mã nhân viên bắt đầu bằng nv không được có chức vụ Quản lí.";
        }

        String sdt = chuanHoa(nv.getSdt());
        if (!sdt.isEmpty()) {
            NhanVienDTO trungSDT = timTheoSDT(sdt);
            if (trungSDT != null && !chuanHoa(trungSDT.getMaNV()).equalsIgnoreCase(ma)) {
                return "Số điện thoại đã tồn tại ở nhân viên khác.";
            }
        }
        String email = chuanHoa(nv.getEmail());
        if (!email.isEmpty()) {
            NhanVienDTO trungEmail = timTheoEmail(email);
            if (trungEmail != null && !chuanHoa(trungEmail.getMaNV()).equalsIgnoreCase(ma)) {
                return "Email nhân viên đã tồn tại ở nhân viên khác.";
            }
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
        ghiNhanCapNhat(cu);
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
        ghiNhanXoa(nv.getMaNV());
        return null;
    }

    private void ghiNhanThemMoi(NhanVienDTO nv) {
        String ma = chuanHoa(nv.getMaNV()).toLowerCase();
        if (dsMaXoa.remove(ma)) {
            dsCapNhat.add(nv);
        } else {
            dsThemMoi.add(nv);
        }
    }

    private void ghiNhanCapNhat(NhanVienDTO nv) {
        String ma = chuanHoa(nv.getMaNV()).toLowerCase();
        for (int i = 0; i < dsThemMoi.size(); i++) {
            if (chuanHoa(dsThemMoi.get(i).getMaNV()).equalsIgnoreCase(ma)) {
                dsThemMoi.set(i, nv);
                return;
            }
        }
        for (int i = 0; i < dsCapNhat.size(); i++) {
            if (chuanHoa(dsCapNhat.get(i).getMaNV()).equalsIgnoreCase(ma)) {
                dsCapNhat.set(i, nv);
                return;
            }
        }
        dsCapNhat.add(nv);
    }

    private void ghiNhanXoa(String maNV) {
        String ma = chuanHoa(maNV).toLowerCase();
        for (int i = dsThemMoi.size() - 1; i >= 0; i--) {
            if (chuanHoa(dsThemMoi.get(i).getMaNV()).equalsIgnoreCase(ma)) {
                dsThemMoi.remove(i);
                return;
            }
        }
        for (int i = dsCapNhat.size() - 1; i >= 0; i--) {
            if (chuanHoa(dsCapNhat.get(i).getMaNV()).equalsIgnoreCase(ma)) {
                dsCapNhat.remove(i);
            }
        }
        if (!dsMaXoa.contains(ma)) {
            dsMaXoa.add(ma);
        }
    }

    /**
     * Lưu toàn bộ thay đổi chờ lưu (thêm, sửa, xóa) xuống SQL Server thông qua NhanVienDAO
     * và đồng bộ lại danh sách nhân viên với dữ liệu thực tế trong database.
     */
    public boolean luuVaoCSDL() {
        NhanVienDAO dao = new NhanVienDAO();
        if (dsThemMoi.isEmpty() && dsCapNhat.isEmpty() && dsMaXoa.isEmpty()) {
            List<NhanVienDTO> dsHienTai = dao.getAll();
            if (dsHienTai == null || (dsHienTai.isEmpty() && !dsNhanVien.isEmpty())) {
                return false;
            }
            return true;
        }

        while (!dsThemMoi.isEmpty()) {
            NhanVienDTO nv = dsThemMoi.get(0);
            if (!dao.insert(nv)) {
                return false;
            }
            dsThemMoi.remove(0);
        }

        while (!dsCapNhat.isEmpty()) {
            NhanVienDTO nv = dsCapNhat.get(0);
            if (!dao.update(nv)) {
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

        List<NhanVienDTO> dsDongBo = dao.getAll();
        if (dsDongBo != null && !dsDongBo.isEmpty()) {
            for (NhanVienDTO nvDB : dsDongBo) {
                NhanVienDTO nvTrongBoNho = timTheoMa(nvDB.getMaNV());
                if (nvTrongBoNho != null) {
                    nvTrongBoNho.setTenNV(nvDB.getTenNV());
                    nvTrongBoNho.setNgaySinh(nvDB.getNgaySinh());
                    nvTrongBoNho.setSdt(nvDB.getSdt());
                    nvTrongBoNho.setEmail(nvDB.getEmail());
                    nvTrongBoNho.setChucVu(nvDB.getChucVu());
                    nvTrongBoNho.setGioiTinh(nvDB.getGioiTinh());
                } else {
                    dsNhanVien.add(nvDB);
                }
            }
        }
        return true;
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }

    // ==================== KHU VỰC NẠP DỮ LIỆU TỪ CSDL ====================
    /** Nạp danh sách nhân viên đọc từ CSDL khi dữ liệu trong bộ nhớ còn trống. */
    public boolean napDuLieuTuCSDL(List<NhanVienDTO> dsNhanVienDB) {
        if (laySoLuongNhanVien() > 0 || dsNhanVienDB == null || dsNhanVienDB.isEmpty()) {
            return false;
        }
        List<String> dsMaDaCo = new ArrayList<>();
        for (NhanVienDTO nhanVien : dsNhanVienDB) {
            if (nhanVien == null) {
                return false;
            }
            String maNV = chuanHoa(nhanVien.getMaNV()).toLowerCase();
            if (maNV.isEmpty() || dsMaDaCo.contains(maNV)) {
                return false;
            }
            dsMaDaCo.add(maNV);
        }
        dsNhanVien.addAll(dsNhanVienDB);
        dsThemMoi.clear();
        dsCapNhat.clear();
        dsMaXoa.clear();
        return true;
    }
}
