package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Class.NhanVien;
import repository.INhanVienRepository;

public class NhanVienPanel extends JPanel {

    private INhanVienRepository nhanVienRepo;

    // Components
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaNV, txtTen, txtCMND, txtSDT, txtLuong, txtChucVu;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JTextField txtTimKiem;
    private DecimalFormat vndFormat;

    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;

    public NhanVienPanel(INhanVienRepository repo) {
        this.nhanVienRepo = repo;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("###,###", symbols);
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        initActionListeners();
        loadData();
    }

    private void initComponents() {
        // --- Panel Form (NORTH) ---
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(COLOR_BACKGROUND);

        // Sử dụng GridLayout 6 hàng, 2 cột
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        fieldsPanel.setBackground(COLOR_BACKGROUND);

        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
        txtTen = new JTextField();
        txtCMND = new JTextField();
        txtSDT = new JTextField();
        txtLuong = new JTextField();
        txtChucVu = new JTextField();

        fieldsPanel.add(CustomStyler.createStyledLabel("Tên Nhân Viên:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtTen));

        fieldsPanel.add(CustomStyler.createStyledLabel("Số CMND:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtCMND));

        fieldsPanel.add(CustomStyler.createStyledLabel("Số Điện Thoại:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtSDT));

        fieldsPanel.add(CustomStyler.createStyledLabel("Lương Cơ Bản:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtLuong));

        fieldsPanel.add(CustomStyler.createStyledLabel("Chức Vụ:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtChucVu));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(COLOR_BACKGROUND);

        btnThem = CustomStyler.createStyledButton("Thêm Mới");
        btnSua = CustomStyler.createStyledButton("Cập Nhật");
        btnXoa = CustomStyler.createStyledButton("Xóa");
        btnLamMoi = CustomStyler.createStyledButton("Làm Mới");

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Panel Bảng và Tìm kiếm (CENTER) ---
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(COLOR_BACKGROUND);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(COLOR_BACKGROUND);
        txtTimKiem = new JTextField(20);
        btnTimKiem = CustomStyler.createStyledButton("Tìm kiếm");
        searchPanel.add(CustomStyler.createStyledLabel("Tìm tên NV:"));
        searchPanel.add(CustomStyler.createStyledTextField(txtTimKiem));
        searchPanel.add(btnTimKiem);

        String[] columnNames = { "Mã NV", "Tên", "CMND", "SĐT", "Lương", "Chức Vụ" };
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

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void initActionListeners() {
        btnThem.addActionListener(e -> themNhanVien());
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadData();
        });
        btnTimKiem.addActionListener(e -> timNhanVien());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtMaNV.setText(model.getValueAt(row, 0).toString());
                txtTen.setText(model.getValueAt(row, 1).toString());
                txtCMND.setText(model.getValueAt(row, 2).toString());
                txtSDT.setText(model.getValueAt(row, 3).toString());

                // Lấy giá trị lương từ bảng (đã có dấu chấm) đưa vào ô nhập
                txtLuong.setText(model.getValueAt(row, 4).toString());

                txtChucVu.setText(model.getValueAt(row, 5).toString());
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        List<NhanVien> list = nhanVienRepo.getAll();
        for (NhanVien nv : list) {
            model.addRow(new Object[] {
                    nv.getMaID(),
                    nv.getTen(),
                    nv.getSoCMND(),
                    nv.getSoDienThoai(),
                    vndFormat.format(nv.getLuongCoBan()), // <-- FORMAT TẠI ĐÂY
                    nv.getChucVu()
            });
        }
    }

    private void themNhanVien() {
        try {
            String ten = txtTen.getText();
            String cmnd = txtCMND.getText();
            String sdt = txtSDT.getText();
            String chucVu = txtChucVu.getText();

            // Xóa dấu chấm trước khi chuyển sang số (1.000.000 -> 1000000)
            String luongRaw = txtLuong.getText().replace(".", "").trim();
            double luong = Double.parseDouble(luongRaw);

            NhanVien nv = new NhanVien(ten, cmnd, sdt, luong, chucVu);

            if (nhanVienRepo.add(nv)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công! ID: " + nv.getMaID());
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lương phải là số và không chứa ký tự lạ!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaNhanVien() {
        if (txtMaNV.getText().equals("Tự động"))
            return;
        try {
            // Xóa dấu chấm trước khi chuyển sang số
            String luongRaw = txtLuong.getText().replace(".", "").trim();

            NhanVien nv = new NhanVien(txtTen.getText(), txtCMND.getText(), txtSDT.getText(),
                    Double.parseDouble(luongRaw), txtChucVu.getText());
            nv.setMaID(txtMaNV.getText());

            if (nhanVienRepo.update(nv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập liệu! Vui lòng kiểm tra ô Lương.");
        }
    }

    private void xoaNhanVien() {
        String id = txtMaNV.getText();
        if (id.equals("Tự động"))
            return;
        if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + id + "?") == JOptionPane.YES_OPTION) {
            if (nhanVienRepo.delete(id)) {
                JOptionPane.showMessageDialog(this, "Đã xóa!");
                loadData();
                clearForm();
            }
        }
    }

    private void timNhanVien() {
        String ten = txtTimKiem.getText();
        List<NhanVien> list = nhanVienRepo.findByName(ten);
        model.setRowCount(0);
        for (NhanVien nv : list) {
            model.addRow(new Object[] {
                    nv.getMaID(), nv.getTen(), nv.getSoCMND(), nv.getSoDienThoai(), nv.getLuongCoBan(), nv.getChucVu()
            });
        }
    }

    private void clearForm() {
        txtMaNV.setText("Tự động");
        txtTen.setText("");
        txtCMND.setText("");
        txtSDT.setText("");
        txtLuong.setText("");
        txtChucVu.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
    }
}