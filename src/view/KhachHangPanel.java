package view; // Phải cùng package với MainForm

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Class.KhachHang; // Import model KhachHang
import repository.IKhachHangRepository; // Import repository

public class KhachHangPanel extends JPanel {

    private IKhachHangRepository khachHangRepo;

    // Components
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaKH, txtTen, txtCMND, txtSDT, txtEmail;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    // MỚI: Components cho tìm kiếm
    private JTextField txtTimKiem;
    private JButton btnTimKiem;

    // Màu sắc
    private static final Color COLOR_PRIMARY = MainForm.COLOR_PRIMARY;
    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;
    private static final Color COLOR_HEADER = MainForm.COLOR_HEADER;
    private static final Color COLOR_TEXT = MainForm.COLOR_TEXT;

    public KhachHangPanel(IKhachHangRepository repo) {
        this.khachHangRepo = repo;
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        initActionListeners();
        loadCustomerData(); // Tải toàn bộ khi khởi động
    }

    private void initComponents() {
        // --- Panel Form (NORTH) ---
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(COLOR_BACKGROUND);

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        fieldsPanel.setBackground(COLOR_BACKGROUND);

        // ... (Code tạo txtMaKH, txtTen... giữ nguyên) ...
        txtMaKH = new JTextField();
        txtTen = new JTextField();
        txtCMND = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();
        fieldsPanel.add(CustomStyler.createStyledLabel("Tên Khách Hàng:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtTen));
        fieldsPanel.add(CustomStyler.createStyledLabel("Số CMND:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtCMND));
        fieldsPanel.add(CustomStyler.createStyledLabel("Số Điện Thoại:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtSDT));
        fieldsPanel.add(CustomStyler.createStyledLabel("Email:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtEmail));
        txtMaKH.setEditable(false);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(COLOR_BACKGROUND);

        btnThem = CustomStyler.createStyledButton("Thêm Mới");
        btnSua = CustomStyler.createStyledButton("Cập Nhật");
        btnXoa = CustomStyler.createStyledButton("Xóa");
        btnLamMoi = CustomStyler.createStyledButton("Làm Mới Form");

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Panel Bảng và Tìm kiếm (CENTER) ---
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(COLOR_BACKGROUND);

        // MỚI: Panel Tìm kiếm (Đặt ở phía Bắc của tablePanel)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(COLOR_BACKGROUND);

        txtTimKiem = new JTextField(30); // Ô nhập text tìm kiếm
        btnTimKiem = CustomStyler.createStyledButton("Tìm theo Tên");

        searchPanel.add(CustomStyler.createStyledLabel("Tìm kiếm:"));
        searchPanel.add(CustomStyler.createStyledTextField(txtTimKiem));
        searchPanel.add(btnTimKiem);

        // --- Panel Bảng (Đặt ở giữa tablePanel) ---
        String[] columnNames = { "Tên", "CMND", "SĐT", "Email" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        CustomStyler.styleTable(table); //

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Thêm searchPanel và scrollPane vào tablePanel
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Thêm các components vào panel chính
        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER); // Sửa: Thêm tablePanel vào CENTER
    }

    private void initActionListeners() {
        // Nút Thêm
        btnThem.addActionListener(e -> themKhachHang());

        // Nút Sửa
        btnSua.addActionListener(e -> suaKhachHang());

        // Nút Xóa
        btnXoa.addActionListener(e -> xoaKhachHang());

        // Nút Làm Mới
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadCustomerData(); // SỬA: Tải lại toàn bộ danh sách
        });

        // MỚI: Nút Tìm Kiếm
        btnTimKiem.addActionListener(e -> timKhachHang());

        // Sự kiện click vào 1 dòng trên bảng (Giữ nguyên)
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                txtMaKH.setText(model.getValueAt(selectedRow, 0).toString());
                txtTen.setText(model.getValueAt(selectedRow, 1).toString());
                txtCMND.setText(model.getValueAt(selectedRow, 2).toString());
                txtSDT.setText(model.getValueAt(selectedRow, 3).toString());
                txtEmail.setText(model.getValueAt(selectedRow, 4).toString());
                txtMaKH.setEditable(false);
            }
        });
    }

    /**
     * Tải TOÀN BỘ dữ liệu từ Repository lên JTable
     */
    private void loadCustomerData() {
        model.setRowCount(0); // Xóa hết dữ liệu cũ
        List<KhachHang> customers = khachHangRepo.getAll();
        for (KhachHang kh : customers) {
            model.addRow(new Object[] {
                    kh.getTen(),
                    kh.getSoCMND(),
                    kh.getSoDienThoai(),
                    kh.getEmail()
            });
        }
    }

    /**
     * MỚI: Chức năng Tìm Kiếm Khách Hàng (load kết quả lên bảng)
     */
    private void timKhachHang() {
        String ten = txtTimKiem.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên để tìm kiếm.", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Gọi hàm findByName mới
        List<KhachHang> customers = khachHangRepo.findByName(ten);

        model.setRowCount(0); // Xóa bảng

        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào có tên chứa: " + ten, "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Nạp kết quả tìm kiếm vào bảng
            for (KhachHang kh : customers) {
                model.addRow(new Object[] {
                        kh.getMaID(),
                        kh.getTen(),
                        kh.getSoCMND(),
                        kh.getSoDienThoai(),
                        kh.getEmail()
                });
            }
        }
    }

    /**
     * Chức năng Thêm Khách Hàng (Giữ nguyên logic của bạn)
     */
    private void themKhachHang() {
        String ten = txtTen.getText();
        String cmnd = txtCMND.getText();
        String sdt = txtSDT.getText();
        String email = txtEmail.getText();

        if (ten.isEmpty() || cmnd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên và CMND không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cmnd.length() != 12) {
            JOptionPane.showMessageDialog(this, "So CMND phai du 12 so!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (sdt.length() != 10) {
            JOptionPane.showMessageDialog(this, "So dien thoai phai du 10 so!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        KhachHang kh = new KhachHang(ten, cmnd, sdt, email);

        if (khachHangRepo.add(kh)) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công! ID mới: " + kh.getMaID());
            loadCustomerData(); // Tải lại bảng
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Chức năng Sửa Khách Hàng (Giữ nguyên)
     */
    private void suaKhachHang() {
        String maKH = txtMaKH.getText();
        if (maKH.isEmpty() || maKH.equals("Tự động hoặc chọn từ bảng")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng từ bảng để sửa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        KhachHang kh = new KhachHang();
        kh.setMaID(maKH);
        kh.setTen(txtTen.getText());
        kh.setSoCMND(txtCMND.getText());
        kh.setSoDienThoai(txtSDT.getText());
        kh.setEmail(txtEmail.getText());

        if (khachHangRepo.update(kh)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadCustomerData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Chức năng Xóa Khách Hàng (Giữ nguyên)
     */
    private void xoaKhachHang() {
        String maKH = txtMaKH.getText();
        if (maKH.isEmpty() || maKH.equals("Tự động hoặc chọn từ bảng")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng từ bảng để xóa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa khách hàng " + maKH + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (khachHangRepo.delete(maKH)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadCustomerData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Dọn dẹp form nhập liệu
     */
    private void clearForm() {
        txtMaKH.setText("Tự động hoặc chọn từ bảng");
        txtMaKH.setEditable(false);
        txtTen.setText("");
        txtCMND.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtTimKiem.setText(""); // MỚI: Cũng xóa ô tìm kiếm
        table.clearSelection();
    }
}