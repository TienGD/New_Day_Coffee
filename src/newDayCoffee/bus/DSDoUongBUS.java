package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.DSDoUongDTO;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.Validator;

/**
 * Xử lý nghiệp vụ trên danh sách đồ uống.
 *
 * Dữ liệu hiện được lưu trong bộ nhớ thông qua DSDoUongDTO.
 * Khi DAO hoàn thiện, chỉ cần thay phần luuVaoCSDL() là có thể nối CSDL.
 */
public class DSDoUongBUS {

    // ==================== KHU VỰC SINGLETON ====================
    private static DSDoUongBUS instance;

    public static synchronized DSDoUongBUS getInstance() {
        if (instance == null) {
            instance = new DSDoUongBUS();
        }
        return instance;
    }

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final DSDoUongDTO dsDoUong;
    private final DoUongBUS doUongBUS;

    private DSDoUongBUS() {
        dsDoUong = new DSDoUongDTO();
        doUongBUS = new DoUongBUS();
        taoDuLieuMau();
    }

    /** Dữ liệu mẫu để màn hình bán hàng và quản lí thực đơn có dữ liệu ngay. */
    private void taoDuLieuMau() {
        themMau("cof01", "Cà phê đen", 15000, "Đậm đà, đánh thức ngày mới");
        themMau("cof02", "Bạc sỉu", 20000, "Cà phê sữa béo nhẹ");
        themMau("cof03", "Cà phê sữa", 18000, "Cà phê truyền thống");
        themMau("tea01", "Trà Đào", 25000, "Trà đào thơm mát");
        themMau("tea02", "Trà Mãng Cầu", 30000, "Chua ngọt tự nhiên");
        themMau("tea03", "Trà Vải", 28000, "Trà vải thanh mát");
        themMau("mil01", "Trà Sữa Thái", 25000, "Trà sữa đậm vị Thái");
        themMau("mil02", "Trà Sữa Trân Châu", 30000, "Trân châu dai mềm");
        themMau("bak01", "Bánh Tiramisu", 20000, "Bánh ngọt vị cà phê");
        themMau("bak02", "Bánh Flan", 15000, "Bánh flan mềm mịn");
    }

    private void themMau(String maMon, String tenMon, double donGia, String moTa) {
        dsDoUong.them(new DoUongDTO(maMon, tenMon, donGia, moTa));
    }

    // ==================== KHU VỰC ĐỌC / TÌM KIẾM ====================
    /** Danh sách món chưa bị xóa, dùng cho bảng quản lí và màn hình bán hàng. */
    public List<DoUongDTO> layDanhSachHienThi() {
        return new ArrayList<>(dsDoUong.getDsHienThi());
    }

    public DoUongDTO timTheoMa(String maMon) {
        return dsDoUong.timTheoMa(Validator.chuanHoa(maMon));
    }

    /** Tìm theo mã hoặc tên (không phân biệt hoa thường). */
    public List<DoUongDTO> timKiem(String tuKhoa) {
        String khoa = Validator.chuanHoa(tuKhoa).toLowerCase();
        List<DoUongDTO> ketQua = new ArrayList<>();
        for (DoUongDTO du : dsDoUong.getDsHienThi()) {
            boolean dungMa = Validator.chuanHoa(du.getMaMon()).toLowerCase().contains(khoa);
            boolean dungTen = Validator.chuanHoa(du.getTenMon()).toLowerCase().contains(khoa);
            if (khoa.isEmpty() || dungMa || dungTen) {
                ketQua.add(du);
            }
        }
        return ketQua;
    }

    /**
     * Lọc theo nhóm món.
     * Nhóm được xác định dựa trên tiền tố mã món:
     * cof = Cà phê, tea = Trà, mil = Trà sữa, bak = Bánh.
     */
    public List<DoUongDTO> locTheoNhom(String nhom) {
        String n = Validator.chuanHoa(nhom).toLowerCase();
        List<DoUongDTO> ketQua = new ArrayList<>();
        for (DoUongDTO du : dsDoUong.getDsHienThi()) {
            String ma = Validator.chuanHoa(du.getMaMon()).toLowerCase();
            boolean thoa;
            if (n.isEmpty() || n.equals("tất cả")) {
                thoa = true;
            } else if (n.contains("cà phê") || n.contains("cafe") || n.equals("cof")) {
                thoa = ma.startsWith("cof");
            } else if (n.contains("trà sữa") || n.equals("mil")) {
                thoa = ma.startsWith("mil");
            } else if (n.equals("trà") || n.equals("tea")) {
                thoa = ma.startsWith("tea");
            } else if (n.contains("bánh") || n.equals("bak")) {
                thoa = ma.startsWith("bak");
            } else {
                thoa = true;
            }
            if (thoa) {
                ketQua.add(du);
            }
        }
        return ketQua;
    }

    /** Kết hợp lọc nhóm và tìm kiếm trên cùng một danh sách. */
    public List<DoUongDTO> locVaTim(String tuKhoa, String nhom) {
        String khoa = Validator.chuanHoa(tuKhoa).toLowerCase();
        List<DoUongDTO> ketQua = new ArrayList<>();
        for (DoUongDTO du : locTheoNhom(nhom)) {
            boolean dungMa = Validator.chuanHoa(du.getMaMon()).toLowerCase().contains(khoa);
            boolean dungTen = Validator.chuanHoa(du.getTenMon()).toLowerCase().contains(khoa);
            if (khoa.isEmpty() || dungMa || dungTen) {
                ketQua.add(du);
            }
        }
        return ketQua;
    }

    // ==================== KHU VỰC THÊM / SỬA / XÓA ====================
    /**
     * Thêm món từ dữ liệu form.
     *
     * @return null nếu thành công, ngược lại là thông báo lỗi.
     */
    public String themDoUong(String maMon, String tenMon, String donGiaText) {
        if (!Auth.isManager()) {
            return "Chỉ quản lí mới có quyền thêm món.";
        }
        String loi = doUongBUS.kiemTraThongTin(maMon, tenMon, donGiaText);
        if (loi != null) {
            return loi;
        }
        DoUongDTO du = doUongBUS.taoDoUong(maMon, tenMon, donGiaText);
        return themDoUong(du);
    }

    /** Thêm món từ DTO đã có sẵn. */
    public String themDoUong(DoUongDTO du) {
        if (!Auth.isManager()) {
            return "Chỉ quản lí mới có quyền thêm món.";
        }
        if (du == null) {
            return "Thông tin món không hợp lệ.";
        }
        String loi = doUongBUS.kiemTraThongTin(du.getMaMon(), du.getTenMon(),
                DoUongBUS.dinhDangGiaNhap(du.getDonGia()));
        if (loi != null) {
            return loi;
        }
        if (dsDoUong.kiemTraTrungMa(du.getMaMon())) {
            return "Mã món đã tồn tại.";
        }
        if (!dsDoUong.them(du)) {
            return "Không thể thêm món.";
        }
        dsDoUong.luuVaoCSDL();
        return null;
    }

    /** Sửa thông tin món (không cho đổi mã món). */
    public String suaDoUong(String maMon, String tenMon, String donGiaText) {
        if (!Auth.isManager()) {
            return "Chỉ quản lí mới có quyền sửa món.";
        }
        String loi = doUongBUS.kiemTraThongTin(maMon, tenMon, donGiaText);
        if (loi != null) {
            return loi;
        }
        DoUongDTO du = doUongBUS.taoDoUong(maMon, tenMon, donGiaText);
        if (du == null || dsDoUong.timTheoMa(du.getMaMon()) == null) {
            return "Không tìm thấy món cần sửa.";
        }
        if (!dsDoUong.sua(du)) {
            return "Không thể cập nhật món.";
        }
        dsDoUong.luuVaoCSDL();
        return null;
    }

    /** Xóa mềm món khỏi danh sách hiển thị. */
    public String xoaDoUong(String maMon) {
        if (!Auth.isManager()) {
            return "Chỉ quản lí mới có quyền xóa món.";
        }
        String loi = doUongBUS.kiemTraMaMon(maMon);
        if (loi != null) {
            return loi;
        }
        if (dsDoUong.timTheoMa(maMon) == null) {
            return "Không tìm thấy món cần xóa.";
        }
        if (!dsDoUong.xoaMem(maMon)) {
            return "Không thể xóa món.";
        }
        dsDoUong.luuVaoCSDL();
        return null;
    }

    /**
     * Lưu toàn bộ danh sách xuống CSDL.
     * Hiện DTO trả về true vì DAO chưa được hoàn thiện.
     */
    public boolean luuVaoCSDL() {
        if (!Auth.isManager()) {
            return false;
        }
        return dsDoUong.luuVaoCSDL();
    }
}
