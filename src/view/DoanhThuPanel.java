package view;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener; // Import để xử lý sự kiện chuyển tab
import javax.swing.table.DefaultTableModel;
import repository.DoanhThuRepository;

public class DoanhThuPanel extends JPanel {

    private DoanhThuRepository doanhThuRepo;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTongDoanhThuNam;
    private DecimalFormat vndFormat;

    // Nút làm mới
    private JButton btnLamMoi;

    public DoanhThuPanel() {
        this.doanhThuRepo = new DoanhThuRepository();

        // Định dạng tiền tệ
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
        symbols.setGroupingSeparator('.');
        vndFormat = new DecimalFormat("###,### VND", symbols);

        setLayout(new BorderLayout(10, 10));
        setBackground(MainForm.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();

        // --- (MỚI) TỰ ĐỘNG LOAD LẠI KHI CHUYỂN TAB ---
        // Thêm sự kiện lắng nghe: Khi Panel này được hiển thị (chuyển tab tới đây),
        // nó sẽ tự động chạy hàm loadData()
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                // Khi tab được mở -> Tải lại dữ liệu
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
        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout()); // Dùng BorderLayout để đẩy nút sang phải
        headerPanel.setBackground(MainForm.COLOR_BACKGROUND);
        headerPanel.setOpaque(false);

        int currentYear = LocalDate.now().getYear();

        JLabel lblTitle = new JLabel("Doanh Thu Theo Tuần - Năm " + currentYear);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(MainForm.COLOR_PRIMARY);

        // --- (MỚI) NÚT LÀM MỚI ---
        btnLamMoi = CustomStyler.createStyledButton("Làm Mới");
        btnLamMoi.setPreferredSize(new Dimension(150, 40));
        btnLamMoi.addActionListener(e -> {
            loadData(); // Gọi hàm tải lại dữ liệu
            JOptionPane.showMessageDialog(this, "Dữ liệu đã được cập nhật!");
        });

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnLamMoi, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Body: Bảng số liệu ---
        String[] cols = { "Tuần Thứ", "Thời Gian", "Doanh Thu" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa bảng
            }
        };

        table = new JTable(model);
        CustomStyler.styleTable(table);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Footer: Tổng doanh thu năm ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(Color.WHITE);
        lblTongDoanhThuNam = new JLabel("Tổng Doanh Thu: 0 VND");
        lblTongDoanhThuNam.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongDoanhThuNam.setForeground(Color.RED);
        footerPanel.add(lblTongDoanhThuNam);

        add(footerPanel, BorderLayout.SOUTH);
    }

    // Hàm tải dữ liệu từ DB
    public void loadData() {
        model.setRowCount(0); // Xóa dữ liệu cũ trên bảng

        int currentYear = LocalDate.now().getYear();

        // Lấy dữ liệu mới nhất từ Database
        Map<Integer, Double> data = doanhThuRepo.getDoanhThuTheoTuan(currentYear);
        double tongNam = 0;

        // Duyệt tuần tự từ tuần 1 đến tuần 53
        for (int tuan = 1; tuan <= 53; tuan++) {
            if (data.containsKey(tuan)) {
                double doanhThu = data.get(tuan);
                tongNam += doanhThu;

                String timeRange = getDateRangeOfWeek(currentYear, tuan);

                model.addRow(new Object[] {
                        "Tuần " + tuan,
                        timeRange,
                        vndFormat.format(doanhThu)
                });
            }
        }

        // Cập nhật tổng tiền cuối trang
        lblTongDoanhThuNam.setText("Tổng Doanh Thu Năm " + currentYear + ": " + vndFormat.format(tongNam));
    }

    // Hàm hỗ trợ tính ngày
    private String getDateRangeOfWeek(int year, int week) {
        try {
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            LocalDate firstDay = LocalDate.of(year, 1, 1)
                    .with(weekFields.weekOfYear(), week)
                    .with(weekFields.dayOfWeek(), 1);

            LocalDate lastDay = firstDay.plusDays(6);

            return firstDay.getDayOfMonth() + "/" + firstDay.getMonthValue() +
                    " - " + lastDay.getDayOfMonth() + "/" + lastDay.getMonthValue();
        } catch (Exception e) {
            return "---";
        }
    }
}