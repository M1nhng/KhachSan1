// Tệp: src/model/ThanhToan.java
package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.DecimalFormat;

public class ThanhToan {
    private static List<ThanhToan> lichSuThanhToan = new ArrayList<>();
    private static double tongDoanhThu = 0;
    private static Scanner sc = new Scanner(System.in);
    private static final DecimalFormat df = new DecimalFormat("#,###");

    private String maPhong;
    private String tenKhach;
    private double soTien;
    private String thoiGian;

    public ThanhToan(String maPhong, String tenKhach, double soTien) {
        this.maPhong = maPhong;
        this.tenKhach = tenKhach;
        this.soTien = soTien;
        this.thoiGian = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    // ===== GHI NHẬN THANH TOÁN (ĐÃ SỬA: Trả về String) =====
    public static String ghiNhanThanhToan(Phong phong){
        if(phong == null || phong.getKhachThue() == null){
            // Sửa: return thay vì System.out.println
            return "Khong the thanh toan! Phong va khach khong hop le!";
        }

        double soTien = phong.getGiaPhong();
        ThanhToan tt = new ThanhToan(
                phong.getMaPhong(),
                phong.getKhachThue().getTen(),
                soTien
        );

        lichSuThanhToan.add(tt);
        tongDoanhThu += soTien;

        // Sửa: return thay vì System.out.println
        return "Thanh toan thanh cong! So tien: " + df.format(soTien) + " VND";
    }

    // ===== THỰC HIỆN THANH TOÁN (khi trả phòng - Dùng cho Console Main.java) =====
    public static void thanhToanPhong() {
        System.out.print("Nhap ma phong can thanh toan: ");
        String maPhong = sc.nextLine().trim();

        Phong phong = timPhongTheoMa(maPhong); // Dùng hàm timPhongTheoMa đã sửa
        if (phong == null) {
            System.out.println("Khong tim thay phong " + maPhong);
            return;
        }

        if (!phong.isTrangThai()) {
            System.out.println("Phong nay hien dang trong, khong can thanh toan!");
            return;
        }

        // Gọi hàm ghiNhanThanhToan (bản mới trả về String nhưng ta không gán)
        String thongBao = ghiNhanThanhToan(phong);
        System.out.println(thongBao); // In thông báo ra console

        phong.traPhong(); // Gọi hàm traPhong của Phong
    }

    // ===== XEM LỊCH SỬ THANH TOÁN =====
    public static void xemLichSuThanhToan(){
        if(lichSuThanhToan.isEmpty()){
            System.out.println("Chua co thanh toan nao!");
            return;
        }
        System.out.println("\n===== LICH SU THANH TOAN =====");
        for (ThanhToan tt : lichSuThanhToan) {
            System.out.println(tt);
        }
        System.out.println("================================");
        System.out.println("Tong doanh thu: " + df.format(tongDoanhThu) + " VND\n");
    }

    // ===== XEM DOANH THU =====
    public static void xemDoanhThu() {
        System.out.println("\n===== DOANH THU HIEN TAI =====");
        System.out.println("Tong doanh thu: " + df.format(tongDoanhThu) + " VND");
        System.out.println("================================\n");
    }

    // ===== TÌM PHÒNG THEO MÃ (ĐÃ SỬA: Bỏ Reflection) =====
    private static Phong timPhongTheoMa(String maPhong) {
        // Giờ đây chúng ta có thể gọi thẳng QuanLyPhong.timPhongTheoMa
        // (với điều kiện bạn cũng phải sửa hàm đó trong QuanLyPhong thành public)

        // Tuy nhiên, vì PhongPanel đang dùng PhongRepository (lấy từ CSDL),
        // còn Main.java (console) đang dùng QuanLyPhong (danh sách tĩnh),
        // chúng ta cần đảm bảo logic cho bản console vẫn chạy đúng.

        // Tạm thời, chúng ta sẽ gọi PhongRepository nếu có thể,
        // nhưng cách đơn giản nhất là vẫn dùng Reflection cho bản Console
        // HOẶC sửa QuanLyPhong.java

        // === GIẢI PHÁP ĐƠN GIẢN NHẤT (giữ code cũ của bạn cho bản console): ===
        try {
            // Giữ lại Reflection để tương thích với Main.java (console)
            java.lang.reflect.Field field = QuanLyPhong.class.getDeclaredField("dsPhong");
            field.setAccessible(true);
            List<Phong> dsPhong = (List<Phong>) field.get(null);

            for (Phong p : dsPhong) {
                if (p.getMaPhong().equalsIgnoreCase(maPhong)) {
                    return p;
                }
            }
        } catch (Exception e) {
            System.out.println("Loi khi tim phong (tu ThanhToan): " + e.getMessage());
        }
        return null;

        /* // === GIẢI PHÁP SẠCH HƠN (nếu bạn sửa QuanLyPhong.timPhongTheoMa thành public): ===
        // return QuanLyPhong.timPhongTheoMa(maPhong);
        */
    }

    @Override
    public String toString() {
        return String.format(
                "Phong: %s | Khach: %s | Tien: %s VND | Thoi gian: %s",
                maPhong, tenKhach, df.format(soTien), thoiGian
        );
    }

    public static double getTongDoanhThu() {
        return tongDoanhThu;
    }
}