package Class;

import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;

public class NhanVien extends Person {
    private double luongCoBan;
    private String chucvu;
    private static ArrayList<NhanVien> dsNV = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);
    private static final DecimalFormat df = new DecimalFormat("#,###"); 

    // ===== CONSTRUCTOR =====
    public NhanVien(){}
    
    public NhanVien(String ten, String soCMND, String soDienThoai, double luongCoBan,String chucvu) {
        super("NV",ten, soCMND, soDienThoai);
        setLuongCoBan(luongCoBan);
        setChucVu(chucvu);
    }

    // ===== GETTER & SETTER =====
    public double getLuongCoBan() {
        return luongCoBan;
    }
    public String getChucVu(){
        return chucvu;
    }

    public void setLuongCoBan(double luongCoBan) {
        if (luongCoBan >= 0) {
            this.luongCoBan = luongCoBan;
        }
    }
    public void setChucVu(String chucvu){
        if(!chucvu.equals("")) {
        	this.chucvu = chucvu;
        }
    }

    // ===== HIỂN THỊ THÔNG TIN =====
    @Override
    public String toString() {
        return String.format(
        	"Nhan vien [ID=%s, Ten=%s, CMND=%s, SDT=%s, Luong=%s VND, Chucvu = %s]",
             getMaID(), getTen(), getSoCMND(), getSoDienThoai(), df.format(getLuongCoBan()),getChucVu()
        );
    }

    // ===== CÁC PHƯƠNG THỨC QUẢN LÝ =====

    // Thêm nhân viên
    public static void themNhanVien() {
        System.out.print("Nhap ten nhan vien: ");
        String ten = sc.nextLine();

        System.out.print("Nhap so CMND: ");
        String cmnd = sc.nextLine();

        System.out.print("Nhap so dien thoai: ");
        String sdt = sc.nextLine();

        System.out.print("Nhap luong co ban: ");
        double luong = Double.parseDouble(sc.nextLine());

        System.out.print("Nhap chuc vu: ");
        String chucvu = sc.nextLine();

        NhanVien nv = new NhanVien(ten, cmnd, sdt, luong,chucvu);
        dsNV.add(nv);
        System.out.println("Da them nhan vien thanh cong!");
    }

    // Xóa nhân viên
    public static void xoaNhanVien() {
        System.out.print("Nhap ma ID nhan vian can xoa: ");
        String id = sc.nextLine();

        NhanVien found = null;
        for (NhanVien nv : dsNV) {
            if (nv.getMaID().equals(id)) {
                found = nv;
                break;
            }
        }

        if (found != null) {
            dsNV.remove(found);
            System.out.println("Da xoa nhan vien: " + id);
        } else {
            System.out.println("Khong tim thay nhan vien co ID: " + id);
        }
    }

    // Sửa thông tin nhân viên
    public static void suaNhanVien() {
        System.out.print("Nhap ma ID nhan vien can sua: ");
        String id = sc.nextLine();

        NhanVien found = null;
        for (NhanVien nv : dsNV) {
            if (nv.getMaID().equals(id)) {
                found = nv;
                break;
            }
        }

        if (found == null) {
            System.out.println("Khong tim thay nhan vien co ID: " + id);
            return;
        }

        System.out.print("Nhap ten moi: ");
        String ten = sc.nextLine();
        if (!ten.isEmpty()) found.setTen(ten);

        System.out.print("Nhap CMND moi: ");
        String cmnd = sc.nextLine();
        if (!cmnd.isEmpty()) found.setSoCMND(cmnd);

        System.out.print("Nhap SDT moi: ");
        String sdt = sc.nextLine();
        if (!sdt.isEmpty()) found.setSoDienThoai(sdt);

        System.out.print("Nhap luong moi: ");
        String luongTxt = sc.nextLine();
        if (!luongTxt.isEmpty()) {
            try {
                double luong = Double.parseDouble(luongTxt);
                found.setLuongCoBan(luong);
            } catch (NumberFormatException e) {
                System.out.println("Luong khong hop le, giu nguyen gia tri cu.");
            }
        }

        System.out.print("Nhap chuc vu moi: ");
        String chucVu = sc.nextLine();
        if (!chucVu.isEmpty())
            found.setChucVu(chucVu);
        else System.out.println("Chuc vu khong duoc de trong, giu nguyen gia tri cu.");
        
        System.out.println("Da cap nhat thong tin nhan vien!");
    }

    // Xem danh sách nhân viên
    public static void xemNhanVien() {
        if (dsNV.isEmpty()) {
            System.out.println("Danh sach nhan vien trong!");
            return;
        }

        System.out.println("\n===== DANH SACH NHAN VIEN =====");
        for (NhanVien nv : dsNV) {
            System.out.println(nv);
        }
    }
    
  //Tính lương
    public double TinhLuong() {
        double heSo = 1.0;
        if (chucvu != null) {
            switch (chucvu.toLowerCase()) {
                case "quan ly" -> heSo = 2.0;
                case "le tan" -> heSo = 1.2;
                case "phuc vu" -> heSo = 1.0;
            }
        }
        return luongCoBan * heSo;
    }
    public static void ThanhToanLuong() {
        if (dsNV.isEmpty()) {
            System.out.println("Chua co nhan vien nao!");
            return;
        }
        System.out.print("Nhap ma nhan vien can thanh toan: ");
        String id = sc.nextLine().trim();

        NhanVien nv = null;
        for (NhanVien n : dsNV) {
            if (n.getMaID().equalsIgnoreCase(id)) {
                nv = n;
                break;
            }
        }
        if (nv == null) {
            System.out.println("Khong tim thay nhan vien: " + id);
            return;
        }

        double luong = nv.TinhLuong();
        System.out.println("Luong cua " + nv.getTen() + " la: " + df.format(luong) + " VND");
        System.out.print("Xac nhan thanh toan? (y/n): ");
        String xacnhan = sc.nextLine();
        if (xacnhan.equalsIgnoreCase("y")) {
            ThanhToan.truDoanhThu(nv);
        } else {
            System.out.println("Da huy thanh toan");
        }
    }
}
