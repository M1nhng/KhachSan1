package Class;

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
    private String thoiGianDatPhong;
    private String thoiGianTraPhong;

    public ThanhToan(String maPhong, String tenKhach, double soTien, String thoiGianDatPhong) {
        this.maPhong = maPhong;
        this.tenKhach = tenKhach;
        this.soTien = soTien;
        this.thoiGianDatPhong = (thoiGianDatPhong != null) ? thoiGianDatPhong : "N/A"; 
        this.thoiGianTraPhong = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss"));
    }

    // ===== GHI NHẬN THANH TOÁN =====
    public static String ghiNhanThanhToan(Phong phong){
    	
        if(phong == null || phong.getKhachThue() == null){
            System.out.println("Khong the thanh toan! Phong va khach khong hop le!");
            return "Thanh toan that bai: Phong hoac khach khong hop le.";
        }

        double soTien = phong.getGiaPhong(); 
        double tienDV = phong.tinhTongTienDichVu();
        double tongTien = soTien + tienDV;
        ThanhToan tt = new ThanhToan(phong.getMaPhong(), phong.getKhachThue().getTen(), tongTien, phong.getThoiGianDatPhong());

        lichSuThanhToan.add(tt);
        tongDoanhThu += tongTien;

        System.out.println("Thanh toan thanh cong! Tien phong: " + df.format(soTien) + " VND! " + "Tien dich vu: " + df.format(tienDV) + "VND! " + "Tong tien: " + df.format(tongTien));
        System.out.println("Thoi gian tra phong: " + tt.thoiGianTraPhong);
        return "Thanh toan thanh cong.";
    }

    // ===== THỰC HIỆN THANH TOÁN (khi trả phòng) =====
    public static void thanhToanPhong() {
        System.out.print("Nhap ma phong can thanh toan: ");
        String maPhong = sc.nextLine().trim();

        Phong phong = timPhongTheoMa(maPhong);
        if (phong == null) {
            System.out.println("Khong tim thay phong " + maPhong);
            return;
        }

        if (!phong.isTrangThai()) {
            System.out.println("Phong nay hien dang trong, khong can thanh toan!");
            return;
        }

        ghiNhanThanhToan(phong);
        phong.traPhong();
    }
    //TRỪ DOANH THU
    public static void truDoanhThu(NhanVien nv) {
        tongDoanhThu -= nv.TinhLuong();
        ThanhToan tt = new ThanhToan("LuongNV", nv.getTen(), -nv.TinhLuong(), "Chi tra luong");
        lichSuThanhToan.add(tt);
        System.out.println("Da thanh toan luong cho nhan vien: " + nv.getTen());
        System.out.println("So tien da chi: " + df.format(nv.TinhLuong()) + " VND");
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

    // ===== TÌM PHÒNG THEO MÃ =====
    private static Phong timPhongTheoMa(String maPhong) {
        try {
            java.lang.reflect.Field field = QuanLyPhong.class.getDeclaredField("dsPhong");
            field.setAccessible(true);
            List<Phong> dsPhong = (List<Phong>) field.get(null);

            for (Phong p : dsPhong) {
                if (p.getMaPhong().equalsIgnoreCase(maPhong)) {
                    return p;
                }
            }
        } catch (Exception e) {
            System.out.println("Loi khi tim phong: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format(
        	"Phong: %s | Khach: %s | Tien: %s VND | Thoi gian: %s - %s",
             maPhong, tenKhach, df.format(soTien), thoiGianDatPhong, thoiGianTraPhong
        );
    }

    public static double getTongDoanhThu() {
        return tongDoanhThu;
    }
}
