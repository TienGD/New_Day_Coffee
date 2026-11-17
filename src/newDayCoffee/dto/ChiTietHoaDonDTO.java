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
        this();
        this.maHoaDon.setMaHD(maHD);
        this.maDoUong.setMaMon(maMon);
    }

    public ChiTietHoaDonDTO(DoUongDTO doUong, int soLuong) {
        this();
        this.maDoUong = doUong;
        this.soLuong = soLuong;
    }

    public ChiTietHoaDonDTO(HoaDonDTO hoaDon, DoUongDTO doUong, int soLuong, double donGia) {
        this.maHoaDon = hoaDon != null ? hoaDon : new HoaDonDTO();
        this.maDoUong = doUong != null ? doUong : new DoUongDTO();
        this.soLuong = soLuong;
        if (donGia > 0) {
            setDonGia(donGia);
        }
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
        if (maHoaDon == null || maHoaDon.getMaHD() == null) {
            return "";
        }
        return maHoaDon.getMaHD();
    }

    public void setMaHD(String maHD) {
        if (this.maHoaDon == null) {
            this.maHoaDon = new HoaDonDTO();
        }
        this.maHoaDon.setMaHD(maHD);
    }

    public String getMaMon() {
        if (maDoUong == null || maDoUong.getMaMon() == null) {
            return "";
        }
        return maDoUong.getMaMon();
    }

    public void setMaMon(String maMon) {
        if (this.maDoUong == null) {
            this.maDoUong = new DoUongDTO();
        }
        this.maDoUong.setMaMon(maMon);
    }

    public String getTenMon() {
        if (maDoUong == null || maDoUong.getTenMon() == null) {
            return "";
        }
        return maDoUong.getTenMon();
    }

    public String getMoTa() {
        if (maDoUong == null || maDoUong.getMoTa() != null) {
            return maDoUong == null ? "" : maDoUong.getMoTa();
        }
        return "";
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return maDoUong != null ? maDoUong.getDonGia() : 0;
    }

    public void setDonGia(double donGia) {
        if (maDoUong == null) {
            maDoUong = new DoUongDTO();
            maDoUong.setDonGia(donGia);
        } else if (Double.compare(maDoUong.getDonGia(), donGia) != 0) {
            maDoUong = new DoUongDTO(maDoUong.getMaMon(), maDoUong.getTenMon(), donGia, maDoUong.getMoTa());
        }
    }

    @Override
    public String toString() {
        return "ChiTietHoaDonDTO [maHD=" + getMaHD() + ", maMon=" + getMaMon() + ", soLuong=" + soLuong
                + "]";
    }
}
