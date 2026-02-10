package com.revworkforce.dao.impl;

import com.revworkforce.dao.AuditLogDAO;
import com.revworkforce.entity.AuditLog;
import com.revworkforce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAOImpl implements AuditLogDAO {

    @Override
    public boolean createLog(AuditLog auditLog) {
        String query = "INSERT INTO audit_logs (employee_id, action, details) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, auditLog.getEmployeeId());
            pstmt.setString(2, auditLog.getAction());
            pstmt.setString(3, auditLog.getDetails());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Silent error
        }
        return false;
    }

    @Override
    public List<AuditLog> getAllLogs() {
        List<AuditLog> logs = new ArrayList<>();
        String query = "SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 100";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                logs.add(mapResultSetToAuditLog(rs));
            }
        } catch (SQLException e) {
            // Silent error
        }
        return logs;
    }

    @Override
    public List<AuditLog> getLogsByEmployee(String employeeId) {
        List<AuditLog> logs = new ArrayList<>();
        String query = "SELECT * FROM audit_logs WHERE employee_id = ? ORDER BY timestamp DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                logs.add(mapResultSetToAuditLog(rs));
            }
        } catch (SQLException e) {
            // Silent error
        }
        return logs;
    }

    @Override
    public List<AuditLog> getLogsByAction(String action) {
        List<AuditLog> logs = new ArrayList<>();
        String query = "SELECT * FROM audit_logs WHERE action LIKE ? ORDER BY timestamp DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + action + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                logs.add(mapResultSetToAuditLog(rs));
            }
        } catch (SQLException e) {
            // Silent error
        }
        return logs;
    }

    private AuditLog mapResultSetToAuditLog(ResultSet rs) throws SQLException {
        return new AuditLog(
                rs.getInt("log_id"),
                rs.getString("employee_id"),
                rs.getString("action"),
                rs.getString("details"),
                rs.getTimestamp("timestamp"));
    }
}
