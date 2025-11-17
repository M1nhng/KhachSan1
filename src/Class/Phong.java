package Class;

import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;

public class Phong {
    private String maPhong;
    private String loaiPhong;
    private double giaPhong;
    private boolean trangThai;
    private KhachHang KhachThue;
    private String thoiGianDatPhong;
    private static final DecimalFormat df = new DecimalFormat("#,###");

    public Phong(String maPhong, String loaiPhong, double giaPhong, boolean trangThai) {
        setMaPhong(maPhong);
        setLoaiPhong(loaiPhong);
        setGiaPhong(giaPhong);
        setTrangThai(trangThai);
    }

    public Phong() {
        
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
    
    public String getThoiGianDatPhong() {
        return thoiGianDatPhong;
    }

    public void setThoiGianDatPhong(String thoiGianDatPhong) {
        this.thoiGianDatPhong = thoiGianDatPhong;
    }

    // ===== ĐẶT PHÒNG =====
    public String datPhong(KhachHang khach) {
        if (trangThai) {
            return "Phong " + maPhong + " da co nguoi thue!";
        }
        this.trangThai = true;
        this.KhachThue = khach;
        this.thoiGianDatPhong = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss"));

        return "Phong " + maPhong + " da duoc dat cho khach: " + khach.getTen() +
        ", Thoi gian dat phong: " + this.thoiGianDatPhong;
    }

    // ===== TRẢ PHÒNG =====
    public String traPhong() {
        if (!trangThai) {
            return "Phong " + maPhong + " dang trong!";
        }
        this.trangThai = false;
        this.KhachThue = null;
        this.thoiGianDatPhong = null;
        return "Phong " + maPhong + " da duoc tra.";
    }

    @Override
    public String toString() {
        return String.format(
            "Phong %s | Loai: %s | Gia: %s VND | Trang thai: %s",
            maPhong, loaiPhong, df.format(giaPhong),
            (trangThai ? "Da thue" : "Trong")
        );
    }
}
