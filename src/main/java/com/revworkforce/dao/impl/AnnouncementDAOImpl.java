package com.revworkforce.dao.impl;

import com.revworkforce.dao.AnnouncementDAO;
import com.revworkforce.entity.Announcement;
import com.revworkforce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAOImpl implements AnnouncementDAO {

    @Override
    public boolean createAnnouncement(Announcement announcement) {
        String query = "INSERT INTO announcements (title, content, posted_by) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, announcement.getTitle());
            pstmt.setString(2, announcement.getContent());
            pstmt.setString(3, announcement.getPostedBy());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        String query = "SELECT * FROM announcements ORDER BY posted_on DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                announcements.add(new Announcement(
                        rs.getInt("announcement_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("posted_by"),
                        rs.getTimestamp("posted_on")));
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return announcements;
    }
}
