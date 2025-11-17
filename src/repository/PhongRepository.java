package repository;

import Class.KhachHang;
import Class.Phong;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhongRepository implements IPhongRepository {

    // (MỚI) Không cần IKhachHangRepository nữa
    // private IKhachHangRepository khachHangRepo;

    // (MỚI) Dùng constructor rỗng
    public PhongRepository() {
        // Không cần khởi tạo repo khác
    }

    private Phong mapResultSetToPhong(ResultSet rs) throws SQLException {
        Phong p = new Phong();
        p.setMaPhong(rs.getString("MaPhong"));
        p.setLoaiPhong(rs.getString("LoaiPhong"));
        p.setGiaPhong(rs.getDouble("GiaPhong"));
        p.setTrangThai(rs.getBoolean("TrangThai"));

        // (MỚI) Lấy thông tin KhachHang từ JOIN
        String maKH = rs.getString("MaKH");
        if (maKH != null) {
            // Tạo đối tượng KhachHang từ các cột đã JOIN
            KhachHang kh = new KhachHang();
            kh.setMaID(maKH);
            kh.setTen(rs.getString("Ten")); // Lấy từ bảng khachhang
            kh.setSoCMND(rs.getString("SoCMND")); // Lấy từ bảng khachhang
            kh.setSoDienThoai(rs.getString("SoDienThoai")); // Lấy từ bảng khachhang
            kh.setEmail(rs.getString("Email")); // Lấy từ bảng khachhang

            p.setKhachThue(kh);
        }
        return p;
    }

    @Override
    public List<Phong> getAll() {
        List<Phong> dsPhong = new ArrayList<>();
        // (MỚI) Dùng LEFT JOIN
        String sql = "SELECT p.*, kh.Ten, kh.SoCMND, kh.SoDienThoai, kh.Email " +
                "FROM phong p " +
                "LEFT JOIN khachhang kh ON p.MaKH = kh.MaKH";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // mapResultSetToPhong bây giờ đã bao gồm cả việc map KhachHang
                dsPhong.add(mapResultSetToPhong(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhong;
    }

    @Override
    public Phong getById(String id) {
        // (MỚI) Dùng LEFT JOIN
        String sql = "SELECT p.*, kh.Ten, kh.SoCMND, kh.SoDienThoai, kh.Email " +
                "FROM phong p " +
                "LEFT JOIN khachhang kh ON p.MaKH = kh.MaKH " +
                "WHERE p.MaPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPhong(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ... (Hàm add, update, delete giữ nguyên như phiên bản trước) ...

    @Override
    public boolean add(Phong p) {
        String sql = "INSERT INTO phong (MaPhong, LoaiPhong, GiaPhong, TrangThai) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getLoaiPhong());
            ps.setDouble(3, p.getGiaPhong());
            ps.setBoolean(4, false); // Mặc định là trống

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Phong p) {
        String sql = "UPDATE phong SET LoaiPhong = ?, GiaPhong = ?, TrangThai = ?, MaKH = ? WHERE MaPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getLoaiPhong());
            ps.setDouble(2, p.getGiaPhong());
            ps.setBoolean(3, p.isTrangThai());

            if (p.isTrangThai() && p.getKhachThue() != null) {
                ps.setString(4, p.getKhachThue().getMaID());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }

            ps.setString(5, p.getMaPhong());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM phong WHERE MaPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Phong> findByName(String ten) {
        List<Phong> dsPhong = new ArrayList<>();
        // Tìm kiếm phòng theo tên khách hàng đang thuê
        String sql = "SELECT p.*, kh.Ten, kh.SoCMND, kh.SoDienThoai, kh.Email " +
                "FROM phong p " +
                "LEFT JOIN khachhang kh ON p.MaKH = kh.MaKH " +
                "WHERE kh.Ten LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + ten + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dsPhong.add(mapResultSetToPhong(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhong;
    }
}