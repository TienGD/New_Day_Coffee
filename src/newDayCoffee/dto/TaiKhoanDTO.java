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
        this.maDangNhap = maDangNhap != null ? maDangNhap : "";
        this.maNV.setMaNV(this.maDangNhap);
    }
    
    public TaiKhoanDTO(NhanVienDTO maNV, String matKhau, String maDangNhap) {
        this.maNV = maNV != null ? maNV : new NhanVienDTO();
        this.matKhau = matKhau != null ? matKhau : "";
        if (maDangNhap != null && !maDangNhap.trim().isEmpty()) {
            this.maDangNhap = maDangNhap;
        } else {
            this.maDangNhap = this.maNV.getMaNV() != null ? this.maNV.getMaNV() : "";
        }
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
		if ((this.maDangNhap == null || this.maDangNhap.trim().isEmpty())
				&& maNV != null && maNV.getMaNV() != null) {
			this.maDangNhap = maNV.getMaNV();
		}
	}
	
	

	public String getMaDangNhap() {
		if ((maDangNhap == null || maDangNhap.trim().isEmpty())
				&& maNV != null && maNV.getMaNV() != null) {
			return maNV.getMaNV();
		}
		return maDangNhap;
	}



	public void setMaDangNhap(String maDangNhap) {
		this.maDangNhap = maDangNhap;
		if (this.maNV != null && (this.maNV.getMaNV() == null || this.maNV.getMaNV().trim().isEmpty())) {
			this.maNV.setMaNV(maDangNhap != null ? maDangNhap : "");
		}
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