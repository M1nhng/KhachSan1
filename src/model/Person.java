package model;

public abstract class Person {
    private String maID;
    private String ten;
    private String soCMND;
    private String soDienThoai;

    public Person() {
    }

    public Person(String maID, String ten, String soCMND, String soDienThoai) {
        this.maID = maID;
        this.ten = ten;
        this.soCMND = soCMND;
        this.soDienThoai = soDienThoai;
    }

    // Phương thức trừu tượng (Abstraction)
    public abstract String getVaiTro();

    public String getMaID() {
        return maID;
    }

    public void setMaID(String maID) {
        this.maID = maID;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSoCMND() {
        return soCMND;
    }

    public void setSoCMND(String soCMND) {
        this.soCMND = soCMND;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}