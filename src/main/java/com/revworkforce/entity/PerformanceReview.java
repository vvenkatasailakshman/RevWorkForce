package com.revworkforce.entity;

public class PerformanceReview {
    private int reviewId;
    private String employeeId;
    private String reviewPeriod;
    private String achievements;
    private String areasOfImprovement;
    private int selfRating;
    private String managerFeedback;
    private int managerRating;
    private String status; // DRAFT, SUBMITTED, REVIEWED

    public PerformanceReview() {
    }

    public PerformanceReview(int reviewId, String employeeId, String reviewPeriod, String achievements,
            String areasOfImprovement, int selfRating, String managerFeedback, int managerRating, String status) {
        this.reviewId = reviewId;
        this.employeeId = employeeId;
        this.reviewPeriod = reviewPeriod;
        this.achievements = achievements;
        this.areasOfImprovement = areasOfImprovement;
        this.selfRating = selfRating;
        this.managerFeedback = managerFeedback;
        this.managerRating = managerRating;
        this.status = status;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getReviewPeriod() {
        return reviewPeriod;
    }

    public void setReviewPeriod(String reviewPeriod) {
        this.reviewPeriod = reviewPeriod;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getAreasOfImprovement() {
        return areasOfImprovement;
    }

    public void setAreasOfImprovement(String areasOfImprovement) {
        this.areasOfImprovement = areasOfImprovement;
    }

    public int getSelfRating() {
        return selfRating;
    }

    public void setSelfRating(int selfRating) {
        this.selfRating = selfRating;
    }

    public String getManagerFeedback() {
        return managerFeedback;
    }

    public void setManagerFeedback(String managerFeedback) {
        this.managerFeedback = managerFeedback;
    }

    public int getManagerRating() {
        return managerRating;
    }

    public void setManagerRating(int managerRating) {
        this.managerRating = managerRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
