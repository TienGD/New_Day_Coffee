package newDayCoffee.utils;

import newDayCoffee.dto.NhanVienDTO;

/**
 * Quản lý phiên đăng nhập hiện tại.
 *
 * Auth chỉ giữ nhân viên đang đăng nhập, không xử lý dữ liệu nghiệp vụ.
 * Các lớp BUS sẽ gọi Auth sau khi kiểm tra thông tin đăng nhập hợp lệ.
 */
public class Auth {

    // ==================== KHU VỰC DỮ LIỆU PHIÊN ====================
    private static NhanVienDTO nguoiDungHienTai = null;

    private Auth() {
        // Lớp tiện ích, không cho phép khởi tạo
    }

    // ==================== KHU VỰC ĐĂNG NHẬP / ĐĂNG XUẤT ====================
    /** Lưu nhân viên đang đăng nhập vào phiên làm việc. */
    public static void dangNhap(NhanVienDTO nhanVien) {
        nguoiDungHienTai = nhanVien;
    }

    /** Kết thúc phiên đăng nhập hiện tại. */
    public static void dangXuat() {
        nguoiDungHienTai = null;
    }

    // ==================== KHU VỰC ĐỌC THÔNG TIN PHIÊN ====================
    public static NhanVienDTO getNguoiDung() {
        return nguoiDungHienTai;
    }

    public static boolean isDangNhap() {
        return nguoiDungHienTai != null;
    }

    /** Chỉ tài khoản có mã qlXX và chức vụ "Quản lí" mới được quản lý nhân viên/thực đơn. */
    public static boolean laQuanLy() {
        if (nguoiDungHienTai == null) {
            return false;
        }
        String maNV = nguoiDungHienTai.getMaNV();
        return maNV != null && maNV.trim().toLowerCase().startsWith("ql")
                && "Quản lí".equals(nguoiDungHienTai.getChucVu());
    }

    /** Quyền màn hình dùng chung cho menu và điều hướng. */
    public static boolean coQuyenTruyCapManHinh(String tenManHinh) {
        if (!isDangNhap() || tenManHinh == null) {
            return false;
        }
        if ("Tổng quan".equals(tenManHinh)) {
            return true;
        }
        switch (tenManHinh) {
     // Các màn hình chỉ dành riêng cho Quản lý
        case "Thực đơn":
        case "Nhân viên":
        case "Ca trực":
        case "Danh sách hóa đơn":
        case "Tài khoản":
            return laQuanLy();
            
        // Các màn hình dành cho Nhân viên (không phải Quản lý)
        case "Ca trực của tôi":
            // shortcut: Chua kiem tra ca lam; bo sung khi co nghiep vu xac nhan ca.
            return !laQuanLy();
            
        // Màn hình cả Quản lý và Nhân viên đều có thể truy cập
        case "Bán hàng":
        case "Thanh toán":
            return true;
            
        default:
            return false;
    }
    }

    public static String getTenHienThi() {
        if (nguoiDungHienTai == null) {
            return "Khách";
        }
        return nguoiDungHienTai.getTenNV();
    }
}
