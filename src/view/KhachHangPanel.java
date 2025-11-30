package view; 

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.KhachHang;
import repository.IKhachHangRepository;

public class KhachHangPanel extends JPanel {

    private IKhachHangRepository khachHangRepo;

    // Components
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaKH, txtTen, txtCMND, txtSDT, txtEmail;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

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
        loadCustomerData();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(COLOR_BACKGROUND);

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        fieldsPanel.setBackground(COLOR_BACKGROUND);

        txtMaKH = new JTextField();
        txtTen = new JTextField();
        txtCMND = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();

        fieldsPanel.add(CustomStyler.createStyledLabel("Mã KH:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtMaKH));
        fieldsPanel.add(CustomStyler.createStyledLabel("Tên Khách Hàng:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtTen));
        fieldsPanel.add(CustomStyler.createStyledLabel("Số CMND:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtCMND));
        fieldsPanel.add(CustomStyler.createStyledLabel("Số Điện Thoại:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtSDT));
        fieldsPanel.add(CustomStyler.createStyledLabel("Email:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtEmail));

        txtMaKH.setEditable(false);
        txtMaKH.setText("Tự động hoặc chọn từ bảng");

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

        String[] columnNames = { "Mã KH", "Tên", "CMND", "SĐT", "Email" };
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        CustomStyler.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initActionListeners() {
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> clearForm());

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
     * Tải dữ liệu từ Repository lên JTable
     */
    private void loadCustomerData() {
        model.setRowCount(0);
        List<KhachHang> customers = khachHangRepo.getAll();
        for (KhachHang kh : customers) {
            model.addRow(new Object[] {
                    kh.getMaID(), kh.getTen(), kh.getSoCMND(), kh.getSoDienThoai(), kh.getEmail()
            });
        }
        txtMaKH.setText(IdGenerator.generateNextId("khachhang", "KH", "MaKH"));
    }

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

        String newID = IdGenerator.generateNextId("khachhang", "KH", "MaKH");
        KhachHang kh = new KhachHang(newID, ten, cmnd, sdt, email);

        if (khachHangRepo.add(kh)) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công! ID: " + newID);
            loadCustomerData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại! Có thể lỗi kết nối CSDL.", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaKhachHang() {
        String maKH = txtMaKH.getText();
        if (maKH.isEmpty() || maKH.equals("Tự động hoặc chọn từ bảng")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng từ bảng để sửa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Dùng constructor rỗng và setter
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

    private void clearForm() {
        txtMaKH.setText("Tự động hoặc chọn từ bảng");
        txtMaKH.setEditable(false);
        txtTen.setText("");
        txtCMND.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        table.clearSelection();
    }
}