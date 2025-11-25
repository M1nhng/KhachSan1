package repository;

import Class.NhanVien;
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
        nv.setLuongCoBan(rs.getDouble("Luong"));
        nv.setChucVu(rs.getString("ChucVu"));
        return nv;
    }

    @Override
    public List<NhanVien> getAll() {
        List<NhanVien> dsNV = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                dsNV.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNV;
    }

    @Override
    public NhanVien getById(String id) {
        String sql = "SELECT * FROM nhanvien WHERE MaNV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapResultSetToNhanVien(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<NhanVien> findByName(String ten) {
        List<NhanVien> dsNV = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien WHERE Ten LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + ten + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    dsNV.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNV;
    }

    @Override
    public boolean add(NhanVien nv) {
        String sql = "INSERT INTO nhanvien (MaNV, Ten, SoCMND, SoDienThoai, Luong, ChucVu) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaID());
            ps.setString(2, nv.getTen());
            ps.setString(3, nv.getSoCMND());
            ps.setString(4, nv.getSoDienThoai());
            ps.setDouble(5, nv.getLuongCoBan());
            ps.setString(6, nv.getChucVu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(NhanVien nv) {
        String sql = "UPDATE nhanvien SET Ten=?, SoCMND=?, SoDienThoai=?, Luong=?, ChucVu=? WHERE MaNV=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getTen());
            ps.setString(2, nv.getSoCMND());
            ps.setString(3, nv.getSoDienThoai());
            ps.setDouble(4, nv.getLuongCoBan());
            ps.setString(5, nv.getChucVu());
            ps.setString(6, nv.getMaID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM nhanvien WHERE MaNV=?";
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