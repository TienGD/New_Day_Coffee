package newDayCoffee.bus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.CaTrucDTO;
import newDayCoffee.dto.ChiTietCaTrucDTO;
import newDayCoffee.dto.NhanVienDTO;
import newDayCoffee.utils.Auth;
import newDayCoffee.utils.Validator;

/**
 * Quản lý danh sách ca trực và danh sách nhân viên được phân công.
 *
 * Dữ liệu nằm trong bộ nhớ, dùng chung qua DSNhanVienBUS khi đổi tài khoản.
 * DTO do giao diện tạo; chỉ lưu khi ca và toàn bộ nhân viên đều hợp lệ.
 */
public class DSCaTrucBUS {

    public static final String CA_SANG = "Ca sáng (6:00 - 11:00)";
    public static final String CA_CHIEU = "Ca chiều (12:00 - 17:00)";
    public static final String TRANG_THAI_MOI = "Chưa hoàn thành";

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<CaTrucDTO> dsCaTruc;
    private final ChiTietCaTrucBUS chiTietCaTrucBUS;
    private final DSNhanVienBUS DSNhanVienBUS;
    private boolean daNapDuLieuMau = false;

    public DSCaTrucBUS(DSNhanVienBUS DSNhanVienBUS) {
        this.DSNhanVienBUS = DSNhanVienBUS;
        this.dsCaTruc = new ArrayList<>();
        this.chiTietCaTrucBUS = new ChiTietCaTrucBUS();
    }

    // ==================== KHU VỰC ĐỌC / TÌM KIẾM ====================
    public List<CaTrucDTO> layDanhSachCaTruc() {
        return Auth.laQuanLy() ? new ArrayList<>(dsCaTruc) : new ArrayList<>();
    }

    public CaTrucDTO timTheoMa(String maCaTruc) {
        return coQuyenDocCa(maCaTruc) ? timNoiBo(maCaTruc) : null;
    }

    private boolean coQuyenDocCa(String maCaTruc) {
        return Auth.laQuanLy() || (Auth.isDangNhap()
                && chiTietCaTrucBUS.coNhanVienTrongCa(maCaTruc, Auth.getNguoiDung().getMaNV()));
    }

    private CaTrucDTO timNoiBo(String maCaTruc) {
        String ma = chuanHoa(maCaTruc);
        for (CaTrucDTO caTruc : dsCaTruc) {
            if (caTruc.getMaCaTruc().equalsIgnoreCase(ma)) {
                return caTruc;
            }
        }
        return null;
    }

    /** Chỉ lấy các ca có phân công nhân viên được truyền vào. */
    public List<CaTrucDTO> layCaTrucTheoNhanVien(String maNV) {
        List<CaTrucDTO> ketQua = new ArrayList<>();
        if (chuanHoa(maNV).isEmpty() || !Auth.isDangNhap()
                || (!Auth.laQuanLy() && !chuanHoa(maNV).equalsIgnoreCase(Auth.getNguoiDung().getMaNV()))) {
            return ketQua;
        }
        for (CaTrucDTO caTruc : dsCaTruc) {
            if (chiTietCaTrucBUS.coNhanVienTrongCa(caTruc.getMaCaTruc(), maNV)) {
                ketQua.add(caTruc);
            }
        }
        return ketQua;
    }

    public List<ChiTietCaTrucDTO> layChiTiet(String maCaTruc) {
        List<ChiTietCaTrucDTO> ketQua = new ArrayList<>();
        if (!coQuyenDocCa(maCaTruc)) return ketQua;
        for (ChiTietCaTrucDTO chiTiet : chiTietCaTrucBUS.layTheoCa(maCaTruc)) {
            if (Auth.laQuanLy() || chiTiet.getMaNV().equalsIgnoreCase(Auth.getNguoiDung().getMaNV())) {
                ketQua.add(chiTiet);
            }
        }
        return ketQua;
    }

    /** Ghép tên các nhân viên trong ca để hiển thị trên bảng. */
    public String layTenNhanVien(String maCaTruc) {
        StringBuilder ketQua = new StringBuilder();
        for (ChiTietCaTrucDTO chiTiet : layChiTiet(maCaTruc)) {
            NhanVienDTO nhanVien = DSNhanVienBUS.timTheoMa(chiTiet.getMaNV());
            if (nhanVien == null) {
                nhanVien = chiTiet.getNhanVien();
            }
            if (ketQua.length() > 0) {
                ketQua.append(", ");
            }
            String tenNV = chuanHoa(nhanVien.getTenNV());
            ketQua.append(tenNV.isEmpty() ? nhanVien.getMaNV() : tenNV);
        }
        return ketQua.toString();
    }

    /** Ghép mã nhân viên để đưa trở lại ô nhập nhiều nhân viên. */
    public String layMaNhanVien(String maCaTruc) {
        StringBuilder ketQua = new StringBuilder();
        for (ChiTietCaTrucDTO chiTiet : layChiTiet(maCaTruc)) {
            if (ketQua.length() > 0) {
                ketQua.append(", ");
            }
            ketQua.append(chiTiet.getMaNV());
        }
        return ketQua.toString();
    }

    // ==================== KHU VỰC THÊM CA TRỰC ====================
    public String themCaTruc(CaTrucDTO caTruc, List<ChiTietCaTrucDTO> dsChiTietMoi) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền thêm ca trực.";
        }

        String loi = kiemTraDuLieuCaTruc(caTruc, dsChiTietMoi);
        if (loi != null) {
            return loi;
        }

        dsCaTruc.add(caTruc);
        chiTietCaTrucBUS.themChiTiet(dsChiTietMoi);
        return null;
    }

    /** Kiểm tra toàn bộ ca trước khi thêm, tránh lưu dở danh sách nhân viên. */
    private String kiemTraDuLieuCaTruc(CaTrucDTO caTruc, List<ChiTietCaTrucDTO> dsChiTietMoi) {
        return kiemTraDuLieuCaTruc(caTruc, dsChiTietMoi, null);
    }

    /** Khi sửa, bỏ qua chính ca đang sửa trong các bước kiểm tra trùng. */
    private String kiemTraDuLieuCaTruc(CaTrucDTO caTruc, List<ChiTietCaTrucDTO> dsChiTietMoi,
            String maCaBoQua) {
        if (caTruc == null) {
            return "Thông tin ca trực không hợp lệ.";
        }
        if (caTruc.getMaCaTruc() == null || !caTruc.getMaCaTruc().matches(Validator.MA_CA_TRUC)) {
            return "Mã ca trực phải có đúng 4 ký tự dạng CAXX (ví dụ CA01).";
        }
        if (timNoiBo(caTruc.getMaCaTruc()) != null
                && !caTruc.getMaCaTruc().equalsIgnoreCase(chuanHoa(maCaBoQua))) {
            return "Mã ca trực đã tồn tại.";
        }
        if (caTruc.getNgayTruc() == null || caTruc.getNgayTruc().isBefore(LocalDate.now())) {
            return "Ngày trực phải từ ngày hiện tại trở đi.";
        }
        if (!CA_SANG.equals(caTruc.getTenCaTruc()) && !CA_CHIEU.equals(caTruc.getTenCaTruc())) {
            return "Vui lòng chọn ca sáng hoặc ca chiều.";
        }
        if (dsChiTietMoi == null || dsChiTietMoi.size() < 3) {
            return "Mỗi ca trực phải có ít nhất 3 nhân viên.";
        }

        List<String> dsMaDaChon = new ArrayList<>();
        for (ChiTietCaTrucDTO chiTiet : dsChiTietMoi) {
            if (chiTiet == null || chiTiet.getCaTruc() == null || chiTiet.getNhanVien() == null
                    || !caTruc.equals(chiTiet.getCaTruc())) {
                return "Chi tiết ca trực không hợp lệ.";
            }
            String maNV = chuanHoa(chiTiet.getMaNV()).toLowerCase();
            if (maNV.isEmpty() || DSNhanVienBUS.timTheoMa(maNV) == null) {
                return "Nhân viên " + maNV + " chưa tồn tại trong hệ thống.";
            }
            if (dsMaDaChon.contains(maNV)) {
                return "Nhân viên " + maNV + " bị trùng trong cùng ca trực.";
            }
            if (chiTiet.getSoLuong() != dsChiTietMoi.size()) {
                return "Số lượng phải bằng số nhân viên được phân công vào ca.";
            }
            for (CaTrucDTO caDaCo : dsCaTruc) {
                if (caDaCo.getMaCaTruc().equalsIgnoreCase(chuanHoa(maCaBoQua))) {
                    continue;
                }
                if (caTruc.getNgayTruc().equals(caDaCo.getNgayTruc())
                        && caTruc.getTenCaTruc().equals(caDaCo.getTenCaTruc())
                        && chiTietCaTrucBUS.coNhanVienTrongCa(caDaCo.getMaCaTruc(), maNV)) {
                    return "Nhân viên " + maNV + " đã được phân công vào ca này trong ngày đã chọn.";
                }
            }
            dsMaDaChon.add(maNV);
        }
        return null;
    }

    // ==================== KHU VỰC SỬA / XÓA CA TRỰC ====================
    /** Kiểm tra đủ dữ liệu trước khi thay ca và toàn bộ danh sách phân công. */
    public String suaCaTruc(String maCaTruc, CaTrucDTO caTrucMoi, List<ChiTietCaTrucDTO> dsChiTietMoi) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền sửa ca trực.";
        }
        CaTrucDTO caCu = timTheoMa(maCaTruc);
        if (caCu == null) {
            return "Không tìm thấy ca trực cần sửa.";
        }
        if (caTrucMoi == null || !caCu.getMaCaTruc().equals(caTrucMoi.getMaCaTruc())) {
            return "Không được thay đổi mã ca trực khi sửa.";
        }
        String loi = kiemTraDuLieuCaTruc(caTrucMoi, dsChiTietMoi, caCu.getMaCaTruc());
        if (loi != null) {
            return loi;
        }

        dsCaTruc.set(dsCaTruc.indexOf(caCu), caTrucMoi);
        chiTietCaTrucBUS.xoaTheoCa(caCu.getMaCaTruc());
        chiTietCaTrucBUS.themChiTiet(dsChiTietMoi);
        return null;
    }

    public String xoaCaTruc(String maCaTruc) {
        if (!Auth.laQuanLy()) {
            return "Chỉ quản lí mới có quyền xóa ca trực.";
        }
        CaTrucDTO caTruc = timTheoMa(maCaTruc);
        if (caTruc == null) {
            return "Không tìm thấy ca trực cần xóa.";
        }
        dsCaTruc.remove(caTruc);
        chiTietCaTrucBUS.xoaTheoCa(caTruc.getMaCaTruc());
        return null;
    }

    private boolean laTrangThaiHopLe(String trangThai) {
        return TRANG_THAI_MOI.equals(trangThai) || "Đã hoàn thành".equals(trangThai)
                || "Vắng có phép".equals(trangThai) || "Vắng không có phép".equals(trangThai);
    }

    // ==================== KHU VỰC DỮ LIỆU MẪU (TẠM THỜI) ====================
    /** Nạp một lần lúc khởi động, không phục hồi ca mẫu đã bị quản lý xóa. */
    public String napDuLieuMau(List<CaTrucDTO> dsCaMau, List<ChiTietCaTrucDTO> dsChiTietMau) {
        if (daNapDuLieuMau) {
            return null;
        }
        if (!dsCaTruc.isEmpty() || dsCaMau == null || dsCaMau.isEmpty() || dsChiTietMau == null) {
            return "Không thể nạp dữ liệu ca trực mẫu.";
        }

        DSCaTrucBUS dsTam = new DSCaTrucBUS(DSNhanVienBUS);
        List<ChiTietCaTrucDTO> dsChiTietHopLe = new ArrayList<>();
        for (CaTrucDTO caTruc : dsCaMau) {
            List<ChiTietCaTrucDTO> dsTheoCa = new ArrayList<>();
            for (ChiTietCaTrucDTO chiTiet : dsChiTietMau) {
                if (chiTiet != null && caTruc != null && caTruc.equals(chiTiet.getCaTruc())) {
                    dsTheoCa.add(chiTiet);
                }
            }
            String loi = dsTam.kiemTraDuLieuCaTruc(caTruc, dsTheoCa);
            if (loi != null) {
                return loi;
            }
            dsTam.dsCaTruc.add(caTruc);
            dsTam.chiTietCaTrucBUS.themChiTiet(dsTheoCa);
            dsChiTietHopLe.addAll(dsTheoCa);
        }
        if (dsChiTietHopLe.size() != dsChiTietMau.size()) {
            return "Dữ liệu phân công mẫu không khớp với danh sách ca trực.";
        }
        dsCaTruc.addAll(dsTam.dsCaTruc);
        chiTietCaTrucBUS.themChiTiet(dsChiTietHopLe);
        daNapDuLieuMau = true;
        return null;
    }

    private String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
