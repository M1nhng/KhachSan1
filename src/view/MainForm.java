package view;

import javax.swing.*;
import java.awt.*;

import repository.IKhachHangRepository;
import repository.KhachHangRepository;
import repository.INhanVienRepository;
import repository.NhanVienRepository;
import repository.IPhongRepository;
import repository.PhongRepository;
import repository.IDichVuRepository;
import repository.DichVuRepository;


public class MainForm extends JFrame {

    public static final Color COLOR_PRIMARY = new Color(76, 175, 80);
    public static final Color COLOR_BACKGROUND = new Color(236, 249, 245);
    public static final Color COLOR_TEXT = new Color(51, 51, 51);
    public static final Color COLOR_HEADER = new Color(56, 142, 60);

    private IKhachHangRepository khachHangRepo;
    private INhanVienRepository nhanVienRepo;
    private IPhongRepository phongRepo;
    private IDichVuRepository dichVuRepo;

    public MainForm() {
        // 1. Khởi tạo các Repository (Kết nối thực tế đến Database)
        khachHangRepo = new KhachHangRepository();
        nhanVienRepo = new NhanVienRepository();
        phongRepo = new PhongRepository();
        // ((PhongRepository) phongRepo).khoiTaoPhongChoMenu();
        dichVuRepo = new DichVuRepository();

        setTitle("Hệ thống Quản lý Khách sạn");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(COLOR_HEADER);
        tabbedPane.setForeground(Color.WHITE);
        JPanel khachHangTab = new KhachHangPanel(khachHangRepo);
        tabbedPane.addTab("  Quản lý Khách Hàng  ", khachHangTab);

        JPanel nhanVienTab = new NhanVienPanel(nhanVienRepo);
        tabbedPane.addTab("  Quản lý Nhân Viên  ", nhanVienTab);

        JPanel dichVuTab = new DichVuPanel(dichVuRepo);
        tabbedPane.addTab("  Quản lý Dịch Vụ  ", null, dichVuTab, "Danh mục dịch vụ khách sạn");

        JPanel phongTab = new PhongPanel(phongRepo, khachHangRepo, dichVuRepo);
        tabbedPane.addTab("  Quản lý Phòng  ", null, phongTab, "Sơ đồ phòng và đặt phòng");

        JPanel thanhToanTab = new ThanhToanPanel(phongRepo, dichVuRepo);
        tabbedPane.addTab("  Thanh Toán   ", null, thanhToanTab, "Lập hóa đơn và thanh toán");

        JPanel thongKeTab = new DoanhThuPanel();
        tabbedPane.addTab("  Doanh Thu  ", null, thongKeTab, "Xem doanh thu theo tuần/năm");
        add(tabbedPane, BorderLayout.CENTER);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Tạo và hiện cửa sổ Đăng nhập trước
            LoginView loginView = new LoginView();
            loginView.setLoginHandler((username, password, remember) -> {
                String passStr = new String(password);
                // Kiểm tra mật khẩu cứng (Hardcoded)
                if (username.equals("admin") && passStr.equals("1234")) {
                    loginView.dispose();
                    new MainForm().setVisible(true);
                } else {
                    loginView.showError("Sai tên đăng nhập hoặc mật khẩu!");
                }
            });

            loginView.setVisible(true);
        });
    }
}
