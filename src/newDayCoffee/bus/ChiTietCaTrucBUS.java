package newDayCoffee.bus;

import java.util.ArrayList;
import java.util.List;

import newDayCoffee.dto.ChiTietCaTrucDTO;

/**
 * Quản lý danh sách nhân viên được phân công vào từng ca trực.
 * DTO do giao diện tạo và truyền vào; BUS chỉ lưu và tra cứu dữ liệu.
 */
public class ChiTietCaTrucBUS {

    // ==================== KHU VỰC DỮ LIỆU ====================
    private final List<ChiTietCaTrucDTO> dsChiTiet;

    public ChiTietCaTrucBUS() {
        dsChiTiet = new ArrayList<>();
    }

    // ==================== KHU VỰC ĐỌC DỮ LIỆU ====================
    /** Trả về danh sách nhân viên của một ca trực. */
    public List<ChiTietCaTrucDTO> layTheoCa(String maCaTruc) {
        List<ChiTietCaTrucDTO> ketQua = new ArrayList<>();
        String ma = chuanHoa(maCaTruc);
        for (ChiTietCaTrucDTO chiTiet : dsChiTiet) {
            if (chiTiet.getMaCaTruc().equalsIgnoreCase(ma)) {
                ketQua.add(chiTiet);
            }
        }
        return ketQua;
    }

    /** Kiểm tra nhân viên đã nằm trong danh sách của ca trực hay chưa. */
    public boolean coNhanVienTrongCa(String maCaTruc, String maNV) {
        String ma = chuanHoa(maNV);
        for (ChiTietCaTrucDTO chiTiet : layTheoCa(maCaTruc)) {
            if (chiTiet.getMaNV().equalsIgnoreCase(ma)) {
                return true;
            }
        }
        return false;
    }

    // ==================== KHU VỰC LƯU CHI TIẾT ====================
    /** Chỉ DSCaTrucBUS gọi sau khi đã kiểm tra toàn bộ dữ liệu của ca. */
    void themChiTiet(List<ChiTietCaTrucDTO> dsChiTietMoi) {
        dsChiTiet.addAll(dsChiTietMoi);
    }

    /** Xóa toàn bộ phân công của ca khi sửa hoặc xóa ca trực. */
    void xoaTheoCa(String maCaTruc) {
        String ma = chuanHoa(maCaTruc);
        for (int i = dsChiTiet.size() - 1; i >= 0; i--) {
            if (dsChiTiet.get(i).getMaCaTruc().equalsIgnoreCase(ma)) {
                dsChiTiet.remove(i);
            }
        }
    }

    private String chuanHoa(String giaTri) {
        return giaTri == null ? "" : giaTri.trim();
    }
}
