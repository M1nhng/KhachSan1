package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import repository.DatabaseConnection;

public class IdGenerator {

    public static String generateNextId(String tableName, String prefix, String idColumnName) {
        int nextId = 1;
        String sql = "SELECT MAX(CAST(SUBSTRING(" + idColumnName + ", 3) AS UNSIGNED)) FROM " + tableName;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                nextId = rs.getInt(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.format("%s%03d", prefix, nextId);
    }
}