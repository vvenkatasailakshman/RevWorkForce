package com.revworkforce.dao.impl;

import com.revworkforce.dao.PerformanceDAO;
import com.revworkforce.entity.Goal;
import com.revworkforce.entity.PerformanceReview;
import com.revworkforce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceDAOImpl implements PerformanceDAO {

    @Override
    public boolean createPerformanceReview(PerformanceReview review) {
        String query = "INSERT INTO performance_reviews (employee_id, review_period, achievements, areas_of_improvement, self_rating, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, review.getEmployeeId());
            pstmt.setString(2, review.getReviewPeriod());
            pstmt.setString(3, review.getAchievements());
            pstmt.setString(4, review.getAreasOfImprovement());
            pstmt.setInt(5, review.getSelfRating());
            pstmt.setString(6, review.getStatus());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public PerformanceReview getPerformanceReview(String employeeId, String period) {
        String query = "SELECT * FROM performance_reviews WHERE employee_id = ? AND review_period = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            pstmt.setString(2, period);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToReview(rs);
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return null;
    }

    @Override
    public PerformanceReview getReviewById(int reviewId) {
        String query = "SELECT * FROM performance_reviews WHERE review_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reviewId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToReview(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updatePerformanceReview(PerformanceReview review) {
        String query = "UPDATE performance_reviews SET manager_feedback = ?, manager_rating = ?, status = ? WHERE review_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, review.getManagerFeedback());
            pstmt.setInt(2, review.getManagerRating());
            pstmt.setString(3, review.getStatus());
            pstmt.setInt(4, review.getReviewId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean addGoal(Goal goal) {
        String query = "INSERT INTO goals (employee_id, description, deadline, priority) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, goal.getEmployeeId());
            pstmt.setString(2, goal.getDescription());
            pstmt.setDate(3, goal.getDeadline());
            pstmt.setString(4, goal.getPriority());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Goal> getGoalsByEmployee(String employeeId) {
        List<Goal> goals = new ArrayList<>();
        String query = "SELECT * FROM goals WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                goals.add(mapResultSetToGoal(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return goals;
    }

    @Override
    public boolean updateGoalStatus(int goalId, String status) {
        String query = "UPDATE goals SET status = ? WHERE goal_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, goalId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<PerformanceReview> getPendingReviewsByManager(String managerId) {
        List<PerformanceReview> reviews = new ArrayList<>();
        String query = "SELECT r.* FROM performance_reviews r JOIN employees e ON r.employee_id = e.employee_id WHERE e.manager_id = ? AND r.status = 'SUBMITTED'";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, managerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return reviews;
    }

    private PerformanceReview mapResultSetToReview(ResultSet rs) throws SQLException {
        return new PerformanceReview(
                rs.getInt("review_id"),
                rs.getString("employee_id"),
                rs.getString("review_period"),
                rs.getString("achievements"),
                rs.getString("areas_of_improvement"),
                rs.getInt("self_rating"),
                rs.getString("manager_feedback"),
                rs.getInt("manager_rating"),
                rs.getString("status"));
    }

    @Override
    public List<Goal> getGoalsByManager(String managerId) {
        List<Goal> goals = new ArrayList<>();
        String query = "SELECT g.* FROM goals g JOIN employees e ON g.employee_id = e.employee_id WHERE e.manager_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, managerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                goals.add(mapResultSetToGoal(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching team goals: " + e.getMessage());
        }
        return goals;
    }

    private Goal mapResultSetToGoal(ResultSet rs) throws SQLException {
        return new Goal(
                rs.getInt("goal_id"),
                rs.getString("employee_id"),
                rs.getString("description"),
                rs.getDate("deadline"),
                rs.getString("priority"),
                rs.getString("status"));
    }
}
