package model;

public class DichVu {
    private int maDV;
    private String tenDV;
    private double giaDV;
    private String maPhong;

    public DichVu() {
    }

    public DichVu(String tenDV, double giaDV, String maPhong) {
        this.tenDV = tenDV;
        this.giaDV = giaDV;
        this.maPhong = maPhong;
    }

    public DichVu(int maDV, String tenDV, double giaDV, String maPhong) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.giaDV = giaDV;
        this.maPhong = maPhong;
    }

    public int getMaDV() {
        return maDV;
    }

    public void setMaDV(int maDV) {
        this.maDV = maDV;
    }

    public String getTenDV() {
        return tenDV;
    }

    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
    }

    public double getGiaDV() {
        return giaDV;
    }

    public void setGiaDV(double giaDV) {
        this.giaDV = giaDV;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    @Override
    public String toString() {
        return tenDV;
    }
}