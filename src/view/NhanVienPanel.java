package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import model.NhanVien;
import repository.INhanVienRepository;
import exception.InvalidInputException;

public class NhanVienPanel extends JPanel {

    private INhanVienRepository nhanVienRepo;

    // Components
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaNV, txtTen, txtCMND, txtSDT, txtChucVu, txtLuong;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    private DecimalFormat vndFormat;

    private static final Color COLOR_PRIMARY = MainForm.COLOR_PRIMARY;
    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;
    private static final Color COLOR_HEADER = MainForm.COLOR_HEADER;
    private static final Color COLOR_TEXT = MainForm.COLOR_TEXT;

    public NhanVienPanel(INhanVienRepository repo) {
        this.nhanVienRepo = repo;
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("###,### VND", symbols);

        initComponents();
        initActionListeners();
        loadNhanVienData();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(COLOR_BACKGROUND);

        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        fieldsPanel.setBackground(COLOR_BACKGROUND);

        txtMaNV = new JTextField();
        txtTen = new JTextField();
        txtCMND = new JTextField();
        txtSDT = new JTextField();
        txtChucVu = new JTextField();
        txtLuong = new JTextField();

        fieldsPanel.add(CustomStyler.createStyledLabel("Mã NV:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtMaNV));
        fieldsPanel.add(CustomStyler.createStyledLabel("Tên Nhân Viên:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtTen));
        fieldsPanel.add(CustomStyler.createStyledLabel("Số CMND:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtCMND));
        fieldsPanel.add(CustomStyler.createStyledLabel("Số Điện Thoại:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtSDT));
        fieldsPanel.add(CustomStyler.createStyledLabel("Chức Vụ:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtChucVu));
        fieldsPanel.add(CustomStyler.createStyledLabel("Lương:"));
        fieldsPanel.add(CustomStyler.createStyledTextField(txtLuong));

        txtMaNV.setEditable(false);
        txtMaNV.setText("Tự động hoặc chọn từ bảng");

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

        String[] columnNames = { "Mã NV", "Tên", "CMND", "SĐT", "Lương", "Chức vụ" };
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
        btnThem.addActionListener(e -> themNhanVien());
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        btnLamMoi.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                txtMaNV.setText(model.getValueAt(selectedRow, 0).toString());
                txtTen.setText(model.getValueAt(selectedRow, 1).toString());
                txtCMND.setText(model.getValueAt(selectedRow, 2).toString());
                txtSDT.setText(model.getValueAt(selectedRow, 3).toString());
                txtChucVu.setText(model.getValueAt(selectedRow, 5).toString());

                String luongFormatted = model.getValueAt(selectedRow, 4).toString();
                String luongUnformatted = luongFormatted
                        .replace("VND", "")
                        .replace(".", "")
                        .trim();
                txtLuong.setText(luongUnformatted);

                txtMaNV.setEditable(false);
            }
        });
    }

    private void loadNhanVienData() {
        model.setRowCount(0);
        List<NhanVien> list = nhanVienRepo.getAll();
        for (NhanVien nv : list) {
            model.addRow(new Object[] {
                    nv.getMaID(), nv.getTen(), nv.getSoCMND(), nv.getSoDienThoai(),
                    vndFormat.format(nv.getLuongCoBan()), nv.getChucVu()
            });
        }
        txtMaNV.setText(IdGenerator.generateNextId("nhanvien", "NV", "MaNV"));
    }

    private void themNhanVien() {
        try {
            String ten = txtTen.getText();
            String cmnd = txtCMND.getText();
            String sdt = txtSDT.getText();
            String chucVu = txtChucVu.getText();
            double luong = Double.parseDouble(txtLuong.getText());

            if (ten.isEmpty() || cmnd.isEmpty() || chucVu.isEmpty()) {
                throw new InvalidInputException("Tên, CMND và chức vụ không được để trống!");
            }
            if (cmnd.length() != 12) {
                throw new InvalidInputException("So CMND phai du 12 so!");
            }
            if (sdt.length() != 10) {
                throw new InvalidInputException("So dien thoai phai du 10 so!");
            }
            if (luong < 0) {
                throw new InvalidInputException("Lương không được âm!");
            }

            String newID = IdGenerator.generateNextId("nhanvien", "NV", "MaNV");
            NhanVien nv = new NhanVien(newID, ten, cmnd, sdt, luong, chucVu);

            if (nhanVienRepo.add(nv)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công! ID: " + newID);
                loadNhanVienData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lương không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void suaNhanVien() {
        String maNV = txtMaNV.getText();
        if (maNV.isEmpty() || maNV.equals("Tự động hoặc chọn từ bảng")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên từ bảng để sửa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double luong = Double.parseDouble(txtLuong.getText());

            if (luong < 0) {
                throw new InvalidInputException("Lương không được âm!");
            }

            NhanVien nv = new NhanVien();
            nv.setMaID(maNV);
            nv.setTen(txtTen.getText());
            nv.setSoCMND(txtCMND.getText());
            nv.setSoDienThoai(txtSDT.getText());
            nv.setChucVu(txtChucVu.getText());
            nv.setLuongCoBan(luong);

            if (nhanVienRepo.update(nv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadNhanVienData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải là một con số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void xoaNhanVien() {
        String maNV = txtMaNV.getText();
        if (maNV.isEmpty() || maNV.equals("Tự động hoặc chọn từ bảng")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên từ bảng để xóa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa nhân viên " + maNV + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (nhanVienRepo.delete(maNV)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadNhanVienData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtMaNV.setText("Tự động hoặc chọn từ bảng");
        txtMaNV.setEditable(false);
        txtTen.setText("");
        txtCMND.setText("");
        txtSDT.setText("");
        txtChucVu.setText("");
        txtLuong.setText("");
        table.clearSelection();
    }
}