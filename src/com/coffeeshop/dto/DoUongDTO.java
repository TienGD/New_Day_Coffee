package com.coffeeshop.dto;

public class DoUongDTO {
    private String maMon;
    private String tenMon;
    private double donGia;
    private boolean daXoa; // cờ đánh dấu xóa mềm

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

    public String getGiaFormatted() {
        return String.format("%,.0fĐ", donGia).replace(',', '.');
    }

    @Override
    public String toString() {
        return tenMon + " (" + getGiaFormatted() + ")";
    }
}
