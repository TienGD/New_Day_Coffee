package newDayCoffee.dto;

import java.util.Objects;

public class TaiKhoanDTO {
    private String matKhau;
    private NhanVienDTO maNV; 
    private String maDangNhap;

    public TaiKhoanDTO() {
    	this.maNV = new NhanVienDTO();
    	this.matKhau = "";
    	this.maDangNhap = "";
    }

    
    
    public TaiKhoanDTO(String maDangNhap) {
        this();
        this.maDangNhap = maDangNhap;
    }
    
    public TaiKhoanDTO(NhanVienDTO maNV, String matKhau, String maDangNhap) {
        this.maNV = maNV;
        this.matKhau = matKhau;
        this.maDangNhap = maDangNhap;
    }

    public TaiKhoanDTO(NhanVienDTO maNV, String matKhau) {
        this(maNV, matKhau, (maNV != null && maNV.getMaNV() != null) ? maNV.getMaNV() : "");
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
	
	

	public String getMaDangNhap() {
		return maDangNhap;
	}



	public void setMaDangNhap(String maDangNhap) {
		this.maDangNhap = maDangNhap;
	}



	@Override
	public int hashCode() {
		return Objects.hash(maDangNhap);
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
		return Objects.equals(maDangNhap, other.maDangNhap);
	}
    
    
}