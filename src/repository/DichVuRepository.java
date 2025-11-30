package repository;

import model.DichVu;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DichVuRepository implements IDichVuRepository {

    @Override
    public List<DichVu> getAll() {
        List<DichVu> list = new ArrayList<>();
        String sql = "SELECT * FROM dichvu";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DichVu dv = new DichVu();
                dv.setMaDV(rs.getInt("MaDV"));
                dv.setTenDV(rs.getString("TenDV"));
                dv.setGiaDV(rs.getDouble("GiaDV"));
                dv.setMaPhong(rs.getString("MaPhong"));
                list.add(dv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean add(DichVu dv) {
        String sql = "INSERT INTO dichvu (TenDV, GiaDV, MaPhong) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dv.getTenDV());
            ps.setDouble(2, dv.getGiaDV());
            ps.setString(3, dv.getMaPhong());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(DichVu dv) {
        String sql = "UPDATE dichvu SET TenDV=?, GiaDV=?, MaPhong=? WHERE MaDV=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dv.getTenDV());
            ps.setDouble(2, dv.getGiaDV());
            ps.setString(3, dv.getMaPhong());
            ps.setInt(4, dv.getMaDV());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM dichvu WHERE MaDV=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public DichVu getById(String id) {
        return null;
    }
}