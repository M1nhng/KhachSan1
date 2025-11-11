package Class;

public class Person {
    private String maID;
    private String ten;
    private String soCMND;
    private String soDienThoai;

    protected static int cnt = 1;


    public Person() {
       
    }

    public Person(String prefix) {
        this.maID = String.format("%s%03d", prefix, cnt++);
    }

    public Person(String prefix, String ten, String soCMND, String soDienThoai) {
        this(prefix);
        setTen(ten);
        setSoCMND(soCMND);
        setSoDienThoai(soDienThoai);
    }

    // ===== GETTER =====
    public String getMaID() {
        return maID;
    }

    public String getTen() {
        return ten;
    }

    public String getSoCMND() {
        return soCMND;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setMaID(String maID) {
        this.maID = maID;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setSoCMND(String soCMND) {
        this.soCMND = soCMND;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public static void setCnt(int cnt) {
        Person.cnt = cnt;
    }
}