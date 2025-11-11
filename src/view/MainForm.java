package view; // Bạn có thể đặt tên package là View, UI, or GiaoDien

import javax.swing.*;
import java.awt.*;

// Import các repository bạn cần
import repository.IKhachHangRepository;
import repository.KhachHangRepository;
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
    // private INhanVienRepository nhanVienRepo; // (sẽ cần sau)

    public MainForm() {
        // Khởi tạo các repository
        khachHangRepo = new KhachHangRepository();
        // nhanVienRepo = new NhanVienRepository(); // (sẽ cần sau)

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

       
        JPanel phongTab = createPlaceholderPanel("Chức năng Quản lý Phòng");
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

    /**
     * Phương thức Main để chạy ứng dụng
     */
    public static void main(String[] args) {
        // Đảm bảo đồng bộ ID trước khi chạy (nếu cần)
        // Ghi chú: Việc đồng bộ Person.cnt
        // cần được xử lý cẩn thận hơn khi dùng CSDL
        // (ví dụ: lấy ID lớn nhất từ CSDL khi khởi động)

        // Chạy giao diện
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}