package repository;

import model.ThanhToan;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoanhThuRepository {
    public boolean addDoanhThu(String maPhong, String tenKhach, double soTien, String chiTietDV) {
        String sql = "INSERT INTO doanhthu (MaPhong, TenKhach, SoTien, NgayThanhToan, ChiTietDichVu) VALUES (?, ?, ?, NOW(), ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPhong);
            ps.setString(2, tenKhach);
            ps.setDouble(3, soTien);
            ps.setString(4, chiTietDV);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy thống kê doanh thu theo tuần của một năm cụ thể
    // Map<Số Tuần, Tổng Tiền>
    public Map<Integer, Double> getDoanhThuTheoTuan(int year) {
        Map<Integer, Double> stats = new HashMap<>();
        // WEEK(NgayThanhToan, 1): Mode 1 bắt đầu tuần từ thứ 2
        String sql = "SELECT WEEK(NgayThanhToan, 1) as Tuan, SUM(SoTien) as TongTien " +
                "FROM doanhthu " +
                "WHERE YEAR(NgayThanhToan) = ? " +
                "GROUP BY Tuan " +
                "ORDER BY Tuan ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                stats.put(rs.getInt("Tuan"), rs.getDouble("TongTien"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}