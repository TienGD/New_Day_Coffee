package newDayCoffee.dto;

public class ChiTietHoaDonDTO {
    private HoaDonDTO hoaDon;
    private DoUongDTO doUong;
    private int soLuong;
    private double donGia;

    public ChiTietHoaDonDTO() {
        this.hoaDon = new HoaDonDTO();
        this.doUong = new DoUongDTO();
        this.soLuong = 0;
        this.donGia = 0;
    }

    public ChiTietHoaDonDTO(String maHD, String maMon) {
        this.hoaDon.setMaHD(maHD);
        this.doUong.setMaMon(maMon);
    }


    public ChiTietHoaDonDTO(HoaDonDTO hoaDon, DoUongDTO doUong, int soLuong, double donGia) {
        this.hoaDon = hoaDon;
        this.doUong = doUong;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public HoaDonDTO getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDonDTO hoaDon) {
        this.hoaDon = hoaDon;
    }

    public DoUongDTO getDoUong() {
        return doUong;
    }

    public void setDoUong(DoUongDTO doUong) {
        this.doUong = doUong;
    }

    public String getMaHD() {
        return hoaDon.getMaHD();
    }

    public void setMaHD(String maHD) {
        this.hoaDon.setMaHD(maHD);
    }

    public String getMaMon() {
        return doUong.getMaMon();
    }

    public void setMaMon(String maMon) {
        this.doUong.setMaMon(maMon);
    }

    public String getTenMon() {
        return doUong.getTenMon();
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
        return "ChiTietHoaDonDTO [maHD=" + getMaHD() + ", maMon=" + getMaMon() + ", soLuong=" + soLuong
                + ", donGia=" + donGia + "]";
    }
}
