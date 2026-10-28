package newDayCoffee.dto;

import java.time.LocalDate;

public class NhanVienDTO {
    private String maNV;
    private String tenNV;
    private LocalDate ngaySinh;
    private String sdt;
    private String email;
    private String chucVu;
    private String gioiTinh;
    private String matKhau;

    public NhanVienDTO() {
        this.maNV = "";
        this.tenNV = "";
        this.ngaySinh = null;
        this.sdt = "";
        this.email = "";
        this.chucVu = "";
        this.gioiTinh = "";
        this.matKhau = "123456";
    }

    /** Constructor chỉ chứa mã. */
    public NhanVienDTO(String maNV) {
        this();
        this.maNV = maNV;
    }

    /**
     * NOTE: constructor không có tham số mật khẩu, dùng khi thêm nhân viên mới.
     * Nhân viên được gán mật khẩu mặc định "123456", sau đó tự đổi mật khẩu.
     */
    public NhanVienDTO(String maNV, String tenNV, LocalDate ngaySinh, String sdt, String email, String chucVu, String gioiTinh) {
        this(maNV, tenNV, ngaySinh, sdt, email, chucVu, gioiTinh, "123456");
    }

    public NhanVienDTO(String maNV, String tenNV, LocalDate ngaySinh, String sdt, String email, String chucVu, String gioiTinh, String matKhau) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.email = email;
        this.chucVu = chucVu;
        this.gioiTinh = gioiTinh;
        this.matKhau = matKhau;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    @Override
    public String toString() {
        return "NhanVienDTO [maNV=" + maNV + ", tenNV=" + tenNV + ", ngaySinh=" + ngaySinh + ", sdt=" + sdt + ", email="
                + email + ", chucVu=" + chucVu + ", gioiTinh=" + gioiTinh + "]";
    }
}