package repository;

import model.NhanVien; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhanVienRepository implements INhanVienRepository {

    private NhanVien mapResultSetToNhanVien(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setMaID(rs.getString("MaNV"));
        nv.setTen(rs.getString("Ten"));
        nv.setSoCMND(rs.getString("SoCMND"));
        nv.setSoDienThoai(rs.getString("SoDienThoai"));

        nv.setChucVu(rs.getString("ChucVu"));
        nv.setLuongCoBan(rs.getDouble("LuongCoBan"));

        return nv;
    }

    @Override
    public List<NhanVien> getAll() {
        List<NhanVien> dsNhanVien = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsNhanVien.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNhanVien;
    }

    @Override
    public NhanVien getByName(String ten) {
        // String sql = "SELECT * FROM nhanvien WHERE Ten LIKE ?";

        // try (Connection conn = DatabaseConnection.getConnection();
        //         PreparedStatement ps = conn.prepareStatement(sql)) {

        //     ps.setString(1, "%" + ten + "%");
        //     try (ResultSet rs = ps.executeQuery()) {
        //         if (rs.next()) {
        //             return mapResultSetToNhanVien(rs);
        //         }
        //     }
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        return null;
    }

    @Override
    public boolean add(NhanVien nv) {
        String sql = "INSERT INTO nhanvien (MaNV, Ten, SoCMND, SoDienThoai, ChucVu, LuongCoBan) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMaID());
            ps.setString(2, nv.getTen());
            ps.setString(3, nv.getSoCMND());
            ps.setString(4, nv.getSoDienThoai());

            ps.setString(5, nv.getChucVu());
            ps.setDouble(6, nv.getLuongCoBan());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm nhân viên: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(NhanVien nv) {
        String sql = "UPDATE nhanvien SET Ten = ?, SoCMND = ?, SoDienThoai = ?, ChucVu = ?, LuongCoBan = ? WHERE MaNV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTen());
            ps.setString(2, nv.getSoCMND());
            ps.setString(3, nv.getSoDienThoai());

            ps.setString(4, nv.getChucVu());
            ps.setDouble(5, nv.getLuongCoBan());

            ps.setString(6, nv.getMaID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM nhanvien WHERE MaNV = ?"; 

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}