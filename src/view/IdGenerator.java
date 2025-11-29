package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import repository.DatabaseConnection;

public class IdGenerator {

    public static String generateNextId(String tableName, String prefix, String idColumnName) {
        int nextId = 1;
        // Lấy phần số của ID lớn nhất hiện tại trong bảng
        String sql = "SELECT MAX(CAST(SUBSTRING(" + idColumnName + ", 3) AS UNSIGNED)) FROM " + tableName;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                // Nếu tìm thấy, lấy số đó + 1
                // Nếu bảng trống, rs.getInt(1) trả về 0 -> nextId vẫn là 1
                nextId = rs.getInt(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Format thành chuỗi 3 chữ số (VD: 1 -> "KH001")
        return String.format("%s%03d", prefix, nextId);
    }
}