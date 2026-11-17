package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dao.DoUongDAO;
import newDayCoffee.dto.DoUongDTO;
import newDayCoffee.utils.Auth;

/**
 * Xử lý nghiệp vụ trên danh sách đồ uống.
 *
 * Dữ liệu được nạp từ CSDL; các thay đổi được giữ trong bộ nhớ đến khi lưu.
 * Việc kiểm tra dữ liệu form do giao diện thực hiện.
 */
public class DSDoUongBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final DoUongDAO doUongDAO;
    private boolean daNapDuLieu;
    private final List<DoUongDTO> dsDoUong;
    private final List<DoUongDTO> dsThemMoi;
    private final List<DoUongDTO> dsCapNhat;
    private final List<String> dsMaXoa;
    private ChiTietHoaDonBUS chiTietHoaDonBUS;

    public void setChiTietHoaDonBUS(ChiTietHoaDonBUS chiTietHoaDonBUS) {
        this.chiTietHoaDonBUS = chiTietHoaDonBUS;
    }

    /** Khởi tạo danh sách thực đơn: tự động lấy dữ liệu hiện có từ SQL Server qua DoUongDAO. */
    public void khoiTaoDuLieu() {
        if (daNapDuLieu || !dsThemMoi.isEmpty() || !dsCapNhat.isEmpty() || !dsMaXoa.isEmpty()) {
            return;
        }
        try {
            List<DoUongDTO> dsTuDB = doUongDAO.getAll();
            napDuLieuTuCSDL(dsTuDB);
        } catch (Exception e) {
            // Bắt ngoại lệ an toàn khi cơ sở dữ liệu chưa sẵn sàng
        }
    }

    /** Cho biết thực đơn đã được nạp dữ liệu từ CSDL hay chưa. */
    public boolean daNapDuLieu() {
        return daNapDuLieu;
    }

    /** Nạp thực đơn lần đầu, không ghi đè thay đổi đang chờ lưu. */
    public boolean napDuLieuTuCSDL(List<DoUongDTO> dsTuDB) {
        if (daNapDuLieu || dsTuDB == null
                || !dsThemMoi.isEmpty() || !dsCapNhat.isEmpty() || !dsMaXoa.isEmpty()) {
            return false;
        }
        dsDoUong.clear();
        for (DoUongDTO mon : dsTuDB) {
            if (mon != null && !chuanHoa(mon.getMaMon()).isEmpty() && timTheoMa(mon.getMaMon()) == null) {
                dsDoUong.add(mon);
            }
        }
        daNapDuLieu = true;
        dsThemMoi.clear();
        dsCapNhat.clear();
        dsMaXoa.clear();
        return true;
    }

    public DSDoUongBUS() {
        this(new DoUongDAO());
    }

    DSDoUongBUS(DoUongDAO doUongDAO) {
        this.doUongDAO = doUongDAO;
        dsDoUong = new ArrayList<>();
        dsThemMoi = new ArrayList<>();
        dsCapNhat = new ArrayList<>();
        dsMaXoa = new ArrayList<>();
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
        if (doUong == null || chuanHoa(doUong.getMaMon()).isEmpty()) {
            return "Thông tin món không hợp lệ.";
        }
        doUong.setMaMon(chuanHoa(doUong.getMaMon()).toLowerCase());
        if (timTheoMa(doUong.getMaMon()) != null) {
            return "Mã món đã tồn tại.";
        }

        dsDoUong.add(doUong);
        ghiNhanThemMoi(doUong);
        return null;
    }

    /** Cập nhật tên, giá và mô tả của món đã có (tạo bản ghi mới để không làm lệch đơn giá hóa đơn cũ). */
    public String suaDoUong(String maMon, String tenMon, double donGia, String moTa) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền sửa món.";
        }

        String ma = chuanHoa(maMon);
        for (int i = 0; i < dsDoUong.size(); i++) {
            DoUongDTO du = dsDoUong.get(i);
            if (du.getMaMon().equalsIgnoreCase(ma)) {
                DoUongDTO capNhat = new DoUongDTO(du.getMaMon(), chuanHoa(tenMon), donGia, chuanHoa(moTa));
                dsDoUong.set(i, capNhat);
                ghiNhanCapNhat(capNhat);
                return null;
            }
        }
        return "Không tìm thấy món cần sửa.";
    }

    /** Xóa món theo mã. Từ chối xóa nếu món đã xuất hiện trong đơn hàng hoặc hóa đơn. */
    public String xoaDoUong(String maMon) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền xóa món.";
        }
        DoUongDTO du = timTheoMa(maMon);
        if (du == null) {
            return "Không tìm thấy món cần xóa.";
        }
        if (chiTietHoaDonBUS != null && chiTietHoaDonBUS.coDoUongTrongChiTiet(du.getMaMon())) {
            return "Không thể xóa món đang có trong đơn hàng hoặc hóa đơn.";
        }
        dsDoUong.remove(du);
        ghiNhanXoa(du.getMaMon());
        return null;
    }

    private void ghiNhanThemMoi(DoUongDTO mon) {
        String ma = chuanHoa(mon.getMaMon()).toLowerCase();
        if (dsMaXoa.remove(ma)) {
            dsCapNhat.add(mon);
        } else {
            dsThemMoi.add(mon);
        }
    }

    private void ghiNhanCapNhat(DoUongDTO mon) {
        String ma = chuanHoa(mon.getMaMon()).toLowerCase();
        for (int i = 0; i < dsThemMoi.size(); i++) {
            if (chuanHoa(dsThemMoi.get(i).getMaMon()).equalsIgnoreCase(ma)) {
                dsThemMoi.set(i, mon);
                return;
            }
        }
        for (int i = 0; i < dsCapNhat.size(); i++) {
            if (chuanHoa(dsCapNhat.get(i).getMaMon()).equalsIgnoreCase(ma)) {
                dsCapNhat.set(i, mon);
                return;
            }
        }
        dsCapNhat.add(mon);
    }

    private void ghiNhanXoa(String maMon) {
        String ma = chuanHoa(maMon).toLowerCase();
        for (int i = dsThemMoi.size() - 1; i >= 0; i--) {
            if (chuanHoa(dsThemMoi.get(i).getMaMon()).equalsIgnoreCase(ma)) {
                dsThemMoi.remove(i);
                return;
            }
        }
        for (int i = dsCapNhat.size() - 1; i >= 0; i--) {
            if (chuanHoa(dsCapNhat.get(i).getMaMon()).equalsIgnoreCase(ma)) {
                dsCapNhat.remove(i);
            }
        }
        if (!dsMaXoa.contains(ma)) {
            dsMaXoa.add(ma);
        }
    }

    /**
     * Lưu toàn bộ thay đổi chờ lưu (thêm, sửa, xóa) xuống SQL Server thông qua DoUongDAO
     * và đồng bộ lại danh sách trong bộ nhớ với dữ liệu thực tế trong database.
     */
    public boolean luuVaoCSDL() {
        if (!Auth.laQuanLy()) {
            return false;
        }
        DoUongDAO dao = doUongDAO;
        if (dsThemMoi.isEmpty() && dsCapNhat.isEmpty() && dsMaXoa.isEmpty()) {
            List<DoUongDTO> dsHienTai = dao.getAll();
            if (dsHienTai == null || (dsHienTai.isEmpty() && !dsDoUong.isEmpty())) {
                return false;
            }
            return true;
        }

        while (!dsThemMoi.isEmpty()) {
            DoUongDTO mon = dsThemMoi.get(0);
            if (!dao.insert(mon)) {
                return false;
            }
            dsThemMoi.remove(0);
        }

        while (!dsCapNhat.isEmpty()) {
            DoUongDTO mon = dsCapNhat.get(0);
            if (!dao.update(mon)) {
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

        List<DoUongDTO> dsDongBo = dao.getAll();
        if (dsDongBo == null) {
            return false;
        }
        dsDoUong.clear();
        dsDoUong.addAll(dsDongBo);
        daNapDuLieu = true;
        return true;
    }

    private static String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
