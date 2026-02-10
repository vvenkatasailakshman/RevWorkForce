package com.revworkforce.ui;

import com.revworkforce.entity.Announcement;
import com.revworkforce.entity.Employee;
import com.revworkforce.entity.Goal;
import com.revworkforce.entity.Holiday;
import com.revworkforce.entity.LeaveRequest;
import com.revworkforce.entity.PerformanceReview;
import com.revworkforce.service.EmployeeService;
import com.revworkforce.service.LeaveService;
import com.revworkforce.service.PerformanceService;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class EmployeeMenu {
    protected Employee employee; // Protected for ManagerMenu to access/extend if needed
    protected EmployeeService employeeService;
    protected LeaveService leaveService;
    protected PerformanceService performanceService;
    protected com.revworkforce.service.NotificationService notificationService;
    protected Scanner scanner;

    public EmployeeMenu(Employee employee) {
        this.employee = employee;
        this.employeeService = new EmployeeService();
        this.leaveService = new LeaveService();
        this.performanceService = new PerformanceService();
        this.notificationService = new com.revworkforce.service.NotificationService();
        this.scanner = new Scanner(System.in);
    }

    public void display() {
        while (true) {
            notificationService.checkAndGenerateReminders(employee);
            int unreadCount = notificationService.getUnreadCount(employee.getEmployeeId());
            String notificationBadge = unreadCount > 0 ? " (" + unreadCount + " Unread Notifications)" : "";

            System.out.println("\n=== EMPLOYEE DASHBOARD (" + employee.getName() + ")" + notificationBadge + " ===");
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
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    protected void viewProfile() {
        System.out.println("\n=========================================");
        System.out.println("           MY PROFILE");
        System.out.println("=========================================");
        System.out.printf("%-20s : %s%n", "Employee ID", employee.getEmployeeId());
        System.out.printf("%-20s : %s%n", "Name", employee.getName());
        System.out.printf("%-20s : %s%n", "Email", employee.getEmail());
        System.out.printf("%-20s : %s%n", "Role", employee.getRole());
        System.out.printf("%-20s : %s%n", "Phone",
                employee.getPhoneNumber() != null ? employee.getPhoneNumber() : "N/A");
        System.out.printf("%-20s : %s%n", "Address", employee.getAddress() != null ? employee.getAddress() : "N/A");
        System.out.printf("%-20s : %s%n", "Department",
                employee.getDepartment() != null ? employee.getDepartment() : "N/A");
        System.out.printf("%-20s : %s%n", "Designation",
                employee.getDesignation() != null ? employee.getDesignation() : "N/A");
        System.out.printf("%-20s : %s%n", "Manager ID",
                employee.getManagerId() != null ? employee.getManagerId() : "N/A");
        System.out.printf("%-20s : %s%n", "Joining Date", employee.getJoiningDate());
        System.out.printf("%-20s : %s%n", "Emergency Contact",
                employee.getEmergencyContact() != null ? employee.getEmergencyContact() : "N/A");
        System.out.println("=========================================");
    }

    protected void updateContactInfo() {
        System.out.println("\n--- Update Contact Info ---");
        System.out.print("New Phone Number (leave blank to keep current): ");
        String phone = scanner.nextLine();
        System.out.print("New Address (leave blank to keep current): ");
        String address = scanner.nextLine();
        System.out.print("New Emergency Contact (leave blank to keep current): ");
        String ec = scanner.nextLine();

        if (!phone.isEmpty()) {
            employee.setPhoneNumber(phone);
        }
        if (!address.isEmpty()) {
            employee.setAddress(address);
        }
        if (!ec.isEmpty()) {
            employee.setEmergencyContact(ec);
        }

        if (employeeService.updateProfile(employee)) {
            System.out.println("Profile updated successfully!");
        } else {
            System.out.println("Failed to update profile.");
        }
    }

    protected void applyForLeave() {
        System.out.println("\n--- Apply for Leave ---");
        System.out.println("Select Leave Type:");
        System.out.println("1. CASUAL");
        System.out.println("2. SICK");
        System.out.println("3. PAID");
        System.out.println("4. PRIVILEGE");
        System.out.print("Enter choice: ");

        int typeChoice = -1;
        try {
            typeChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        String leaveType;
        switch (typeChoice) {
            case 1:
                leaveType = "CASUAL";
                break;
            case 2:
                leaveType = "SICK";
                break;
            case 3:
                leaveType = "PAID";
                break;
            case 4:
                leaveType = "PRIVILEGE";
                break;
            default:
                System.out.println("Invalid leave type.");
                return;
        }

        Date startDate = getValidDate("Start Date (YYYY-MM-DD): ");
        if (startDate == null)
            return;

        Date endDate = getValidDate("End Date (YYYY-MM-DD): ");
        if (endDate == null)
            return;

        if (endDate.before(startDate)) {
            System.out.println("Error: End date cannot be before start date.");
            return;
        }

        System.out.print("Reason: ");
        String reason = scanner.nextLine();

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(employee.getEmployeeId());
        leave.setLeaveType(leaveType);
        leave.setStartDate(startDate);
        leave.setEndDate(endDate);
        leave.setReason(reason);

        if (leaveService.applyForLeave(leave)) {
            System.out.println("Leave application submitted.");
        } else {
            System.out.println("Failed to submit leave application.");
        }
    }

    /**
     * Helper to get a valid SQL Date from user input.
     * Reprompts until valid.
     */
    protected Date getValidDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                // Simple regex check before valueOf to avoid some IllegalArgumentException
                // messages being cryptic
                if (!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    throw new IllegalArgumentException("Invalid format");
                }
                return Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please enter valid date (YYYY-MM-DD).");
            }
        }
    }

    protected void viewLeaveHistory() {
        System.out.println("\n--- My Leave History ---");
        List<LeaveRequest> history = leaveService.getMyLeaves(employee.getEmployeeId());
        if (history.isEmpty()) {
            System.out.println("No leave history found.");
        } else {
            for (LeaveRequest req : history) {
                System.out.println(req);
            }
        }
    }

    protected void cancelLeave() {
        System.out.print("Enter Leave ID to cancel: ");
        int leaveId = -1;
        try {
            leaveId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        if (leaveService.cancelLeave(leaveId)) {
            System.out.println("Leave cancelled successfully.");
        } else {
            System.out.println("Failed to cancel leave (might not be pending or doesn't exist).");
        }
    }

    protected void submitSelfReview() {
        System.out.println("\n--- Submit Self Review ---");
        System.out.print("Review Period (e.g., 2023-Q1): ");
        String period = scanner.nextLine();
        System.out.print("Achievements: ");
        String achievements = scanner.nextLine();
        System.out.print("Areas of Improvement: ");
        String improvements = scanner.nextLine();
        System.out.print("Self Rating (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());

        PerformanceReview review = new PerformanceReview();
        review.setEmployeeId(employee.getEmployeeId());
        review.setReviewPeriod(period);
        review.setAchievements(achievements);
        review.setAreasOfImprovement(improvements);
        review.setSelfRating(rating);

        if (performanceService.createSelfAssessment(review)) {
            System.out.println("Self review submitted.");
        } else {
            System.out.println("Failed to submit review.");
        }
    }

    protected void viewMyGoals() {
        System.out.println("\n--- My Goals ---");
        List<Goal> goals = performanceService.getEmployeeGoals(employee.getEmployeeId());
        if (goals.isEmpty()) {
            System.out.println("No goals assigned.");
        } else {
            for (Goal g : goals) {
                System.out
                        .println("Goal ID: " + g.getGoalId() + " - " + g.getDescription() + " [" + g.getStatus() + "]");
            }
        }
    }

    protected void viewHolidays() {
        System.out.println("\n--- Holidays ---");
        List<Holiday> holidays = employeeService.getHolidays();
        if (holidays.isEmpty()) {
            System.out.println("No holidays found.");
        } else {
            for (Holiday h : holidays) {
                System.out.println(h.getDate() + ": " + h.getName());
            }
        }
    }

    protected void viewAnnouncements() {
        System.out.println("\n--- Announcements ---");
        List<Announcement> announcements = employeeService.getAnnouncements();
        if (announcements.isEmpty()) {
            System.out.println("No announcements.");
        } else {
            for (Announcement a : announcements) {
                System.out.println(a.getTitle() + ": " + a.getContent() + " (Posted on " + a.getPostedOn() + ")");
            }
        }
    }

    protected void viewBirthdays() {
        System.out.println("\n--- Upcoming Birthdays ---");
        List<Employee> bdays = employeeService.getUpcomingBirthdays();
        if (bdays.isEmpty()) {
            System.out.println("No upcoming birthdays.");
        } else {
            for (Employee e : bdays) {
                String deptInfo = (e.getDepartment() != null && !e.getDepartment().isEmpty()
                        && !e.getDepartment().equalsIgnoreCase("null"))
                                ? " (" + e.getDepartment() + ")"
                                : "";
                System.out.println(e.getName() + deptInfo + " on " + e.getDob());
            }
        }
    }

    protected void viewAnniversaries() {
        System.out.println("\n--- Upcoming Work Anniversaries ---");
        List<Employee> anns = employeeService.getUpcomingWorkAnniversaries();
        if (anns.isEmpty()) {
            System.out.println("No upcoming anniversaries.");
        } else {
            for (Employee e : anns) {
                String deptInfo = (e.getDepartment() != null && !e.getDepartment().isEmpty()
                        && !e.getDepartment().equalsIgnoreCase("null"))
                                ? " (" + e.getDepartment() + ")"
                                : "";
                System.out.println(e.getName() + deptInfo + " joined on " + e.getJoiningDate());
            }
        }
    }

    protected void viewNotifications() {
        System.out.println("\n--- My Notifications ---");
        List<com.revworkforce.entity.Notification> notifications = notificationService
                .getNotificationsByEmployee(employee.getEmployeeId());
        if (notifications.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }

        for (com.revworkforce.entity.Notification n : notifications) {
            String status = n.isRead() ? "[READ]" : "[UNREAD]";
            System.out.printf("ID: %d | %s | %s | %s%n", n.getNotificationId(), status, n.getCreatedAt(),
                    n.getMessage());
        }

        System.out.print("\nEnter Notification ID to mark as read (or 0 to go back): ");
        int notifId = -1;
        try {
            notifId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (notifId != 0) {
            if (notificationService.markAsRead(notifId)) {
                System.out.println("Marked as read.");
            } else {
                System.out.println("Failed to mark as read (ID might be invalid).");
            }
        }
    }
}
