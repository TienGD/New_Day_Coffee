package newDayCoffee.dto;

import java.util.Objects;

public class HoaDonDTO {
    private String maHD;
    private String ngayLap;
    private NhanVienDTO maNV;
    private double tienKhachDua;
    private String ghiChu;
    private String trangThai;
   
    
    
    public HoaDonDTO() {
        this.maHD = "";
        this.ngayLap = "";
        this.maNV = new NhanVienDTO();
        this.tienKhachDua = 0;
        this.ghiChu = "";
        this.trangThai = "";
    }

    public HoaDonDTO(String maHD) {
        this();
        this.maHD = maHD;
    }

    public HoaDonDTO(String maHD, String ngayLap, NhanVienDTO maNV, double tienKhachDua, String ghiChu, String trangThai) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.maNV = maNV;
        this.tienKhachDua = tienKhachDua;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    
    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }

    public NhanVienDTO getMaNV() {
        return maNV;
    }

    public void setMaNV(NhanVienDTO maNV) {
        this.maNV = maNV;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }



    public double getTienKhachDua() {
        return tienKhachDua;
    }

    public void setTienKhachDua(double tienKhachDua) {
        this.tienKhachDua = tienKhachDua;
    }


    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
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
		HoaDonDTO other = (HoaDonDTO) obj;
		return Objects.equals(maNV, other.maNV);
	}

	@Override
    public String toString() {
        return "HoaDonDTO [maHD=" + maHD + ", ngayLap=" + ngayLap + ", maNV=" + maNV
                + ", tienKhachDua=" + tienKhachDua
                + ", ghiChu=" + ghiChu + ", trangThai=" + trangThai + "]";
    }
}