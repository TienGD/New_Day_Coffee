package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.Validator;

/**
 * Xử lý nghiệp vụ trên danh sách đồ uống.
 *
 * Dữ liệu hiện được lưu trong bộ nhớ của đối tượng DSDoUongBUS.
 * Khi DAO hoàn thiện, chỉ cần thay phần luuVaoCSDL() là có thể nối CSDL.
 */
public class DSDoUongBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<DoUongDTO> dsDoUong;
    private final DoUongBUS doUongBUS;

    public DSDoUongBUS() {
        dsDoUong = new ArrayList<>();
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
        dsDoUong.add(new DoUongDTO(maMon, tenMon, donGia, moTa));
    }

    // ==================== KHU VỰC ĐỌC / TÌM KIẾM ====================
    /** Trả về bản sao danh sách món để giao diện không sửa trực tiếp dữ liệu gốc. */
    public List<DoUongDTO> layDanhSach() {
        return new ArrayList<>(dsDoUong);
    }

    public DoUongDTO timTheoMa(String maMon) {
        String ma = Validator.chuanHoa(maMon);
        for (DoUongDTO du : dsDoUong) {
            if (du.getMaMon().equalsIgnoreCase(ma)) {
                return du;
            }
        }
        return null;
    }

    /** Xác định nhóm món dựa vào tiền tố mã món. */
    public String layNhom(DoUongDTO du) {
        if (du == null) {
            return "Khác";
        }
        String ma = Validator.chuanHoa(du.getMaMon()).toLowerCase();
        if (ma.startsWith("cof")) {
            return "Cà phê";
        }
        if (ma.startsWith("tea")) {
            return "Trà";
        }
        if (ma.startsWith("mil")) {
            return "Trà sữa";
        }
        if (ma.startsWith("bak")) {
            return "Bánh";
        }
        return "Khác";
    }

    /** Tìm theo mã hoặc tên (không phân biệt hoa thường). */
    public List<DoUongDTO> timKiem(String tuKhoa) {
        String khoa = Validator.chuanHoa(tuKhoa).toLowerCase();
        List<DoUongDTO> ketQua = new ArrayList<>();
        for (DoUongDTO du : dsDoUong) {
            boolean dungMa = Validator.chuanHoa(du.getMaMon()).toLowerCase().contains(khoa);
            boolean dungTen = Validator.chuanHoa(du.getTenMon()).toLowerCase().contains(khoa);
            if (khoa.isEmpty() || dungMa || dungTen) {
                ketQua.add(du);
            }
        }
        return ketQua;
    }

    /** Lọc theo nhóm món. Nhóm "Tất cả" trả về toàn bộ danh sách. */
    public List<DoUongDTO> locTheoNhom(String nhom) {
        String nhomChuan = chuanHoaNhom(nhom);
        List<DoUongDTO> ketQua = new ArrayList<>();
        for (DoUongDTO du : dsDoUong) {
            if (nhomChuan.equals("Tất cả") || layNhom(du).equals(nhomChuan)) {
                ketQua.add(du);
            }
        }
        return ketQua;
    }

    /** Kết hợp tìm theo từ khóa và lọc theo nhóm trong một lần duyệt. */
    public List<DoUongDTO> locVaTim(String tuKhoa, String nhom) {
        String khoa = Validator.chuanHoa(tuKhoa).toLowerCase();
        String nhomChuan = chuanHoaNhom(nhom);
        List<DoUongDTO> ketQua = new ArrayList<>();

        for (DoUongDTO du : dsDoUong) {
            boolean dungNhom = nhomChuan.equals("Tất cả") || layNhom(du).equals(nhomChuan);
            boolean dungMa = Validator.chuanHoa(du.getMaMon()).toLowerCase().contains(khoa);
            boolean dungTen = Validator.chuanHoa(du.getTenMon()).toLowerCase().contains(khoa);
            boolean dungTuKhoa = khoa.isEmpty() || dungMa || dungTen;
            if (dungNhom && dungTuKhoa) {
                ketQua.add(du);
            }
        }
        return ketQua;
    }

    private String chuanHoaNhom(String nhom) {
        String n = Validator.chuanHoa(nhom);
        if (n.isEmpty() || n.equalsIgnoreCase("Tất cả")) {
            return "Tất cả";
        }
        if (n.equalsIgnoreCase("Cafe")) {
            return "Cà phê";
        }
        if (n.equalsIgnoreCase("Tra")) {
            return "Trà";
        }
        return n;
    }

    // ==================== KHU VỰC THÊM / SỬA / XÓA ====================
    /**
     * Thêm món mới sau khi kiểm tra dữ liệu và mã trùng.
     *
     * @return null nếu thành công, ngược lại là thông báo lỗi.
     */
    public String themDoUong(String maMon, String tenMon, String donGiaText) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền thêm món.";
        }
        String loi = doUongBUS.kiemTraThongTin(maMon, tenMon, donGiaText);
        if (loi != null) {
            return loi;
        }
        if (timTheoMa(maMon) != null) {
            return "Mã món đã tồn tại.";
        }

        DoUongDTO du = doUongBUS.taoDoUong(maMon, tenMon, donGiaText);
        if (du == null) {
            return "Không thể tạo món mới.";
        }
        dsDoUong.add(du);
        return null;
    }

    /** Cập nhật tên và giá của món đã có. */
    public String suaDoUong(String maMon, String tenMon, String donGiaText) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền sửa món.";
        }
        String loi = doUongBUS.kiemTraThongTin(maMon, tenMon, donGiaText);
        if (loi != null) {
            return loi;
        }

        DoUongDTO du = timTheoMa(maMon);
        if (du == null) {
            return "Không tìm thấy món cần sửa.";
        }
        du.setTenMon(Validator.chuanHoa(tenMon));
        du.setDonGia(Validator.docSoTien(donGiaText));
        return null;
    }

    /** Xóa món theo mã. */
    public String xoaDoUong(String maMon) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền xóa món.";
        }
        if (doUongBUS.kiemTraMaMon(maMon) != null) {
            return "Vui lòng nhập mã món cần xóa.";
        }
        DoUongDTO du = timTheoMa(maMon);
        if (du == null) {
            return "Không tìm thấy món cần xóa.";
        }
        dsDoUong.remove(du);
        return null;
    }

    /**
     * Lưu danh sách vào CSDL.
     * Hiện DAO chưa hoàn thiện nên dữ liệu vẫn nằm trong bộ nhớ.
     */
    public boolean luuVaoCSDL() {
        return true;
    }
}
