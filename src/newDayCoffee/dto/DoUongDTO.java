package newDayCoffee.dto;

import java.util.Objects;

public class DoUongDTO {
    private String maDoUong;
    private String tenDoUong;
    private double giaBan;
    private String moTa;
   
    
    
    public DoUongDTO() {
        this.maDoUong = "";
        this.tenDoUong = "";
        this.giaBan = 0;
        this.moTa = "";
    }

    public DoUongDTO(String maMon) {
        this();
        this.maDoUong = maMon;
    }

    public DoUongDTO(String maMon, String tenMon, double donGia, String moTa) {
        this.maDoUong = maMon;
        this.tenDoUong = tenMon;
        this.giaBan = donGia;
        this.moTa = moTa;
    }

    public String getMaMon() {
        return maDoUong;
    }

    public void setMaMon(String maMon) {
        this.maDoUong = maMon;
    }

    public String getTenMon() {
        return tenDoUong;
    }

    public void setTenMon(String tenMon) {
        this.tenDoUong = tenMon;
    }

    public double getDonGia() {
        return giaBan;
    }

    public void setDonGia(double donGia) {
        this.giaBan = donGia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDoUong);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DoUongDTO other = (DoUongDTO) obj;
        return Objects.equals(maDoUong, other.maDoUong);
    }

    @Override
    public String toString() {
        return "DoUongDTO [maMon=" + maDoUong + ", tenMon=" + tenDoUong + ", donGia=" + giaBan + ", moTa=" + moTa + "]";
    }
}
