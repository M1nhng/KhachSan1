package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import model.KhachHang;
import model.Phong;
import model.ThanhToan; // Sử dụng lại để ghi nhận doanh thu
import repository.IKhachHangRepository;
import repository.IPhongRepository;

public class PhongPanel extends JPanel {

    private IPhongRepository phongRepo;
    private IKhachHangRepository khachHangRepo;

    private JPanel roomCardsPanel; // Panel chứa tất cả các RoomCard
    private JScrollPane scrollPane;

    // Định nghĩa màu sắc (lấy từ logic của RoomCard cũ)
    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;
    private static final Color COLOR_AVAILABLE_BG = new Color(240, 255, 240); // Xanh nhạt cho phòng trống
    private static final Color COLOR_RENTED_BG = new Color(255, 240, 240); // Đỏ nhạt cho phòng đã thuê
    private static final Color COLOR_BORDER = new Color(200, 200, 200);
    private static final Color COLOR_TEXT_HEADER = new Color(50, 50, 50);
    private static final Color COLOR_TEXT_NORMAL = new Color(80, 80, 80);
    private static final Color COLOR_BUTTON_AVAILABLE = new Color(46, 204, 113); // Xanh đặt phòng
    private static final Color COLOR_BUTTON_RENTED = new Color(231, 76, 60); // Đỏ trả phòng
    private static final Color COLOR_BUTTON_TEXT = Color.WHITE;

    private DecimalFormat vndFormat;

    public PhongPanel(IPhongRepository phongRepo, IKhachHangRepository khachHangRepo) {
        this.phongRepo = phongRepo;
        this.khachHangRepo = khachHangRepo;

        // Setup formatter
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("###,### VND", symbols);

        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        loadPhongCards();
    }

    private void initComponents() {
        // --- Header (Tiêu đề, nút refresh) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Danh sách Phòng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(MainForm.COLOR_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton btnRefresh = CustomStyler.createStyledButton("Làm Mới");
        btnRefresh.setPreferredSize(new Dimension(120, 40));
        btnRefresh.addActionListener(e -> loadPhongCards());
        JPanel refreshButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButtonPanel.setOpaque(false);
        refreshButtonPanel.add(btnRefresh);
        headerPanel.add(refreshButtonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Panel chứa các thẻ phòng (CENTER) ---
        roomCardsPanel = new JPanel();
        // Sử dụng FlowLayout để các thẻ tự động xuống hàng
        roomCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        roomCardsPanel.setBackground(COLOR_BACKGROUND);

        scrollPane = new JScrollPane(roomCardsPanel);
        scrollPane.getViewport().setBackground(COLOR_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Bỏ border mặc định
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Tăng tốc độ cuộn

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Tải và hiển thị các thẻ phòng
     */
    private void loadPhongCards() {
        roomCardsPanel.removeAll(); // Xóa tất cả các thẻ cũ
        List<Phong> danhSachPhong = phongRepo.getAll(); // Lấy dữ liệu mới nhất từ DB

        if (danhSachPhong.isEmpty()) {
            JLabel noRoomsLabel = new JLabel("Chưa có phòng nào trong hệ thống.", SwingConstants.CENTER);
            noRoomsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            noRoomsLabel.setForeground(MainForm.COLOR_TEXT);
            // Cần set layout cho roomCardsPanel để label căn giữa
            roomCardsPanel.setLayout(new BorderLayout());
            roomCardsPanel.add(noRoomsLabel, BorderLayout.CENTER);
        } else {
            // Đặt lại layout FlowLayout nếu trước đó đã set BorderLayout
            roomCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
            for (Phong phong : danhSachPhong) {
                // Tạo một RoomCard (JPanel) cho mỗi phòng
                JPanel card = createRoomCard(phong);
                roomCardsPanel.add(card);
            }
        }
        roomCardsPanel.revalidate();
        roomCardsPanel.repaint();
    }

    /**
     * Phương thức này tạo ra một JPanel (thẻ phòng) cho một đối tượng Phong cụ thể.
     */
    private JPanel createRoomCard(Phong phong) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // ===== SỬA ĐỔI CHÍNH Ở ĐÂY =====
        // Tăng chiều cao ưu tiên để chứa dòng khách thuê
        card.setPreferredSize(new Dimension(220, 200));
        // ================================

        // --- Header (Mã phòng và trạng thái) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblMaPhong = new JLabel(phong.getMaPhong(), SwingConstants.LEFT);
        lblMaPhong.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblMaPhong.setForeground(COLOR_TEXT_HEADER);

        JLabel lblTrangThai = new JLabel("", SwingConstants.RIGHT);
        lblTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTrangThai.setBorder(new EmptyBorder(0, 0, 0, 5));

        headerPanel.add(lblMaPhong, BorderLayout.WEST);
        headerPanel.add(lblTrangThai, BorderLayout.EAST);
        card.add(headerPanel, BorderLayout.NORTH);

        // --- Body (Loại phòng, giá, khách thuê) ---
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setOpaque(false);
        bodyPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel lblLoaiPhong = new JLabel(phong.getLoaiPhong());
        lblLoaiPhong.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblLoaiPhong.setForeground(COLOR_TEXT_NORMAL);
        lblLoaiPhong.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblGiaPhong = new JLabel(vndFormat.format(phong.getGiaPhong()));
        lblGiaPhong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblGiaPhong.setForeground(MainForm.COLOR_PRIMARY);
        lblGiaPhong.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblKhachThue = new JLabel();
        lblKhachThue.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblKhachThue.setForeground(COLOR_TEXT_NORMAL);
        lblKhachThue.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblKhachThue.setBorder(new EmptyBorder(5, 0, 0, 0)); // Padding trên

        bodyPanel.add(lblLoaiPhong);
        bodyPanel.add(Box.createVerticalStrut(3));
        bodyPanel.add(lblGiaPhong);
        bodyPanel.add(Box.createVerticalStrut(5));
        bodyPanel.add(lblKhachThue);

        card.add(bodyPanel, BorderLayout.CENTER);

        // --- Footer (Nút hành động) ---
        JButton btnAction = new JButton();
        btnAction.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAction.setForeground(COLOR_BUTTON_TEXT);
        btnAction.setFocusPainted(false);
        btnAction.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(btnAction);
        card.add(buttonWrapper, BorderLayout.SOUTH);

        // Cập nhật giao diện dựa trên trạng thái
        if (phong.isTrangThai()) {
            card.setBackground(COLOR_RENTED_BG);
            lblTrangThai.setText("Đã Thuê");
            lblTrangThai.setForeground(COLOR_BUTTON_RENTED);
            btnAction.setText("Trả Phòng");
            btnAction.setBackground(COLOR_BUTTON_RENTED);
            if (phong.getKhachThue() != null) {
                lblKhachThue.setText("Khách: " + phong.getKhachThue().getTen());
            } else {
                lblKhachThue.setText("Khách: [Không rõ]");
            }
        } else {
            card.setBackground(COLOR_AVAILABLE_BG);
            lblTrangThai.setText("Trống");
            lblTrangThai.setForeground(COLOR_BUTTON_AVAILABLE);
            btnAction.setText("Đặt Phòng");
            btnAction.setBackground(COLOR_BUTTON_AVAILABLE);
            lblKhachThue.setText(" "); // Dùng khoảng trắng để giữ layout
        }

        // Thêm sự kiện Click cho nút
        btnAction.addActionListener(e -> {
            if (phong.isTrangThai()) {
                traPhong(phong);
            } else {
                datPhong(phong);
            }
        });

        // Thêm hiệu ứng hover cho nút
        Color originalColor = btnAction.getBackground();
        Color hoverColor = originalColor.darker();
        btnAction.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnAction.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                btnAction.setBackground(originalColor);
            }
        });

        return card;
    }

    /**
     * Xử lý logic đặt phòng cho một phòng cụ thể (Sử dụng JComboBox)
     */
    private void datPhong(Phong phong) {
        // 1. Lấy danh sách khách hàng từ CSDL
        List<KhachHang> dsKhach = khachHangRepo.getAll();
        if (dsKhach == null || dsKhach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có khách hàng nào trong CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Tạo JComboBox với danh sách khách hàng
        JComboBox<KhachHang> khachHangComboBox = new JComboBox<>(dsKhach.toArray(new KhachHang[0]));

        // 3. Tùy chỉnh cách JComboBox hiển thị tên khách hàng
        khachHangComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof KhachHang) {
                    KhachHang kh = (KhachHang) value;
                    setText(kh.getTen() + " (ID: " + kh.getMaID() + ")");
                }
                return this;
            }
        });

        // 4. Tạo một panel tùy chỉnh cho JOptionPane
        JPanel dialogPanel = new JPanel(new BorderLayout(5, 5));
        dialogPanel.add(new JLabel("Chọn khách hàng để đặt phòng " + phong.getMaPhong() + ":"), BorderLayout.NORTH);
        dialogPanel.add(khachHangComboBox, BorderLayout.CENTER);

        // 5. Hiển thị dialog
        int result = JOptionPane.showConfirmDialog(this,
                dialogPanel,
                "Chọn Khách Hàng",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        // 6. Xử lý kết quả khi người dùng nhấn OK
        if (result == JOptionPane.OK_OPTION) {
            KhachHang khachChon = (KhachHang) khachHangComboBox.getSelectedItem();

            if (khachChon == null) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 7. Cập nhật đối tượng Phong
            phong.setTrangThai(true);
            phong.setKhachThue(khachChon);

            // 8. Lưu vào CSDL
            if (phongRepo.update(phong)) {
                JOptionPane.showMessageDialog(this, "Đặt phòng " + phong.getMaPhong() + " cho khách " + khachChon.getTen() + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadPhongCards(); // Tải lại toàn bộ panel
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                phong.setTrangThai(false);
                phong.setKhachThue(null);
            }
        }
    }

    /**
     * Xử lý logic trả phòng cho một phòng cụ thể
     */
    private void traPhong(Phong phong) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn trả phòng " + phong.getMaPhong() + " không?",
                "Xác nhận trả phòng", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Ghi nhận thanh toán
            String ttMessage = ThanhToan.ghiNhanThanhToan(phong);

            // Cập nhật đối tượng Phong
            phong.setTrangThai(false);
            phong.setKhachThue(null);

            // Lưu vào CSDL
            if (phongRepo.update(phong)) {
                JOptionPane.showMessageDialog(this, "Trả phòng thành công!\n" + ttMessage, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadPhongCards();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}