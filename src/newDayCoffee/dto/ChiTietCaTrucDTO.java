package newDayCoffee.dto;

import java.util.Objects;

public class ChiTietCaTrucDTO {
    private CaTrucDTO caTruc;
    private NhanVienDTO nhanVien;
    private int soLuong;

    public ChiTietCaTrucDTO() {
        this.caTruc = new CaTrucDTO("");
        this.nhanVien = new NhanVienDTO();
        this.soLuong = 0;
    }

    public ChiTietCaTrucDTO(String maCaTruc, String maNV) {
        this();
        this.caTruc.setMaCaTruc(maCaTruc);
        this.nhanVien.setMaNV(maNV);
    }

    public ChiTietCaTrucDTO(CaTrucDTO caTruc, NhanVienDTO nhanVien, int soLuong) {
        this.caTruc = caTruc;
        this.nhanVien = nhanVien;
        this.soLuong = soLuong;
    }

    public CaTrucDTO getCaTruc() {
        return caTruc;
    }

    public void setCaTruc(CaTrucDTO caTruc) {
        this.caTruc = caTruc;
    }

    public NhanVienDTO getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVienDTO nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getMaCaTruc() {
        return caTruc.getMaCaTruc();
    }

    public void setMaCaTruc(String maCaTruc) {
        this.caTruc.setMaCaTruc(maCaTruc);
    }

    public String getMaNV() {
        return nhanVien.getMaNV();
    }

    public void setMaNV(String maNV) {
        this.nhanVien.setMaNV(maNV);
    }

    public String getTenNV() {
        return nhanVien.getTenNV();
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public int hashCode() {
        return Objects.hash(caTruc, nhanVien);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChiTietCaTrucDTO other = (ChiTietCaTrucDTO) obj;
        return Objects.equals(caTruc, other.caTruc) && Objects.equals(nhanVien, other.nhanVien);
    }

    @Override
    public String toString() {
        return "ChiTietCaTrucDTO [maCaTruc=" + getMaCaTruc() + ", maNV=" + getMaNV() + ", soLuong=" + soLuong + "]";
    }
}
