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

    // ===== HIỂN THỊ DANH SÁCH PHÒNG =====
    public static void xemDanhSachPhong() {
        System.out.println("\n===== DANH SACH PHONG =====");
        for (Phong p : dsPhong) {
            System.out.println(p);
            if (p.isTrangThai() && p.getKhachThue() != null) {
                System.out.println("   -> Khach thue: " + p.getKhachThue().getTen() +
                        " (Ma KH: " + p.getKhachThue().getMaID() + ")" +
                        " - Dat luc: " + p.getThoiGianDatPhong());
            }
        }
        System.out.println("=============================\n");
    }

    // ===== ĐẶT PHÒNG =====
    public static void datPhong(List<KhachHang> dsKhach) {
        System.out.print("Nhap ma phong can dat: ");
        String maPhong = sc.nextLine().trim();

        Phong phong = timPhongTheoMa(maPhong);
        if (phong == null) {
            System.out.println("Khong tim thay phong " + maPhong);
            return;
        }

        System.out.print("Nhap ma khach hang dat phong: ");
        String maKH = sc.nextLine().trim();

        KhachHang kh = timKhachHangTheoMa(dsKhach, maKH);
        if (kh == null) {
            System.out.println("Khong tim thay khach hang co ma " + maKH);
            return;
        }

        String ketQua = phong.datPhong(kh);
        System.out.println(ketQua);
    }

    // ===== TRẢ PHÒNG =====
    public static void traPhong() {
        System.out.print("Nhap ma phong can tra: ");
        String maPhong = sc.nextLine().trim();

        Phong phong = timPhongTheoMa(maPhong);
        if (phong == null) {
            System.out.println("Khong tim thay phong " + maPhong);
            return;
        }

        if (!phong.isTrangThai()) {
            System.out.println("Phong nay dang trong, khong the tra!");
            return;
        }
        
        ThanhToan.ghiNhanThanhToan(phong);
        phong.traPhong();
    }
    //==== Xoa Phong =====
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
    
    public static void themDichVuVaoPhong(Scanner sc) {
        System.out.print("Nhap ma phong can them dich vu: ");
        String maPhong = sc.nextLine();

        Phong phong = null;
        for (Phong p : dsPhong) {
            if (p.getMaPhong().equalsIgnoreCase(maPhong)) {
                phong = p;
                break;
            }
        }

        if (phong == null) {
            System.out.println("Khong tim thay phong!");
            return;
        }

        System.out.print("Nhap ten dich vu: ");
        String tenDV = sc.nextLine();

        System.out.print("Nhap gia dich vu: ");
        double giaDV = sc.nextDouble();
        sc.nextLine();

        DichVu dv = new DichVu(tenDV, giaDV);
            phong.themDichVu(dv);
        }

        public static void xemDichVuTheoPhong(Scanner sc) {
	        System.out.print("Nhap ma phong: ");
	        String maPhong = sc.nextLine();
	
	        for (Phong p : dsPhong) {
	            if (p.getMaPhong().equalsIgnoreCase(maPhong)) {
	                if (p.getDsDichVu().isEmpty()) {
	                    System.out.println("Phong chua co dich vu nao!");
	                } else {
	                    System.out.println("Cac dich vu cua phòng " + maPhong + ":");
	                    for (DichVu dv : p.getDsDichVu()) {
	                        System.out.println(" - " + dv.getTenDichVu());
	                    }
	                }
	                return;
	            }
	        }
	        System.out.println("Khong tim thay phong!");
	    }
}
