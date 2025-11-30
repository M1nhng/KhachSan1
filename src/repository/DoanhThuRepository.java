package repository;

import model.ThanhToan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    public List<ThanhToan> getAll() {
        List<ThanhToan> list = new ArrayList<>();
        String sql = "SELECT * FROM doanhthu ORDER BY NgayThanhToan DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ThanhToan tt = new ThanhToan(
                        rs.getString("MaPhong"),
                        rs.getString("TenKhach"),
                        rs.getDouble("SoTien"),
                        rs.getString("ChiTietDichVu"),
                        rs.getString("NgayThanhToan") 
                );
                list.add(tt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // public Map<Integer, Double> getDoanhThuTheoTuan(int year) {
    //     Map<Integer, Double> stats = new HashMap<>();
    //     String sql = "SELECT WEEK(NgayThanhToan, 1) as Tuan, SUM(SoTien) as TongTien " +
    //             "FROM doanhthu WHERE YEAR(NgayThanhToan) = ? GROUP BY Tuan ORDER BY Tuan ASC";

    //     try (Connection conn = DatabaseConnection.getConnection();
    //             PreparedStatement ps = conn.prepareStatement(sql)) {
    //         ps.setInt(1, year);
    //         ResultSet rs = ps.executeQuery();
    //         while (rs.next()) {
    //             stats.put(rs.getInt("Tuan"), rs.getDouble("TongTien"));
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return stats;
    // }
}