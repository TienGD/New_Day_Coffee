package newDayCoffee.dto;

public class DoUongDTO {
    public static final String DM_CA_PHE = "Cà phê";
    public static final String DM_TRA = "Trà";
    public static final String DM_TRA_SUA = "Trà sữa";
    public static final String DM_KHAC = "Khác";

    private String maMon;
    private String tenMon;
    private double donGia;
    private boolean daXoa;  // cờ đánh dấu xóa mềm
    private String moTa = ""; // mô tả món, mặc định rỗng
    public DoUongDTO() {
    }

    public DoUongDTO(String maMon, String tenMon, double donGia) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.daXoa = false;
    }

    public DoUongDTO(String maMon, String tenMon, double donGia, boolean daXoa) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.daXoa = daXoa;
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

    public boolean isDaXoa() {
        return daXoa;
    }

    public void setDaXoa(boolean daXoa) {
        this.daXoa = daXoa;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = (moTa == null) ? "" : moTa;
    }

   
    public String layDanhMuc() {
        if (maMon == null) {
            return DM_KHAC;
        }
        String m = maMon.trim().toLowerCase();
        if (m.startsWith("cof")) {
            return DM_CA_PHE;
        }
        if (m.startsWith("tea")) {
            return DM_TRA;
        }
        if (m.startsWith("mil")) {
            return DM_TRA_SUA;
        }
        return DM_KHAC;
    }

    public boolean kiemTraTienTo() {
        return !DM_KHAC.equals(layDanhMuc());
    }

    public boolean kiemTraTienTo(String tienTo) {
        if (maMon == null || tienTo == null || tienTo.trim().isEmpty()) {
            return false;
        }
        return maMon.trim().toLowerCase().startsWith(tienTo.trim().toLowerCase());
    }

    public String getGiaFormatted() {
        return String.format("%,.0fĐ", donGia).replace(',', '.');
    }

    @Override
    public String toString() {
        return tenMon + " (" + getGiaFormatted() + ")";
    }
}