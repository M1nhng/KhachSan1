//khai bao va cau hinh lop (class defination)
package view;

import javax.swing.*;// tao giao dien (Jframe, Jpanel ,Jbutton..)
import javax.swing.border.Border; // thu vien tao vien
import java.awt.*; //(layout , color , font..)
import java.util.function.Consumer;// Nhập Interface Consumer để xử lý sự kiện (dùng cho nút Thoát)

public class LoginView extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel container;
    private final JPanel loginPanel;// Panel chứa form đăng nhập

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkRemember;
    private JCheckBox chkShowPass;
    private JButton btnLogin;
    private JButton btnCancel;
// Các Interface để xử lý sự kiện (Callback)
    private LoginHandler loginHandler;// Xử lý khi ấn Đăng nhập
    private Consumer<LoginView> cancelHandler;// Xử lý khi ấn Thoát

    public LoginView() {
        setTitle("Đăng nhập hệ thống");
        setSize(760, 460);// Đặt kích thước: Rộng 760, Cao 460
        setLocationRelativeTo(null);// Đặt cửa sổ ra giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);// Không cho phép người dùng thay đổi kích thước cửa sổ

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);
        container.setBackground(new Color(246, 248, 250));//mau xam nhat

        loginPanel = buildLoginPanel();

        JPanel screen = new JPanel(new GridBagLayout());//dùng GridBagLayout để căn giữa nội dung
        screen.setBackground(new Color(246, 248, 250));

        RoundedPanel card = new RoundedPanel(18, Color.WHITE);//khung bo trang
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        card.add(loginPanel, BorderLayout.CENTER);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        screen.add(card, gc);

        container.add(screen, "login");
        setContentPane(container);
        
        // Sự kiện: Khi cửa sổ mở lên thì con trỏ chuột tự nhảy vào ô Username
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                txtUsername.requestFocus();
            }
        });
    }

    private JPanel buildLoginPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);// Làm trong suốt để thấy nền của RoundedPanel

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));// Sắp xếp theo chiều dọc
        JLabel title = new JLabel("Đăng nhập hệ thống");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        JLabel subtitle = new JLabel("Vui lòng nhập tài khoản để tiếp tục");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 14f));
        subtitle.setForeground(new Color(120, 125, 130));// Màu xám cho chữ phụ
        header.add(title);
        header.add(Box.createVerticalStrut(6));// Tạo khoảng cách dọc 6px
        header.add(subtitle);
        header.add(Box.createVerticalStrut(18));
        root.add(header, BorderLayout.NORTH);// Đặt header lên trên cùng
        
        // --- PHẦN FORM NHẬP LIỆU ---
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        // Ô nhập Username
        txtUsername = new JTextField();
        txtUsername.putClientProperty("JComponent.roundRect", Boolean.TRUE);
        txtUsername.setBorder(compoundFieldBorder("Tên đăng nhập"));
        form.add(txtUsername);
        form.add(Box.createVerticalStrut(10));

        // Ô nhập Password
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty("JComponent.roundRect", Boolean.TRUE);
        txtPassword.setBorder(compoundFieldBorder("Mật khẩu"));
        form.add(txtPassword);
        form.add(Box.createVerticalStrut(10));

        // --- CÁC TÙY CHỌN (Ghi nhớ & Hiện mật khẩu) ---
        JPanel opts = new JPanel(new BorderLayout());
        opts.setOpaque(false);

        chkRemember = new JCheckBox("Ghi nhớ");
        chkRemember.setOpaque(false);

        chkShowPass = new JCheckBox("Hiện mật khẩu");
        chkShowPass.setOpaque(false);
        chkShowPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        chkShowPass.addActionListener(e -> {
            if (chkShowPass.isSelected()) {
                txtPassword.setEchoChar((char) 0);// Hiện chữ bình thường
            } else {
                txtPassword.setEchoChar('•');// Hiện dấu chấm tròn
            }
        });

        opts.add(chkRemember, BorderLayout.WEST);
        opts.add(chkShowPass, BorderLayout.EAST);

        form.add(opts);
        form.add(Box.createVerticalStrut(14));

        btnLogin = primaryButton("Đăng nhập");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> {
            if (loginHandler != null) {
                // Gọi hàm xử lý logic (được truyền từ bên ngoài vào)
                loginHandler.onLogin(getUsername(), getPassword(), chkRemember.isSelected());
            }
        });
        form.add(btnLogin);

        root.add(form, BorderLayout.CENTER);
        // --- PHẦN CHÂN (FOOTER - Nút Thoát) ---
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setOpaque(false);
        btnCancel = outlinedButton("Thoát");
        btnCancel.addActionListener(e -> {
            if (cancelHandler != null)
                cancelHandler.accept(this);
            else
                System.exit(0);
        });
        south.add(btnCancel);
        root.add(south, BorderLayout.SOUTH);
        // Phím tắt: Enter -> Đăng nhập, Escape -> Thoát
        root.registerKeyboardAction(e -> btnLogin.doClick(), KeyStroke.getKeyStroke("ENTER"),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        root.registerKeyboardAction(e -> btnCancel.doClick(), KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        return root;
    }
    // Hàm để set logic xử lý đăng nhập từ bên ngoài (MainForm)
    public void setLoginHandler(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public char[] getPassword() {
        return txtPassword.getPassword();
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    // Hàm tạo viền phức hợp cho ô nhập liệu (Viền xám + Padding + Tiêu đề nhỏ)
    private static Border compoundFieldBorder(String title) {
        Color line = new Color(220, 223, 228);
        Border lineBorder = BorderFactory.createLineBorder(line, 1, true);
        Border empty = BorderFactory.createEmptyBorder(6, 8, 6, 8);
        Border titled = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title);
        return BorderFactory.createCompoundBorder(lineBorder, BorderFactory.createCompoundBorder(titled, empty));
    }

    private static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(76, 175, 80));//xanh la
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setFocusPainted(false);//bo vien
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private static JButton outlinedButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(76, 175, 80)),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        button.setForeground(new Color(76, 175, 80));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    @FunctionalInterface
    public interface LoginHandler {
        void onLogin(String username, char[] password, boolean remember);
    }

    private static class RoundedPanel extends JPanel {
        private final int arc;
        private final Color bg;

        private RoundedPanel(int arc, Color bg) {
            this.arc = arc;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, arc + 6, arc + 6);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public Insets getInsets() {
            return new Insets(12, 12, 12, 12);
        }
    }
}
