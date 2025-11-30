package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;

import model.ThanhToan;
import repository.DoanhThuRepository;

public class DoanhThuPanel extends JPanel {

    private DoanhThuRepository doanhThuRepo;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTongDoanhThuToanBo;
    private JButton btnLamMoi;
    private DecimalFormat vndFormat;

    public DoanhThuPanel() {
        this.doanhThuRepo = new DoanhThuRepository();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("###,### VND", symbols);

        setLayout(new BorderLayout(10, 10));
        setBackground(MainForm.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();

        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                loadData();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MainForm.COLOR_BACKGROUND);
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Lịch Sử Giao Dịch Chi Tiết");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(MainForm.COLOR_PRIMARY);

        btnLamMoi = CustomStyler.createStyledButton("Làm Mới");
        btnLamMoi.setPreferredSize(new Dimension(120, 40));
        btnLamMoi.setBackground(new Color(33, 150, 243)); 
        btnLamMoi.addActionListener(e -> {
            loadData();
            JOptionPane.showMessageDialog(this, "Dữ liệu đã được cập nhật!");
        });

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnLamMoi, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        String[] cols = { "Mã Phòng", "Khách Hàng", "Chi Tiết Dịch Vụ", "Thời Gian TT", "Tổng Tiền" };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        table = new JTable(model);
        CustomStyler.styleTable(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(80); 
        table.getColumnModel().getColumn(1).setPreferredWidth(150); 
        table.getColumnModel().getColumn(2).setPreferredWidth(300); 
        table.getColumnModel().getColumn(3).setPreferredWidth(150); 
        table.getColumnModel().getColumn(4).setPreferredWidth(120); 

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        lblTongDoanhThuToanBo = new JLabel("Tổng Doanh Thu: 0 VND");
        lblTongDoanhThuToanBo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongDoanhThuToanBo.setForeground(new Color(231, 76, 60)); 

        footerPanel.add(lblTongDoanhThuToanBo);
        add(footerPanel, BorderLayout.SOUTH);
    }

    public void loadData() {
        model.setRowCount(0); 

        List<ThanhToan> list = doanhThuRepo.getAll(); 
        double tongTienAll = 0;

        for (ThanhToan tt : list) {
            model.addRow(new Object[] {
                    tt.getMaPhong(),
                    tt.getTenKhach(),
                    tt.getChiTietDichVu(), 
                    tt.getThoiGian(), 
                    vndFormat.format(tt.getSoTien())
            });
            tongTienAll += tt.getSoTien();
        }

        lblTongDoanhThuToanBo.setText("Tổng Doanh Thu Thực Tế: " + vndFormat.format(tongTienAll));
    }
}