package com.revworkforce.dao.impl;

import com.revworkforce.dao.HolidayDAO;
import com.revworkforce.entity.Holiday;
import com.revworkforce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HolidayDAOImpl implements HolidayDAO {

    @Override
    public boolean addHoliday(Holiday holiday) {
        String query = "INSERT INTO holidays (date, name) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, holiday.getDate());
            pstmt.setString(2, holiday.getName());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Holiday> getAllHolidays() {
        List<Holiday> holidays = new ArrayList<>();
        String query = "SELECT * FROM holidays ORDER BY date";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                holidays.add(new Holiday(
                        rs.getInt("holiday_id"),
                        rs.getDate("date"),
                        rs.getString("name")));
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return holidays;
    }
}
