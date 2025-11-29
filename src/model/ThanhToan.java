package model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ThanhToan {
    // List tạm (nếu cần dùng cho logic cũ)
    private static List<ThanhToan> lichSuThanhToan = new ArrayList<>();
    private static double tongDoanhThu = 0;

    private static final DecimalFormat df = new DecimalFormat("#,###");

    // Các trường dữ liệu khớp với bảng 'doanhthu' trong CSDL
    private String maPhong;
    private String tenKhach;
    private double soTien;
    private String thoiGian; // Lưu cột NgayThanhToan
    private String chiTietDichVu; // Lưu cột ChiTietDichVu

    public ThanhToan() {
    }

    // Constructor 1: Dùng khi TẠO MỚI thanh toán (lấy giờ hiện tại)
    public ThanhToan(String maPhong, String tenKhach, double soTien, String chiTietDichVu) {
        this.maPhong = maPhong;
        this.tenKhach = tenKhach;
        this.soTien = soTien;
        this.chiTietDichVu = chiTietDichVu;
        this.thoiGian = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Constructor 2: Dùng khi ĐỌC TỪ DATABASE (lấy giờ lịch sử)
    public ThanhToan(String maPhong, String tenKhach, double soTien, String chiTietDichVu, String thoiGian) {
        this.maPhong = maPhong;
        this.tenKhach = tenKhach;
        this.soTien = soTien;
        this.chiTietDichVu = chiTietDichVu;
        this.thoiGian = thoiGian;
    }

    // --- GETTERS ---
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

    // --- HÀM STATIC HỖ TRỢ LOGIC CŨ (GIỮ LẠI ĐỂ KHÔNG BÁO LỖI Ở CHỖ KHÁC) ---

    public static String ghiNhanThanhToan(Phong phong, double tongTienThucTe, String dsDichVu) {
        if (phong == null || phong.getKhachThue() == null) {
            return "Không thể thanh toán! Phòng và khách không hợp lệ!";
        }

        double soTienCuoi = (tongTienThucTe > 0) ? tongTienThucTe : phong.getGiaPhong();

        // Tạo đối tượng để lưu vào list tạm (quan trọng là bước lưu vào DB ở
        // controller)
        ThanhToan tt = new ThanhToan(
                phong.getMaPhong(),
                phong.getKhachThue().getTen(),
                soTienCuoi,
                dsDichVu);

        lichSuThanhToan.add(tt);
        tongDoanhThu += soTienCuoi;

        return "Thanh toán thành công! Tổng tiền: " + df.format(soTienCuoi) + " VND";
    }

    // Giữ lại để tương thích code cũ (nếu có gọi)
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