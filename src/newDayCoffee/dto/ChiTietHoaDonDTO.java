package newDayCoffee.dto;

public class ChiTietHoaDonDTO {
    private String maHD;
    private String maMon;
    private String tenMon;
    private int soLuong;
    private double donGia;

    public ChiTietHoaDonDTO() {
        this.maHD = "";
        this.maMon = "";
        this.tenMon = "";
        this.soLuong = 0;
        this.donGia = 0;
    }

    public ChiTietHoaDonDTO(String maHD, String maMon) {
        this();
        this.maHD = maHD;
        this.maMon = maMon;
    }

    public ChiTietHoaDonDTO(String maHD, String maMon, String tenMon, int soLuong, double donGia) {
        this.maHD = maHD;
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return soLuong * donGia;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDonDTO [maHD=" + maHD + ", maMon=" + maMon + ", tenMon=" + tenMon + ", soLuong=" + soLuong
                + ", donGia=" + donGia + "]";
    }
}