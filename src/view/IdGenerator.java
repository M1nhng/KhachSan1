package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import repository.DatabaseConnection;

public class IdGenerator {

    /**
     * Hàm sinh mã tự động dựa trên Database
     * 
     * @param tableName    Tên bảng (khachhang, nhanvien)
     * @param prefix       Tiền tố (KH, NV)
     * @param idColumnName Tên cột khóa chính (MaKH, MaNV)
     * @return Mã mới (ví dụ: KH005)
     */
    public static String generateNextId(String tableName, String prefix, String idColumnName) {
        int nextId = 1;
        // Query này lấy phần số của ID lớn nhất hiện tại
        // SUBSTRING(cot, 3) để cắt bỏ 2 chữ cái đầu (VD: 'KH' hoặc 'NV')
        String sql = "SELECT MAX(CAST(SUBSTRING(" + idColumnName + ", 3) AS UNSIGNED)) FROM " + tableName;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                // Lấy số lớn nhất + 1. Nếu bảng chưa có gì thì rs.getInt(1) trả về 0 -> nextId
                // = 1
                nextId = rs.getInt(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Format lại thành chuỗi có 3 chữ số (VD: 5 -> KH005)
        return String.format("%s%03d", prefix, nextId);
    }
}