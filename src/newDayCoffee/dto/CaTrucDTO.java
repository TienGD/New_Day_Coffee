package newDayCoffee.dto;

import java.time.LocalDate;
import java.util.Objects;

public class CaTrucDTO {
	private String maCaTruc;
	private String tenCaTruc;
	private NhanVienDTO tenNhanVien;
	private NhanVienDTO maNhanVien;
	private LocalDate ngayTruc;
	private String trangThai;
	
	public CaTrucDTO(String maCaTruc) {
		this.maCaTruc = maCaTruc;
	}
	
	public CaTrucDTO(String maCaTruc, String tenCaTruc, NhanVienDTO tenNhanVien, NhanVienDTO maNhanVien,
			LocalDate ngayTruc, String trangThai) {
		this.maCaTruc = maCaTruc;
		this.tenCaTruc = tenCaTruc;
		this.tenNhanVien = tenNhanVien;
		this.maNhanVien = maNhanVien;
		this.ngayTruc = ngayTruc;
		this.trangThai = trangThai;
	}
	public String getMaCaTruc() {
		return maCaTruc;
	}
	public void setMaCaTruc(String maCaTruc) {
		this.maCaTruc = maCaTruc;
	}
	public String getTenCaTruc() {
		return tenCaTruc;
	}
	public void setTenCaTruc(String tenCaTruc) {
		this.tenCaTruc = tenCaTruc;
	}
	public NhanVienDTO getTenNhanVien() {
		return tenNhanVien;
	}
	public void setTenNhanVien(NhanVienDTO tenNhanVien) {
		this.tenNhanVien = tenNhanVien;
	}
	public NhanVienDTO getMaNhanVien() {
		return maNhanVien;
	}
	public void setMaNhanVien(NhanVienDTO maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
	public LocalDate getNgayTruc() {
		return ngayTruc;
	}
	public void setNgayTruc(LocalDate ngayTruc) {
		this.ngayTruc = ngayTruc;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maCaTruc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaTrucDTO other = (CaTrucDTO) obj;
		return Objects.equals(maCaTruc, other.maCaTruc);
	}

}
