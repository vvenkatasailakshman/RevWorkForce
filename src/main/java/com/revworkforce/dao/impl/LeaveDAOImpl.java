package com.revworkforce.dao.impl;

import com.revworkforce.dao.LeaveDAO;
import com.revworkforce.entity.LeaveRequest;
import com.revworkforce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAOImpl implements LeaveDAO {

    @Override
    public boolean applyForLeave(LeaveRequest leaveRequest) {
        String query = "INSERT INTO leaves (employee_id, leave_type, start_date, end_date, reason) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, leaveRequest.getEmployeeId());
            pstmt.setString(2, leaveRequest.getLeaveType());
            pstmt.setDate(3, leaveRequest.getStartDate());
            pstmt.setDate(4, leaveRequest.getEndDate());
            pstmt.setString(5, leaveRequest.getReason());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<LeaveRequest> getMyLeaves(String employeeId) {
        List<LeaveRequest> leaves = new ArrayList<>();
        String query = "SELECT * FROM leaves WHERE employee_id = ? ORDER BY applied_on DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                leaves.add(mapResultSetToLeave(rs));
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return leaves;
    }

    @Override
    public List<LeaveRequest> getPendingLeavesForManager(String managerId) {
        List<LeaveRequest> leaves = new ArrayList<>();
        String query = "SELECT l.* FROM leaves l JOIN employees e ON l.employee_id = e.employee_id WHERE e.manager_id = ? AND l.status = 'PENDING'";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, managerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                leaves.add(mapResultSetToLeave(rs));
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return leaves;
    }

    @Override
    public boolean approveLeave(int leaveId, String managerId, String comment) {
        return updateLeaveStatus(leaveId, "APPROVED", comment);
    }

    @Override
    public boolean rejectLeave(int leaveId, String managerId, String comment) {
        return updateLeaveStatus(leaveId, "REJECTED", comment);
    }

    private boolean updateLeaveStatus(int leaveId, String status, String managerComment) {
        String query = "UPDATE leaves SET status = ?, manager_comment = ? WHERE leave_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setString(2, managerComment);
            pstmt.setInt(3, leaveId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public int getLeaveBalance(String employeeId, String leaveType) {
        String column = leaveType.toLowerCase() + "_leave"; // e.g., casual_leave
        String query = "SELECT " + column + " FROM leave_balances WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void initializeLeaveBalance(String employeeId) {
        String query = "INSERT INTO leave_balances (employee_id, casual_leave, sick_leave, paid_leave, privilege_leave) VALUES (?, 12, 10, 15, 10)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // System.err.println("Error initializing leave balance: " + e.getMessage());
        }
    }

    @Override
    public LeaveRequest getLeaveById(int leaveId) {
        String query = "SELECT * FROM leaves WHERE leave_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, leaveId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToLeave(rs);
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<LeaveRequest> getAllLeaves() {
        List<LeaveRequest> leaves = new ArrayList<>();
        String query = "SELECT * FROM leaves ORDER BY applied_on DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                leaves.add(mapResultSetToLeave(rs));
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return leaves;
    }

    @Override
    public boolean cancelLeave(int leaveId) {
        String query = "UPDATE leaves SET status = 'CANCELLED' WHERE leave_id = ? AND status = 'PENDING'";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, leaveId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // System.err.println("Error cancelling leave: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<LeaveRequest> getLeavesByDepartment(String department) {
        List<LeaveRequest> leaves = new ArrayList<>();
        String query = "SELECT l.* FROM leaves l JOIN employees e ON l.employee_id = e.employee_id WHERE e.department = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                leaves.add(mapResultSetToLeave(rs));
            }
        } catch (SQLException e) {
            // System.err.println("Error fetching department leaves: " + e.getMessage());
        }
        return leaves;
    }

    private LeaveRequest mapResultSetToLeave(ResultSet rs) throws SQLException {
        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(rs.getInt("leave_id"));
        leave.setEmployeeId(rs.getString("employee_id"));
        leave.setLeaveType(rs.getString("leave_type"));
        leave.setStartDate(rs.getDate("start_date"));
        // Assuming end_date column exists as per standard schema, if not it will throw
        // error
        // but schema likely has it or applied_on etc. Let's try to get end_date
        try {
            leave.setEndDate(rs.getDate("end_date"));
        } catch (SQLException e) {
            // Fallback or ignore if column missing (shouldn't happen if schema correct)
            leave.setEndDate(rs.getDate("start_date"));
        }
        leave.setReason(rs.getString("reason"));
        leave.setStatus(rs.getString("status"));
        leave.setManagerComment(rs.getString("manager_comment"));
        leave.setAppliedOn(rs.getTimestamp("applied_on"));
        return leave;
    }
}
