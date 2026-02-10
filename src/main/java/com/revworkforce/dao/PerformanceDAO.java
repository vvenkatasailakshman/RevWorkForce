package com.revworkforce.dao;

import java.util.List;

import com.revworkforce.entity.Goal;
import com.revworkforce.entity.PerformanceReview;

public interface PerformanceDAO {
    boolean createPerformanceReview(PerformanceReview review);

    PerformanceReview getPerformanceReview(String employeeId, String period);

    PerformanceReview getReviewById(int reviewId);

    boolean updatePerformanceReview(PerformanceReview review);

    boolean addGoal(Goal goal);

    List<Goal> getGoalsByEmployee(String employeeId);

    boolean updateGoalStatus(int goalId, String status);

    List<PerformanceReview> getPendingReviewsByManager(String managerId);

    List<Goal> getGoalsByManager(String managerId);
}
