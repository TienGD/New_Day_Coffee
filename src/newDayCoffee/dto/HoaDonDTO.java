package newDayCoffee.dto;

import java.util.ArrayList;
import java.util.List;

public class HoaDonDTO {
    private String maHD;
    private String ngayLap;
    private String maNV;
    private double tongTien;
    private double tienKhachDua;
    private double tienThoi;
    private String phuongThucTT;
    private String ghiChu;
    private List<ChiTietHoaDonDTO> dsChiTiet = new ArrayList<>();

    public HoaDonDTO() {
    }

    public HoaDonDTO(String maHD, String ngayLap, String maNV, double tongTien, double tienKhachDua, double tienThoi, String phuongThucTT, String ghiChu) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.maNV = maNV;
        this.tongTien = tongTien;
        this.tienKhachDua = tienKhachDua;
        this.tienThoi = tienThoi;
        this.phuongThucTT = phuongThucTT;
        this.ghiChu = ghiChu;
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

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getTienKhachDua() {
        return tienKhachDua;
    }

    public void setTienKhachDua(double tienKhachDua) {
        this.tienKhachDua = tienKhachDua;
    }

    public double getTienThoi() {
        return tienThoi;
    }

    public void setTienThoi(double tienThoi) {
        this.tienThoi = tienThoi;
    }

    public String getPhuongThucTT() {
        return phuongThucTT;
    }

    public void setPhuongThucTT(String phuongThucTT) {
        this.phuongThucTT = phuongThucTT;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public List<ChiTietHoaDonDTO> getDsChiTiet() {
        return dsChiTiet;
    }

    public void setDsChiTiet(List<ChiTietHoaDonDTO> dsChiTiet) {
        this.dsChiTiet = dsChiTiet;
    }
}
