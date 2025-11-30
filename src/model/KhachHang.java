package model;

public class KhachHang extends Person {
    private String email;

    public KhachHang() {
        super();
    }

    public KhachHang(String maID, String ten, String soCMND, String soDienThoai, String email) {
        super(maID, ten, soCMND, soDienThoai);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getVaiTro() {
        return "Khách Hàng";
    }

    @Override
    public String toString() {
        return String.format("Khách hàng [%s] %s - %s", getMaID(), getTen(), getSoCMND());
    }
}