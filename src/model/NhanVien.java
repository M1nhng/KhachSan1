package model;

import java.text.DecimalFormat;

public class NhanVien extends Person {
    private double luongCoBan;
    private String chucvu;
    private static final DecimalFormat df = new DecimalFormat("#,###");

    public NhanVien() {
        super();
    }

    public NhanVien(String maID, String ten, String soCMND, String soDienThoai, double luongCoBan, String chucvu) {
        super(maID, ten, soCMND, soDienThoai);
        this.luongCoBan = luongCoBan;
        this.chucvu = chucvu;
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public String getChucVu() {
        return chucvu;
    }

    public void setChucVu(String chucvu) {
        this.chucvu = chucvu;
    }

    @Override
    public String getVaiTro() {
        return "Nhân Viên";
    }

    @Override
    public String toString() {
        return String.format("NV [%s] %s - %s", getMaID(), getTen(), getChucVu());
    }
}