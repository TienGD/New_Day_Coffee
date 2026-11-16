package newDayCoffee.dto;

public class ChiTietHoaDonDTO {
    private HoaDonDTO maHoaDon;
    private DoUongDTO maDoUong;
    private int soLuong;


    public ChiTietHoaDonDTO() {
        this.maHoaDon = new HoaDonDTO();
        this.maDoUong = new DoUongDTO();
        this.soLuong = 0;
    
    }

    public ChiTietHoaDonDTO(String maHD, String maMon) {
        this.maHoaDon.setMaHD(maHD);
        this.maDoUong.setMaMon(maMon);
    }


    public ChiTietHoaDonDTO(HoaDonDTO hoaDon, DoUongDTO doUong, int soLuong, double donGia) {
        this.maHoaDon = hoaDon;
        this.maDoUong = doUong;
        this.soLuong = soLuong;
    }

    public HoaDonDTO getHoaDon() {
        return maHoaDon;
    }

    public void setHoaDon(HoaDonDTO hoaDon) {
        this.maHoaDon = hoaDon;
    }

    public DoUongDTO getDoUong() {
        return maDoUong;
    }

    public void setDoUong(DoUongDTO doUong) {
        this.maDoUong = doUong;
    }

    public String getMaHD() {
        return maHoaDon.getMaHD();
    }

    public void setMaHD(String maHD) {
        this.maHoaDon.setMaHD(maHD);
    }

    public String getMaMon() {
        return maDoUong.getMaMon();
    }

    public void setMaMon(String maMon) {
        this.maDoUong.setMaMon(maMon);
    }

    public String getTenMon() {
        return maDoUong.getTenMon();
    }

    public String getMoTa() {
        return maDoUong.getMoTa();
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }



    @Override
    public String toString() {
        return "ChiTietHoaDonDTO [maHD=" + getMaHD() + ", maMon=" + getMaMon() + ", soLuong=" + soLuong
                + "]";
    }
}
