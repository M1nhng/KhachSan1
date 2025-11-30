
package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import model.DichVu;
import model.Phong;
import model.ThanhToan;
import repository.IDichVuRepository;
import repository.IPhongRepository;

public class ThanhToanPanel extends JPanel {

    private IPhongRepository phongRepo;
    private IDichVuRepository dichVuRepo;

    private JComboBox<Phong> cboPhong;
    private JTable invoiceTable;
    private DefaultTableModel invoiceModel;
    private JLabel lblTongTien, lblKhachHang;
    private JButton btnThanhToan;

    private List<DichVu> dichVuCuaPhong = new ArrayList<>();

    private DecimalFormat vndFormat;
    private static final Color COLOR_PRIMARY = MainForm.COLOR_PRIMARY;
    private static final Color COLOR_BACKGROUND = MainForm.COLOR_BACKGROUND;

    public ThanhToanPanel(IPhongRepository phongRepo, IDichVuRepository dichVuRepo) {
        this.phongRepo = phongRepo;
        this.dichVuRepo = dichVuRepo;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("###,### VND", symbols);

        setLayout(new BorderLayout(15, 15));
        setBackground(COLOR_BACKGROUND);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        initComponents();
        loadOccupiedRooms();
    }

    private void initComponents() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(350, 0));

        JPanel selectionPanel = new JPanel(new GridLayout(4, 1, 5, 10));
        selectionPanel.setOpaque(false);
        selectionPanel
                .setBorder(BorderFactory.createTitledBorder(null, "Thông Tin Phòng", TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));

        cboPhong = new JComboBox<>();
        cboPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboPhong.addActionListener(e -> updateInvoiceInfo());

        lblKhachHang = CustomStyler.createStyledLabel("Khách hàng: ---");

        JButton btnLamMoi = CustomStyler.createStyledButton("Làm Mới Danh Sách");
        btnLamMoi.setBackground(new Color(255, 152, 0));
        btnLamMoi.addActionListener(e -> loadOccupiedRooms());

        selectionPanel.add(CustomStyler.createStyledLabel("Chọn Phòng Cần Thanh Toán:"));
        selectionPanel.add(cboPhong);
        selectionPanel.add(lblKhachHang);
        selectionPanel.add(btnLamMoi);

        leftPanel.add(selectionPanel, BorderLayout.NORTH);


        JPanel rightPanel = new JPanel(new BorderLayout(0, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel lblInvoiceHeader = new JLabel("CHI TIẾT HÓA ĐƠN", SwingConstants.CENTER);
        lblInvoiceHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblInvoiceHeader.setForeground(COLOR_PRIMARY);
        lblInvoiceHeader.setBorder(new EmptyBorder(20, 0, 20, 0));
        rightPanel.add(lblInvoiceHeader, BorderLayout.NORTH);

        String[] cols = { "Nội Dung", "Đơn Giá", "Thành Tiền" };
        invoiceModel = new DefaultTableModel(cols, 0);
        invoiceTable = new JTable(invoiceModel);
        CustomStyler.styleTable(invoiceTable);

        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout(10, 10));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        lblTongTien = new JLabel("Tổng cộng: 0 VND", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTongTien.setForeground(new Color(231, 76, 60));

        btnThanhToan = CustomStyler.createStyledButton("THANH TOÁN & TRẢ PHÒNG");
        btnThanhToan.setPreferredSize(new Dimension(250, 50));
        btnThanhToan.addActionListener(e -> xuLyThanhToan());

        footerPanel.add(lblTongTien, BorderLayout.NORTH);
        footerPanel.add(btnThanhToan, BorderLayout.SOUTH);

        rightPanel.add(footerPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void loadOccupiedRooms() {
        cboPhong.removeAllItems();
        List<Phong> list = phongRepo.getAll();
        boolean hasRoom = false;
        for (Phong p : list) {
            if (p.isTrangThai()) {
                cboPhong.addItem(p);
                hasRoom = true;
            }
        }

        cboPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Phong) {
                    Phong p = (Phong) value;
                    setText("Phòng " + p.getMaPhong() + " - " + p.getLoaiPhong());
                }
                return this;
            }
        });

        if (!hasRoom) {
            lblKhachHang.setText("Không có phòng nào đang thuê.");
            invoiceModel.setRowCount(0);
            lblTongTien.setText("Tổng cộng: 0 VND");
        }
    }

    private void updateInvoiceInfo() {
        Phong p = (Phong) cboPhong.getSelectedItem();
        if (p != null) {
            String tenKhach = (p.getKhachThue() != null) ? p.getKhachThue().getTen() : "Không rõ";
            lblKhachHang.setText("Khách hàng: " + tenKhach);

            invoiceModel.setRowCount(0);
            dichVuCuaPhong.clear(); 

            double tongTien = 0;

            double giaPhong = p.getGiaPhong();
            invoiceModel.addRow(new Object[] {
                    "Tiền thuê phòng (" + p.getLoaiPhong() + ")",
                    vndFormat.format(giaPhong),
                    vndFormat.format(giaPhong)
            });
            tongTien += giaPhong;

            List<DichVu> allServices = dichVuRepo.getAll();
            for (DichVu dv : allServices) {
                if (dv.getMaPhong() != null && dv.getMaPhong().equals(p.getMaPhong())) {
                    invoiceModel.addRow(new Object[] {
                            "Dịch vụ: " + dv.getTenDV(),
                            vndFormat.format(dv.getGiaDV()),
                            vndFormat.format(dv.getGiaDV())
                    });
                    tongTien += dv.getGiaDV();
                    dichVuCuaPhong.add(dv); 
                }
            }

            lblTongTien.setText("Tổng cộng: " + vndFormat.format(tongTien));
        }
    }

    private void xuLyThanhToan() {

        Phong p = (Phong) cboPhong.getSelectedItem();
        if (p == null)
            return;

        String tongTienText = lblTongTien.getText().replace("Tổng cộng: ", "").replace(" VND", "").replace(".", "")
                .trim();
        double tongTien = 0;
        try {
            tongTien = Double.parseDouble(tongTienText);
        } catch (NumberFormatException e) {
            tongTien = p.getGiaPhong(); 
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận thanh toán và trả phòng " + p.getMaPhong() + "?\n" +
                        "Tổng thực thu: " + vndFormat.format(tongTien),
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            StringBuilder sbDichVu = new StringBuilder();
            if (dichVuCuaPhong.isEmpty()) {
                sbDichVu.append("Không sử dụng dịch vụ");
            } else {
                for (DichVu dv : dichVuCuaPhong) {
                    sbDichVu.append(dv.getTenDV()).append(", ");
                }
                if (sbDichVu.length() > 2) {
                    sbDichVu.setLength(sbDichVu.length() - 2);
                }
            }

            String msg = ThanhToan.ghiNhanThanhToan(p, tongTien, sbDichVu.toString());
            String tenKhach = (p.getKhachThue() != null) ? p.getKhachThue().getTen() : "Khách vãng lai";

            boolean isSaved = billRepo.addDoanhThu(
                    p.getMaPhong(),
                    tenKhach,
                    tongTien,
                    sbDichVu.toString());

            if (isSaved) {
                System.out.println("Đã lưu hóa đơn vào CSDL");
            } else {
                System.err.println("Lỗi lưu hóa đơn!");
            }
            
            p.setTrangThai(false);
            p.setKhachThue(null);
            if (!phongRepo.update(p)) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật Phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (DichVu dv : dichVuCuaPhong) {
                dichVuRepo.delete(String.valueOf(dv.getMaDV()));
            }

            JOptionPane.showMessageDialog(this, msg);

            loadOccupiedRooms();
            invoiceModel.setRowCount(0);
            lblTongTien.setText("Tổng cộng: 0 VND");
        }
    }

    private repository.DoanhThuRepository billRepo = new repository.DoanhThuRepository();
}