package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.example.model.Client;
import org.example.model.Notification;
import org.example.model.NotificationType;


public class NotificationDAO {
    public boolean create(Notification n) throws SQLException {
        String sql = "INSERT INTO notifications (client_id, type, message, notification_date, is_read) VALUES (?, ?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, n.getRecipient().getId());
            ps.setString(2, n.getType().name());
            ps.setString(3, n.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(n.getDate()));
            ps.setBoolean(5, n.isRead());
            if (ps.executeUpdate() != 1) return false;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) n.setId(keys.getInt(1));
            }
            return true;
        }
    }

    public List<Notification> getUserNotifications(int userId) throws SQLException {
        String sql = "SELECT n.id, n.type, n.message, n.notification_date, n.is_read, "
                + "c.id AS c_id, c.username AS c_username, c.name AS c_name, c.date_of_birth AS c_dob "
                + "FROM notifications n JOIN clients c ON c.id = n.client_id "
                + "WHERE n.client_id = ? ORDER BY n.notification_date DESC, n.id DESC";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Notification> list = new ArrayList<>();
                while (rs.next()) {
                    Client recipient = UserDAO.map(rs, "c_");
                    list.add(new Notification(rs.getInt("id"), recipient, NotificationType.valueOf(rs.getString("type")),
                            rs.getString("message"), rs.getTimestamp("notification_date").toLocalDateTime(),
                            rs.getBoolean("is_read")));
                }
                return list;
            }
        }
    }

    public boolean markAsRead(int notificationId) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement("UPDATE notifications SET is_read = TRUE WHERE id = ?")) {
            ps.setInt(1, notificationId);
            return ps.executeUpdate() == 1;
        }
    }
}
