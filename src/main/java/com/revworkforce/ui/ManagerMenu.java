package com.revworkforce.ui;

import java.sql.Date;
import java.util.List;

import com.revworkforce.entity.Employee;
import com.revworkforce.entity.Goal;
import com.revworkforce.entity.LeaveRequest;
import com.revworkforce.entity.PerformanceReview;

public class ManagerMenu extends EmployeeMenu {

    public ManagerMenu(Employee employee) {
        super(employee);
    }

    @Override
    public void display() {
        while (true) {
            notificationService.checkAndGenerateReminders(employee);
            int unreadCount = notificationService.getUnreadCount(employee.getEmployeeId());
            String notificationBadge = unreadCount > 0 ? " (" + unreadCount + " Unread Notifications)" : "";

            System.out.println("\n=== MANAGER DASHBOARD (" + employee.getName() + ")" + notificationBadge + " ===");
            // Employee Options
            System.out.println("--- Employee Actions ---");
            System.out.println("1. View Profile");
            System.out.println("2. Update Contact Info");
            System.out.println("3. Apply for Leave");
            System.out.println("4. View My Leave Request History");
            System.out.println("5. Cancel Pending Leave");
            System.out.println("6. Submit Self-Review");
            System.out.println("7. View My Goals");
            System.out.println("8. View Holidays");
            System.out.println("9. View Announcements");
            System.out.println("10. View Upcoming Birthdays");
            System.out.println("11. View Upcoming Work Anniversaries");
            System.out.println("12. Notifications (" + unreadCount + ")");

            // Manager Options
            System.out.println("--- Manager Actions ---");
            System.out.println("13. Manage Team Leaves");
            System.out.println("14. Manage Team Performance");
            System.out.println("15. Manage Team Goals");
            System.out.println("16. View Team Members");
            System.out.println("0. Logout");
            System.out.print("Select an option: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    updateContactInfo();
                    break;
                case 3:
                    applyForLeave();
                    break;
                case 4:
                    viewLeaveHistory();
                    break;
                case 5:
                    cancelLeave();
                    break;
                case 6:
                    submitSelfReview();
                    break;
                case 7:
                    viewMyGoals();
                    break;
                case 8:
                    viewHolidays();
                    break;
                case 9:
                    viewAnnouncements();
                    break;
                case 10:
                    viewBirthdays();
                    break;
                case 11:
                    viewAnniversaries();
                    break;
                case 12:
                    viewNotifications();
                    break;
                case 13:
                    manageTeamLeaves();
                    break;
                case 14:
                    manageTeamPerformance();
                    break;
                case 15:
                    manageTeamGoals();
                    break;
                case 16:
                    viewTeamMembers();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void manageTeamLeaves() {
        List<LeaveRequest> requests = leaveService.getPendingLeavesForManager(employee.getEmployeeId());
        System.out.println("\n--- Pending Team Leave Requests ---");
        if (requests.isEmpty()) {
            System.out.println("No pending leave requests.");
            return;
        }

        for (LeaveRequest req : requests) {
            System.out.println(req);
        }

        System.out.print("Enter Leave ID to action (or 0 to cancel): ");
        int leaveId = 0;
        try {
            leaveId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        if (leaveId == 0)
            return;

        System.out.print("Approve (A) or Reject (R)? ");
        String decision = scanner.nextLine().toUpperCase();
        System.out.print("Comment: ");
        String comment = scanner.nextLine();

        if (decision.equals("A")) {
            if (leaveService.approveLeave(leaveId, employee.getEmployeeId(), comment)) {
                System.out.println("Leave Approved.");
            } else {
                System.out.println("Operation failed (ID mismatch or already processed).");
            }
        } else if (decision.equals("R")) {
            if (leaveService.rejectLeave(leaveId, employee.getEmployeeId(), comment)) {
                System.out.println("Leave Rejected.");
            } else {
                System.out.println("Operation failed.");
            }
        }
    }

    private void manageTeamPerformance() {
        System.out.println("\n--- Team Performance Reviews ---");
        List<PerformanceReview> reviews = performanceService.getPendingReviewsForManager(employee.getEmployeeId());
        if (reviews.isEmpty()) {
            System.out.println("No pending reviews.");
            return;
        }

        for (PerformanceReview rev : reviews) {
            System.out.println("Review ID: " + rev.getReviewId() + " | EmpID: " + rev.getEmployeeId() + " | Period: "
                    + rev.getReviewPeriod());
            System.out.println("  Achievements: " + rev.getAchievements());
            System.out.println("  Self Rating: " + rev.getSelfRating());
        }

        System.out.print("Enter Review ID to provide feedback (or 0 to cancel): ");
        int reviewId = 0;
        try {
            reviewId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }
        if (reviewId == 0)
            return;

        System.out.print("Manager Feedback: ");
        String feedback = scanner.nextLine();

        System.out.print("Manager Rating (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());

        if (performanceService.submitReviewFeedback(reviewId, feedback, rating)) {
            System.out.println("Feedback submitted successfully.");
        } else {
            System.out.println("Failed to submit feedback.");
        }
    }

    private void manageTeamGoals() {
        System.out.println("\n1. View Team Goals");
        System.out.println("2. Set New Goal");
        System.out.print("Select: ");
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        if (choice == 1) {
            List<Goal> goals = performanceService.getGoalsByManager(employee.getEmployeeId());
            if (goals.isEmpty())
                System.out.println("No goals found.");
            for (Goal g : goals) {
                System.out.println("Goal ID: " + g.getGoalId() + " | Emp: " + g.getEmployeeId() + " | "
                        + g.getDescription() + " | " + g.getStatus());
            }
        } else if (choice == 2) {
            System.out.print("Enter Employee ID: ");
            String empId = scanner.nextLine();
            // Verify employee reports to manager
            if (!employeeService.getDirectReports(employee.getEmployeeId()).stream()
                    .anyMatch(e -> e.getEmployeeId().equals(empId))) {
                System.out.println("Employee does not report to you.");
                return;
            }

            System.out.print("Description: ");
            String desc = scanner.nextLine();
            Date deadlineDate = getValidDate("Deadline (YYYY-MM-DD): ");
            if (deadlineDate == null)
                return;

            Goal goal = new Goal();
            goal.setEmployeeId(empId);
            goal.setDescription(desc);
            goal.setDeadline(deadlineDate);
            goal.setStatus("PENDING");

            if (performanceService.setGoal(goal)) {
                System.out.println("Goal assigned.");
            } else {
                System.out.println("Failed to assign goal.");
            }
        } else {
            System.out.println("Enter valid choice.");
        }
    }

    private void viewTeamMembers() {
        System.out.println("\n--- My Team ---");
        List<Employee> team = employeeService.getDirectReports(employee.getEmployeeId());
        for (Employee e : team) {
            System.out.println(
                    "ID: " + e.getEmployeeId() + " | Name: " + e.getName() + " | Designation: " + e.getDesignation());
        }
    }
}
