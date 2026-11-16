package newDayCoffee.dto;

import java.time.LocalDate;
import java.util.Objects;

public class HoaDonDTO {
    private String maHoaDon;
    private LocalDate ngayLap;
    private NhanVienDTO maNV;
    private double tienKhachTra;
    private String ghiChu;
    private String trangThai;
   
    
    
    public HoaDonDTO() {
        this.maHoaDon = "";
        this.ngayLap = LocalDate.now();
        this.maNV = new NhanVienDTO();
        this.tienKhachTra = 0;
        this.ghiChu = "";
        this.trangThai = "";
    }

    public HoaDonDTO(String maHD) {
        this();
        this.maHoaDon = maHD;
    }

    public HoaDonDTO(String maHD, LocalDate ngayLap, NhanVienDTO maNV, double tienKhachDua, String ghiChu, String trangThai) {
        this.maHoaDon = maHD;
        this.ngayLap = ngayLap;
        this.maNV = maNV;
        this.tienKhachTra = tienKhachDua;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    
    public String getMaHD() {
        return maHoaDon;
    }

    public void setMaHD(String maHD) {
        this.maHoaDon = maHD;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
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
        return tienKhachTra;
    }

    public void setTienKhachDua(double tienKhachDua) {
        this.tienKhachTra = tienKhachDua;
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
        return "HoaDonDTO [maHD=" + maHoaDon + ", ngayLap=" + ngayLap + ", maNV=" + maNV
                + ", tienKhachDua=" + tienKhachTra
                + ", ghiChu=" + ghiChu + ", trangThai=" + trangThai + "]";
    }
}