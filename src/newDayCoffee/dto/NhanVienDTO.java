package newDayCoffee.dto;

public class NhanVienDTO {
    private String maNV;
    private String tenNV;
    private String ngaySinh;
    private String sdt;
    private String email;
    private String chucVu;
    private String gioiTinh;
    private String matKhau;

    public NhanVienDTO() {
    }

    public NhanVienDTO(String maNV, String tenNV, String ngaySinh, String sdt, String email, String chucVu, String gioiTinh) {
        this(maNV, tenNV, ngaySinh, sdt, email, chucVu, gioiTinh, "123456");
    }

    public NhanVienDTO(String maNV, String tenNV, String ngaySinh, String sdt, String email, String chucVu, String gioiTinh, String matKhau) {
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

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
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

    public String layVaiTro() {
        return chucVu;
    }

    @Override
    public String toString() {
        return tenNV + " [" + chucVu + "]";
    }
}