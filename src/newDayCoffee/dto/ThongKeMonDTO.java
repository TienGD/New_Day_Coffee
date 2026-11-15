package newDayCoffee.dto;

import java.util.Objects;

public class ThongKeMonDTO {
    private String maMon;
    private String tenMon;
    private int soLuong;

    public ThongKeMonDTO() {
        this.maMon = "";
        this.tenMon = "";
        this.soLuong = 0;
    }

    public ThongKeMonDTO(String maMon, String tenMon, int soLuong) {
        this.maMon = maMon == null ? "" : maMon;
        this.tenMon = tenMon == null ? "" : tenMon;
        this.soLuong = soLuong;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon == null ? "" : maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon == null ? "" : tenMon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maMon);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ThongKeMonDTO other = (ThongKeMonDTO) obj;
        return Objects.equals(maMon, other.maMon);
    }

    @Override
    public String toString() {
        return "ThongKeMonDTO [maMon=" + maMon + ", tenMon=" + tenMon + ", soLuong=" + soLuong + "]";
    }
}
