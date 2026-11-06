package newDayCoffee.dto;

import java.time.LocalDate;
import java.util.Objects;


public class NhanVienDTO {
    private String maNV;
    private String tenNV;
    private LocalDate ngaySinh;
    private String sdt;
    private String email;
    private String chucVu;
    private String gioiTinh;

    public NhanVienDTO() {
        this.maNV = "";
        this.tenNV = "";
        this.ngaySinh = null;
        this.sdt = "";
        this.email = "";
        this.chucVu = "";
        this.gioiTinh = "";
    }

    public NhanVienDTO(String maNV) {
        this();
        this.maNV = maNV;
    }

    public NhanVienDTO(String maNV, String tenNV, LocalDate ngaySinh, String sdt, String email, String chucVu, String gioiTinh, String matKhau) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.email = email;
        this.chucVu = chucVu;
        this.gioiTinh = gioiTinh;
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

    

    @Override
	public int hashCode() {
		return Objects.hash(maNV);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVienDTO other = (NhanVienDTO) obj;
		return Objects.equals(maNV, other.maNV);
	}

	@Override
    public String toString() {
        return "NhanVienDTO [maNV=" + maNV + ", tenNV=" + tenNV + ", ngaySinh=" + ngaySinh + ", sdt=" + sdt + ", email="
                + email + ", chucVu=" + chucVu + ", gioiTinh=" + gioiTinh + "]";
    }
}