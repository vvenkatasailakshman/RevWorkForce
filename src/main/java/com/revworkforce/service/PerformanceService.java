package com.revworkforce.service;

import com.revworkforce.dao.PerformanceDAO;
import com.revworkforce.dao.impl.PerformanceDAOImpl;
import com.revworkforce.entity.Goal;
import com.revworkforce.entity.PerformanceReview;

import java.util.List;

public class PerformanceService {
    private PerformanceDAO performanceDAO;
    private NotificationService notificationService;

    public PerformanceService() {
        this.performanceDAO = new PerformanceDAOImpl();
        this.notificationService = new NotificationService();
    }

    public boolean createSelfAssessment(PerformanceReview review) {
        // Business logic: check if review period already exists, etc.
        return performanceDAO.createPerformanceReview(review);
    }

    public PerformanceReview getReview(String employeeId, String period) {
        return performanceDAO.getPerformanceReview(employeeId, period);
    }

    public boolean submitReviewFeedback(int reviewId, String feedback, int rating) {
        // Fetch existing review first to get details if needed, but here we just update
        PerformanceReview review = new PerformanceReview();
        review.setReviewId(reviewId);
        review.setManagerFeedback(feedback);
        review.setManagerRating(rating);
        review.setStatus("REVIEWED");

        boolean updated = performanceDAO.updatePerformanceReview(review);
        if (updated) {
            PerformanceReview updatedReview = performanceDAO.getReviewById(reviewId);
            if (updatedReview != null) {
                com.revworkforce.entity.Notification notification = new com.revworkforce.entity.Notification();
                notification.setEmployeeId(updatedReview.getEmployeeId());
                notification.setMessage("Performance Review Feedback received for " + updatedReview.getReviewPeriod());
                notification.setRead(false);
                notification.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                notificationService.createNotification(notification);
            }
        }
        return updated;
    }

    public boolean setGoal(Goal goal) {
        return performanceDAO.addGoal(goal);
    }

    public List<Goal> getEmployeeGoals(String employeeId) {
        return performanceDAO.getGoalsByEmployee(employeeId);
    }

    public boolean updateGoalStatus(int goalId, String status) {
        return performanceDAO.updateGoalStatus(goalId, status);
    }

    public List<PerformanceReview> getPendingReviewsForManager(String managerId) {
        return performanceDAO.getPendingReviewsByManager(managerId);
    }

    public List<Goal> getGoalsByManager(String managerId) {
        return performanceDAO.getGoalsByManager(managerId);
    }
}
