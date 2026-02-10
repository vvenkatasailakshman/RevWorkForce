package com.revworkforce.dao.impl;

import com.revworkforce.dao.NotificationDAO;
import com.revworkforce.entity.Notification;
import com.revworkforce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOImpl implements NotificationDAO {

    @Override
    public boolean createNotification(Notification notification) {
        String query = "INSERT INTO notifications (employee_id, message) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, notification.getEmployeeId());
            pstmt.setString(2, notification.getMessage());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Notification> getNotificationsByEmployee(String employeeId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE employee_id = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getInt("notification_id"),
                        rs.getString("employee_id"),
                        rs.getString("message"),
                        rs.getBoolean("is_read"),
                        rs.getTimestamp("created_at")));
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return notifications;
    }

    @Override
    public boolean markAsRead(int notificationId) {
        String query = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, notificationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public int getUnreadCount(String employeeId) {
        String query = "SELECT COUNT(*) FROM notifications WHERE employee_id = ? AND is_read = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting unread notifications: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean existsToday(String employeeId, String messageSubstring) {
        String query = "SELECT COUNT(*) FROM notifications WHERE employee_id = ? AND message LIKE ? AND DATE(created_at) = CURDATE()";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            pstmt.setString(2, "%" + messageSubstring + "%");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking existing notifications: " + e.getMessage());
        }
        return false;
    }
}
