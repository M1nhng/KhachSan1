package model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Phong {
    private String maPhong;
    private String loaiPhong;
    private double giaPhong;
    private boolean trangThai;
    private KhachHang KhachThue;

    private static final DecimalFormat df = new DecimalFormat("#,###");

   
    public Phong() {
    }

    public Phong(String maPhong, String loaiPhong, double giaPhong, boolean trangThai) {
        setMaPhong(maPhong);
        setLoaiPhong(loaiPhong);
        setGiaPhong(giaPhong);
        setTrangThai(trangThai);
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public double getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(double giaPhong) {
        if (giaPhong > 0) {
            this.giaPhong = giaPhong;
        }
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public KhachHang getKhachThue() {
        return KhachThue;
    }

    public void setKhachThue(KhachHang khach) {
        this.KhachThue = khach;
    }

    public String datPhong(KhachHang khach) {
        if (trangThai) {
            return "Phong " + maPhong + " da co nguoi thue!";
        }
        if (khach == null) {
            return "Khach hang khong hop le!";
        }
        this.trangThai = true;
        this.KhachThue = khach;
        return "Phong " + maPhong + " da duoc dat cho khach: " + khach.getTen();
    }

    public String traPhong() {
        if (!trangThai) {
            return "Phong " + maPhong + " dang trong!";
        }
        String tenKhachCu = (this.KhachThue != null) ? this.KhachThue.getTen() : "[Khong ro]";

        this.trangThai = false;
        this.KhachThue = null;
        return "Phong " + maPhong + " da duoc tra boi " + tenKhachCu + ".";
    }


    private List<DichVu> dichVuList = new ArrayList<>();

    public void addDichVu(DichVu dv) {
        dichVuList.add(dv);
    }

    public void removeDichVu(DichVu dv) {
        dichVuList.remove(dv);
    }

    public List<DichVu> getDichVuList() {
        return dichVuList;
    }

    @Override
    public String toString() {
        return String.format(
                "Phong %s | Loai: %s | Gia: %s VND | Trang thai: %s",
                maPhong, loaiPhong, df.format(giaPhong),
                (trangThai ? "Da thue" : "Trong"));
    }
}