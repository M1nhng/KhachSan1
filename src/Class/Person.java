package Class;

public class Person {
    private String maID;
    private String ten;
    private String soCMND;
    private String soDienThoai;

    // Biến đếm static dùng chung để tạo ID tăng dần
    protected static int cnt = 1;

    public Person() {
    }

    // Constructor tạo ID tự động dựa trên biến cnt
    public Person(String prefix) {
        // ID sẽ là prefix + cnt hiện tại (ví dụ: KH005)
        this.maID = String.format("%s%03d", prefix, cnt++);
    }

    public Person(String prefix, String ten, String soCMND, String soDienThoai) {
        this(prefix); // Gọi constructor trên để tạo ID
        setTen(ten);
        setSoCMND(soCMND);
        setSoDienThoai(soDienThoai);
    }

    // ... (Giữ nguyên các Getter và Setter bên dưới) ...
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

    // Hàm này quan trọng: MainForm sẽ gọi hàm này để đồng bộ số đếm
    public static void setCnt(int cnt) {
        Person.cnt = cnt;
    }
}