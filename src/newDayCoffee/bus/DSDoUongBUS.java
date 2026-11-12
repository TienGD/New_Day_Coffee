package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.utils.Auth;

/**
 * Xử lý nghiệp vụ trên danh sách đồ uống.
 *
 * Dữ liệu hiện được lưu trong bộ nhớ. Khi DAO hoàn thiện chỉ cần thay
 * phần luuVaoCSDL() là có thể nối cơ sở dữ liệu.
 * Việc kiểm tra dữ liệu form do giao diện thực hiện.
 */
public class DSDoUongBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<DoUongDTO> dsDoUong;

    public DSDoUongBUS() {
        dsDoUong = new ArrayList<>();
    }

    // ==================== KHU VỰC ĐỌC / TÌM KIẾM ====================
    /** Trả về bản sao danh sách món để giao diện không sửa dữ liệu gốc. */
    public List<DoUongDTO> layDanhSach() {
        return new ArrayList<>(dsDoUong);
    }

    public DoUongDTO timTheoMa(String maMon) {
        String ma = chuanHoa(maMon);
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
        String ma = chuanHoa(du.getMaMon()).toLowerCase();
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
        return locTheoTuKhoa(tuKhoa, "Tất cả");
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

    /** Kết hợp tìm theo từ khóa và lọc theo nhóm. */
    public List<DoUongDTO> locVaTim(String tuKhoa, String nhom) {
        return locTheoTuKhoa(tuKhoa, nhom);
    }

    /**
     * Tìm món theo từ khóa với thứ tự ưu tiên:
     * 1. Khớp chính xác mã món.
     * 2. Khớp tiền tố mã món (cof, tea, mil, bak).
     * 3. Khớp một phần mã hoặc tên món.
     */
    private List<DoUongDTO> locTheoTuKhoa(String tuKhoa, String nhom) {
        String khoa = chuanHoa(tuKhoa).toLowerCase();
        String nhomChuan = chuanHoaNhom(nhom);
        List<DoUongDTO> ketQua = new ArrayList<>();

        if (khoa.isEmpty()) {
            for (DoUongDTO du : dsDoUong) {
                if (nhomChuan.equals("Tất cả") || layNhom(du).equals(nhomChuan)) {
                    ketQua.add(du);
                }
            }
            return ketQua;
        }

        DoUongDTO trungMa = timTheoMa(khoa);
        if (trungMa != null) {
            if (nhomChuan.equals("Tất cả") || layNhom(trungMa).equals(nhomChuan)) {
                ketQua.add(trungMa);
            }
            return ketQua;
        }

        String tienTo = layTienTo(khoa);
        if (!tienTo.isEmpty()) {
            for (DoUongDTO du : dsDoUong) {
                boolean dungNhom = nhomChuan.equals("Tất cả") || layNhom(du).equals(nhomChuan);
                if (dungNhom && chuanHoa(du.getMaMon()).toLowerCase().startsWith(tienTo)) {
                    ketQua.add(du);
                }
            }
            return ketQua;
        }

        for (DoUongDTO du : dsDoUong) {
            boolean dungNhom = nhomChuan.equals("Tất cả") || layNhom(du).equals(nhomChuan);
            boolean dungMa = chuanHoa(du.getMaMon()).toLowerCase().contains(khoa);
            boolean dungTen = chuanHoa(du.getTenMon()).toLowerCase().contains(khoa);
            if (dungNhom && (dungMa || dungTen)) {
                ketQua.add(du);
            }
        }
        return ketQua;
    }

    /** Trả về tiền tố nhóm món nếu từ khóa bắt đầu bằng một tiền tố hợp lệ. */
    private String layTienTo(String khoa) {
        String[] dsTienTo = {"cof", "tea", "mil", "bak"};
        for (int i = 0; i < dsTienTo.length; i++) {
            if (khoa.startsWith(dsTienTo[i])) {
                return dsTienTo[i];
            }
        }
        return "";
    }

    private String chuanHoaNhom(String nhom) {
        String n = chuanHoa(nhom);
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
    /** Thêm món mới sau khi kiểm tra mã trùng. DTO do giao diện tạo và truyền vào. */
    public String themDoUong(DoUongDTO doUong) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền thêm món.";
        }
        if (doUong == null) {
            return "Thông tin món không hợp lệ.";
        }
        if (timTheoMa(doUong.getMaMon()) != null) {
            return "Mã món đã tồn tại.";
        }

        dsDoUong.add(doUong);
        return null;
    }

    /** Cập nhật tên, giá và mô tả của món đã có. */
    public String suaDoUong(String maMon, String tenMon, double donGia, String moTa) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền sửa món.";
        }

        DoUongDTO du = timTheoMa(maMon);
        if (du == null) {
            return "Không tìm thấy món cần sửa.";
        }
        du.setTenMon(chuanHoa(tenMon));
        du.setDonGia(donGia);
        du.setMoTa(chuanHoa(moTa));
        return null;
    }

    /** Xóa món theo mã. */
    public String xoaDoUong(String maMon) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền xóa món.";
        }
        DoUongDTO du = timTheoMa(maMon);
        if (du == null) {
            return "Không tìm thấy món cần xóa.";
        }
        dsDoUong.remove(du);
        return null;
    }

    /** Lưu danh sách vào CSDL. Hiện dữ liệu vẫn nằm trong bộ nhớ. */
    public boolean luuVaoCSDL() {
        return true;
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
