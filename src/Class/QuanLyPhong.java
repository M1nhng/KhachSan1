package Class;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuanLyPhong {
    private static final List<Phong> dsPhong = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);

    // ===== KHỞI TẠO DANH SÁCH PHÒNG 101 -> 110 =====
    static {
        for (int i = 101; i <= 108; i++) {
            dsPhong.add(new Phong(String.valueOf(i), "Thuong", 500000, false));
        }
        for (int i = 109; i <= 110; i++) {
            dsPhong.add(new Phong(String.valueOf(i), "VIP", 800000, false));
        }
    }

    // (MỚI) Thêm hàm này để PhongPanel (giao diện) có thể lấy dữ liệu
    public static List<Phong> getDsPhong() {
        return dsPhong;
    }

    // ===== HIỂN THỊ DANH SÁCH PHÒNG =====
    public static void xemDanhSachPhong() {
        System.out.println("\n===== DANH SACH PHONG =====");
        for (Phong p : dsPhong) {
            System.out.println(p);
            if (p.isTrangThai() && p.getKhachThue() != null) {
                System.out.println("Khach thue: " + p.getKhachThue().getTen() +
                        " (Ma KH: " + p.getKhachThue().getMaID() + ")");
            }
        }
        System.out.println("=============================\n");
    }

    // ===== ĐẶT PHÒNG (ĐÃ SỬA) =====
    public static void datPhong(List<KhachHang> dsKhach) {
        System.out.print("Nhap ma phong can dat: ");
        String maPhong = sc.nextLine().trim();

        Phong phong = timPhongTheoMa(maPhong);
        if (phong == null) {
            System.out.println("Khong tim thay phong " + maPhong);
            return;
        }

        // Bỏ check if(phong.isTrangThai()) vì hàm phong.datPhong() sẽ làm việc đó

        System.out.print("Nhap ma khach hang dat phong: ");
        String maKH = sc.nextLine().trim();

        KhachHang kh = timKhachHangTheoMa(dsKhach, maKH);
        if (kh == null) {
            System.out.println("Khong tim thay khach hang co ma " + maKH);
            return;
        }

        // Sửa lỗi: Nhận String trả về từ hàm datPhong() và in ra
        String ketQua = phong.datPhong(kh);
        System.out.println(ketQua);
    }

    // ===== TRẢ PHÒNG (ĐÃ SỬA) =====
    public static void traPhong() {
        System.out.print("Nhap ma phong can tra: ");
        String maPhong = sc.nextLine().trim();

        Phong phong = timPhongTheoMa(maPhong);
        if (phong == null) {
            System.out.println("Khong tim thay phong " + maPhong);
            return;
        }

        // Bỏ check if(!phong.isTrangThai()) vì hàm phong.traPhong() sẽ làm việc đó

        // Sửa lỗi: Nhận String trả về từ hàm traPhong()
        String ketQuaTraPhong = phong.traPhong();
        System.out.println(ketQuaTraPhong);

        // Chỉ ghi nhận thanh toán NẾU trả phòng thành công
        // (Kiểm tra xem thông báo trả về có phải là thông báo lỗi không)
        if (!ketQuaTraPhong.contains("dang trong")) {
            // Gọi hàm ghiNhanThanhToan (hàm này cũng trả về String sau khi sửa ở bước
            // trước)
            String ketQuaThanhToan = ThanhToan.ghiNhanThanhToan(phong);
            System.out.println(ketQuaThanhToan);
        }
    }

    // ==== Xoa Phong (ĐÃ SỬA LOGIC + SỬA LỖI) =====
    public static void xoaPhong() {
        System.out.print("Nhap ma phong can xoa: ");
        String maPhong = sc.nextLine().trim();

        Phong phong = timPhongTheoMa(maPhong);
        if (phong == null) {
            System.out.println("Khong tim thay phong " + maPhong);
            return;
        }

        // SỬA LOGIC: Chỉ được xóa phòng TRỐNG
        if (phong.isTrangThai()) {
            System.out.println("Phong nay dang co nguoi thue, khong the xoa!");
            return;
        }

        // SỬA LỖI: Bỏ hàm if(phong.traPhong())
        dsPhong.remove(phong); // Xóa thẳng khỏi danh sách
        System.out.println("Xoa phong " + maPhong + " thanh cong!");
    }

    // ===== XEM KHÁCH ĐANG Ở PHÒNG =====
    public static void xemKhachTheoPhong() {
        System.out.print("Nhap ma phong muon xem thong tin khach: ");
        String maPhong = sc.nextLine().trim();

        Phong phong = timPhongTheoMa(maPhong);
        if (phong == null) {
            System.out.println("Khong tim thay phong " + maPhong);
            return;
        }

        if (!phong.isTrangThai() || phong.getKhachThue() == null) {
            System.out.println("Phong " + maPhong + " hien dang trong.");
            return;
        }

        KhachHang kh = phong.getKhachThue();
        System.out.println("\n===== THONG TIN KHACH THUE =====");
        System.out.println("Phong: " + maPhong);
        System.out.println("Ho ten: " + kh.getTen());
        System.out.println("Ma KH: " + kh.getMaID());
        System.out.println("CMND: " + kh.getSoCMND());
        System.out.println("SDT: " + kh.getSoDienThoai());
        System.out.println("================================\n");
    }

    // ===== TÌM PHÒNG THEO MÃ (SỬA: thành public để ThanhToan.java có thể dùng)
    // =====
    public static Phong timPhongTheoMa(String maPhong) {
        for (Phong p : dsPhong) {
            if (p.getMaPhong().equalsIgnoreCase(maPhong)) {
                return p;
            }
        }
        return null;
    }

    // ===== TÌM KHÁCH HÀNG THEO MÃ =====
    private static KhachHang timKhachHangTheoMa(List<KhachHang> dsKhach, String maKH) {
        for (KhachHang kh : dsKhach) {
            if (kh.getMaID().equalsIgnoreCase(maKH)) {
                return kh;
            }
        }
        return null;
    }

}