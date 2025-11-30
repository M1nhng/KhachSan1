package model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ThanhToan {
    private static List<ThanhToan> lichSuThanhToan = new ArrayList<>();
    private static double tongDoanhThu = 0;

    private static final DecimalFormat df = new DecimalFormat("#,###");

    private String maPhong;
    private String tenKhach;
    private double soTien;
    private String thoiGian;
    private String chiTietDichVu;

    public ThanhToan() {
    }

    public ThanhToan(String maPhong, String tenKhach, double soTien, String chiTietDichVu) {
        this.maPhong = maPhong;
        this.tenKhach = tenKhach;
        this.soTien = soTien;
        this.chiTietDichVu = chiTietDichVu;
        this.thoiGian = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ThanhToan(String maPhong, String tenKhach, double soTien, String chiTietDichVu, String thoiGian) {
        this.maPhong = maPhong;
        this.tenKhach = tenKhach;
        this.soTien = soTien;
        this.chiTietDichVu = chiTietDichVu;
        this.thoiGian = thoiGian;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public double getSoTien() {
        return soTien;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public String getChiTietDichVu() {
        return chiTietDichVu;
    }

    public static String ghiNhanThanhToan(Phong phong, double tongTienThucTe, String dsDichVu) {
        if (phong == null || phong.getKhachThue() == null) {
            return "Không thể thanh toán! Phòng và khách không hợp lệ!";
        }

        double soTienCuoi = (tongTienThucTe > 0) ? tongTienThucTe : phong.getGiaPhong();

        ThanhToan tt = new ThanhToan(
                phong.getMaPhong(),
                phong.getKhachThue().getTen(),
                soTienCuoi,
                dsDichVu);

        lichSuThanhToan.add(tt);
        tongDoanhThu += soTienCuoi;

        return "Thanh toán thành công! Tổng tiền: " + df.format(soTienCuoi) + " VND";
    }

    public static String ghiNhanThanhToan(Phong phong) {
        return ghiNhanThanhToan(phong, 0, "Không có dịch vụ");
    }

    public static void xemLichSuThanhToan() {
        if (lichSuThanhToan.isEmpty()) {
            System.out.println("Chưa có thanh toán nào (trên RAM)!");
            return;
        }
        for (ThanhToan tt : lichSuThanhToan) {
            System.out.println(tt);
        }
    }

    public static void xemDoanhThu() {
        System.out.println("Tổng doanh thu (phiên này): " + df.format(tongDoanhThu) + " VND");
    }

    @Override
    public String toString() {
        return String.format("Phòng: %s | Khách: %s | Tiền: %s | DV: %s | Ngày: %s",
                maPhong, tenKhach, df.format(soTien), chiTietDichVu, thoiGian);
    }
}