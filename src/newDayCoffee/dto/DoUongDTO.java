package newDayCoffee.dto;

public class DoUongDTO {
    private String maMon;
    private String tenMon;
    private double donGia;
    private String moTa;

    public DoUongDTO() {
        this.maMon = "";
        this.tenMon = "";
        this.donGia = 0;
        this.moTa = "";
    }

    public DoUongDTO(String maMon) {
        this();
        this.maMon = maMon;
    }

    public DoUongDTO(String maMon, String tenMon, double donGia) {
        this(maMon, tenMon, donGia, "");
    }

    public DoUongDTO(String maMon, String tenMon, double donGia, String moTa) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.moTa = moTa;
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

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return "DoUongDTO [maMon=" + maMon + ", tenMon=" + tenMon + ", donGia=" + donGia + ", moTa=" + moTa + "]";
    }
}