package view; 

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Class.Person; 
import repository.DatabaseConnection;

// Import các repository bạn cần
import repository.IKhachHangRepository;
import repository.KhachHangRepository;
import repository.IPhongRepository;
import repository.PhongRepository;
// import repository.INhanVienRepository; // (sẽ cần sau)
// import repository.NhanVienRepository; // (sẽ cần sau)

public class MainForm extends JFrame {

    // Định nghĩa màu xanh lá cây
    public static final Color COLOR_PRIMARY = new Color(76, 175, 80); // Xanh lá chính
    public static final Color COLOR_BACKGROUND = new Color(236, 249, 245); // Xanh lá rất nhạt
    public static final Color COLOR_TEXT = new Color(51, 51, 51); // Màu chữ
    public static final Color COLOR_HEADER = new Color(56, 142, 60); // Xanh lá đậm cho header

    // Khai báo các Repository
    private IKhachHangRepository khachHangRepo;
    private IPhongRepository phongRepo;
    // private INhanVienRepository nhanVienRepo; // (sẽ cần sau)

    public MainForm() {
        // Khởi tạo các repository
        khachHangRepo = new KhachHangRepository();
        // nhanVienRepo = new NhanVienRepository(); // (sẽ cần sau)
        phongRepo = new PhongRepository();

        // --- Cài đặt JFrame ---
        setTitle("Hệ thống Quản lý Khách sạn");
        setSize(1200, 800);
        setLocationRelativeTo(null); // Giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Tạo JTabbedPane ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(COLOR_HEADER); // Màu nền của các tab
        tabbedPane.setForeground(Color.WHITE); // Màu chữ của các tab

        JPanel khachHangTab = new KhachHangPanel(khachHangRepo);
        tabbedPane.addTab("  Quản lý Khách Hàng  ", khachHangTab);

        JPanel nhanVienTab = createPlaceholderPanel("Chức năng Quản lý Nhân viên");
        tabbedPane.addTab("  Quản lý Nhân Viên  ", nhanVienTab);

        // SỬA: Thay thế panel giữ chỗ bằng PhongPanel thật
        // Truyền các repository cần thiết vào constructor của PhongPanel
        JPanel phongTab = new PhongPanel(phongRepo, khachHangRepo);
        tabbedPane.addTab("  Quản lý Phòng  ", phongTab);


        JPanel thanhToanTab = createPlaceholderPanel("Chức năng Quản lý Thanh Toán");
        tabbedPane.addTab("  Thanh toán & Doanh thu  ", thanhToanTab);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPlaceholderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BACKGROUND);
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(COLOR_PRIMARY);
        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }
    
    private static void dongBoIdCounter() {
        int maxId = 0;
        // Câu SQL này lấy ID SỐ lớn nhất từ cả 2 bảng
        // (Bằng cách cắt bỏ 'KH', 'NV' và chuyển sang số)
        String sql = "SELECT MAX(IdNumber) FROM (" +
                "  SELECT CAST(SUBSTRING(MaKH, 3) AS UNSIGNED) AS IdNumber FROM khachhang " +
                "  UNION " +
                "  SELECT CAST(SUBSTRING(MaNV, 3) AS UNSIGNED) AS IdNumber FROM nhanvien" +
                ") AS AllIds";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                maxId = rs.getInt(1); // Lấy ID lớn nhất
            }

            // Thiết lập 'cnt' của Person.java thành giá trị (lớn nhất + 1)
            Person.setCnt(maxId + 1);
            // System.out.println("Dong bo ID counter thanh cong. Gia tri bat dau: " + (maxId + 1));

        } catch (SQLException e) {
            // System.err.println("Loi khi dong bo ID counter! Dat gia tri mac dinh = 1.");
            e.printStackTrace();
            Person.setCnt(1); // Quay về mặc định nếu lỗi CSDL
        }
    }

    /**
     * Phương thức Main để chạy ứng dụng
     */
    public static void main(String[] args) {

        // ===== BƯỚC FIX LỖI TRÙNG KEY =====
        // Đồng bộ ID counter từ CSDL trước khi làm bất cứ điều gì khác
        dongBoIdCounter();
        // ===================================

        // Chạy giao diện
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}