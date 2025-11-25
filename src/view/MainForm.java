package view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Class.Person;
import repository.DatabaseConnection;

import repository.IKhachHangRepository;
import repository.KhachHangRepository;
import repository.IPhongRepository;
import repository.PhongRepository;
// Import mới
import repository.INhanVienRepository;
import repository.NhanVienRepository;

public class MainForm extends JFrame {

    public static final Color COLOR_PRIMARY = new Color(76, 175, 80);
    public static final Color COLOR_BACKGROUND = new Color(236, 249, 245);
    public static final Color COLOR_TEXT = new Color(51, 51, 51);
    public static final Color COLOR_HEADER = new Color(56, 142, 60);

    private IKhachHangRepository khachHangRepo;
    private IPhongRepository phongRepo;
    private INhanVienRepository nhanVienRepo; // Khai báo Repo

    public MainForm() {
        khachHangRepo = new KhachHangRepository();
        phongRepo = new PhongRepository();
        nhanVienRepo = new NhanVienRepository(); // Khởi tạo Repo

        setTitle("Hệ thống Quản lý Khách sạn");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(COLOR_HEADER);
        tabbedPane.setForeground(Color.WHITE);

        // Tab 1: Khách Hàng
        JPanel khachHangTab = new KhachHangPanel(khachHangRepo);
        tabbedPane.addTab("  Quản lý Khách Hàng  ", khachHangTab);

        // Tab 2: Nhân Viên (MỚI - Dùng Panel thật)
        JPanel nhanVienTab = new NhanVienPanel(nhanVienRepo);
        tabbedPane.addTab("  Quản lý Nhân Viên  ", nhanVienTab);

        // Tab 3: Phòng
        JPanel phongTab = new PhongPanel(phongRepo, khachHangRepo);
        tabbedPane.addTab("  Quản lý Phòng  ", phongTab);

        // Tab 4: Dịch vụ
        JPanel dichVuTab = createPlaceholderPanel("Chức năng Quản lý Dịch vụ");
        tabbedPane.addTab("  Dịch vụ  ", dichVuTab);

        // Tab 5: Thanh toán
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
        return panel;
    }

    // ===== HÀM FIX LỖI TRÙNG ID =====
    private static void dongBoIdCounter() {
        int maxId = 0;
        // Lấy số lớn nhất từ cả 2 bảng (cắt bỏ KH/NV và chuyển thành số)
        // Ví dụ: KH005 -> lấy số 5.
        String sql = "SELECT MAX(IdNumber) FROM (" +
                "  SELECT CAST(SUBSTRING(MaKH, 3) AS UNSIGNED) AS IdNumber FROM khachhang " +
                "  UNION " +
                "  SELECT CAST(SUBSTRING(MaNV, 3) AS UNSIGNED) AS IdNumber FROM nhanvien" +
                ") AS AllIds";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                maxId = rs.getInt(1);
            }

            // Set biến đếm bắt đầu từ maxId + 1
            // Ví dụ database có KH005 là lớn nhất -> lần sau sẽ tạo KH006
            Person.setCnt(maxId + 1);
            System.out.println("Đã đồng bộ ID. Bắt đầu từ: " + (maxId + 1));

        } catch (SQLException e) {
            e.printStackTrace();
            Person.setCnt(1);
        }
    }

    public static void main(String[] args) {
        // Gọi hàm này TRƯỚC KHI chạy giao diện
        dongBoIdCounter();

        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}