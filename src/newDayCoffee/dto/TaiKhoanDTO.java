package newDayCoffee.dto;

import java.util.Objects;

public class TaiKhoanDTO {
    private String matKhau;
    private NhanVienDTO maNV; 

    public TaiKhoanDTO() {
    	this.maNV = new NhanVienDTO();
    	this.matKhau = "";
    }
   
    
    public TaiKhoanDTO(String maNV) {
        this();
        this.maNV = new NhanVienDTO(maNV);
    }
    
    public TaiKhoanDTO(NhanVienDTO maNV, String matKhau) {
        this.maNV = maNV;
        this.matKhau = matKhau;
    }

   
	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public NhanVienDTO getMaNV() {
		return maNV;
	}

	public void setMaNV(NhanVienDTO maNV) {
		this.maNV = maNV;
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
		TaiKhoanDTO other = (TaiKhoanDTO) obj;
		return Objects.equals(maNV, other.maNV);
	}
    
    
}