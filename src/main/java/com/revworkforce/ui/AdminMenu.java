package com.revworkforce.ui;

import com.revworkforce.entity.Announcement;
import com.revworkforce.entity.Employee;
import com.revworkforce.entity.Holiday;
import com.revworkforce.entity.LeaveRequest;
import com.revworkforce.service.AdminService;
import com.revworkforce.service.EmployeeService;
import com.revworkforce.service.LeaveService;
import com.revworkforce.service.NotificationService;
import com.revworkforce.service.AuditLogService;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private Employee admin;
    private AdminService adminService;
    private EmployeeService employeeService;
    private LeaveService leaveService;
    private NotificationService notificationService;
    private AuditLogService auditLogService;
    private Scanner scanner;

    public AdminMenu(Employee admin) {
        this.admin = admin;
        this.adminService = new AdminService();
        this.employeeService = new EmployeeService();
        this.leaveService = new LeaveService();
        this.notificationService = new NotificationService();
        this.auditLogService = new AuditLogService();
        this.scanner = new Scanner(System.in);
    }

    private void viewAllEmployees() {
        System.out.println(
                "\n-------------------------------------------------------------------------------------------------------");
        System.out.println("                                         ALL EMPLOYEES");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s | %-20s | %-25s | %-15s | %-10s%n", "ID", "Name", "Email", "Role", "Status");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------");
        List<Employee> employees = adminService.viewAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("                                     No employees found.");
        } else {
            for (Employee emp : employees) {
                String status = emp.isActive() ? "Active" : "Inactive";
                System.out.printf("%-10s | %-20s | %-25s | %-15s | %-10s%n",
                        emp.getEmployeeId(), emp.getName(), emp.getEmail(), emp.getRole(), status);
            }
        }
        System.out.println(
                "-------------------------------------------------------------------------------------------------------");
    }

    private void addNewEmployee() {
        System.out.println("\n--- Add New Employee ---");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Department: ");
        String dept = scanner.nextLine();
        System.out.print("Designation: ");
        String desig = scanner.nextLine();
        System.out.print("Role (EMPLOYEE/MANAGER/ADMIN): ");
        String role = scanner.nextLine().toUpperCase();
        System.out.print("Manager ID (or leave blank): ");
        String mgrId = scanner.nextLine();
        if (mgrId.trim().isEmpty()) {
            mgrId = null;
        }

        Date joiningDate = getValidDate("Joining Date (YYYY-MM-DD): ");
        if (joiningDate == null)
            return;

        Date dob = getValidDate("DOB (YYYY-MM-DD): ");
        if (dob == null)
            return;

        System.out.print("Emergency Contact: ");
        String emergencyContact = scanner.nextLine();

        System.out.print("Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        Employee newEmp = new Employee(null, name, email, password, phone, address, dept, desig, role, mgrId,
                joiningDate, dob, emergencyContact, salary, true);

        if (adminService.addNewEmployee(newEmp)) {
            System.out.println("Employee added successfully!");
        } else {
            System.out.println("Failed to add employee.");
        }
    }

    private void manageHolidays() {
        Date holidayDate = getValidDate("Enter holiday date (YYYY-MM-DD): ");
        if (holidayDate == null)
            return;

        System.out.print("Enter holiday name: ");
        String name = scanner.nextLine();

        Holiday holiday = new Holiday();
        holiday.setDate(holidayDate);
        holiday.setName(name);

        if (adminService.addHoliday(holiday)) {
            System.out.println("Holiday added.");
        } else {
            System.out.println("Failed to add holiday.");
        }
    }

    private void postAnnouncement() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter content: ");
        String content = scanner.nextLine();

        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setPostedBy(admin.getEmployeeId());

        if (adminService.postAnnouncement(announcement)) {
            System.out.println("Announcement posted.");
        } else {
            System.out.println("Failed to post announcement.");
        }
    }

    private void viewLeaveReports() {
        System.out.print("Enter Department to view reports: ");
        String dept = scanner.nextLine();
        List<LeaveRequest> leaves = leaveService.getLeavesByDepartment(dept);
        System.out.println("\n--- Leave Reports for " + dept + " ---");
        if (leaves.isEmpty()) {
            System.out.println("No leaves found.");
        } else {
            for (LeaveRequest leave : leaves) {
                System.out.println(leave);
            }
        }
    }

    private void viewNotifications() {
        System.out.println("\n--- My Notifications ---");
        List<com.revworkforce.entity.Notification> notifications = notificationService
                .getNotificationsByEmployee(admin.getEmployeeId());
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

    public void display() {
        while (true) {
            notificationService.checkAndGenerateReminders(admin);
            int unreadCount = notificationService.getUnreadCount(admin.getEmployeeId());
            String notificationBadge = unreadCount > 0 ? " (" + unreadCount + " Unread Notifications)" : "";

            System.out.println("\n=== ADMIN DASHBOARD" + notificationBadge + " ===");
            System.out.println("1. View All Employees");
            System.out.println("2. Add New Employee");
            System.out.println("3. Search Employees");
            System.out.println("4. Update Employee");
            System.out.println("5. Deactivate Employee");
            System.out.println("6. Manage Holidays");
            System.out.println("7. Post Announcement");
            System.out.println("8. View Leave Reports");
            System.out.println("9. My Admin Profile");
            System.out.println("10. Notifications (" + unreadCount + ")");
            System.out.println("11. View Audit Logs");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                scanner.nextLine(); // Clear invalid input
            }

            switch (choice) {
                case 1:
                    viewAllEmployees();
                    break;
                case 2:
                    addNewEmployee();
                    break;
                case 3:
                    searchEmployees();
                    break;
                case 4:
                    updateEmployee();
                    break;
                case 5:
                    deactivateEmployee();
                    break;
                case 6:
                    manageHolidays();
                    break;
                case 7:
                    postAnnouncement();
                    break;
                case 8:
                    viewLeaveReports();
                    break;
                case 9:
                    handleMyAdminProfile();
                    break;
                case 10:
                    viewNotifications();
                    break;
                case 11:
                    viewAuditLogs();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleMyAdminProfile() {
        while (true) {
            System.out.println("\n--- My Admin Profile ---");
            System.out.println("1. View Profile");
            System.out.println("2. Update My Profile");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
            }

            switch (choice) {
                case 1:
                    viewAdminProfileFormatted();
                    break;
                case 2:
                    updateAdminProfile();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAdminProfileFormatted() {
        System.out.println("\n=========================================");
        System.out.println("           ADMIN PROFILE");
        System.out.println("=========================================");
        System.out.printf("%-20s : %s%n", "Employee ID", admin.getEmployeeId());
        System.out.printf("%-20s : %s%n", "Name", admin.getName());
        System.out.printf("%-20s : %s%n", "Email", admin.getEmail());
        System.out.printf("%-20s : %s%n", "Role", admin.getRole());
        System.out.printf("%-20s : %s%n", "Phone", admin.getPhoneNumber() != null ? admin.getPhoneNumber() : "N/A");
        System.out.printf("%-20s : %s%n", "Address", admin.getAddress() != null ? admin.getAddress() : "N/A");
        System.out.printf("%-20s : %s%n", "Department", admin.getDepartment() != null ? admin.getDepartment() : "N/A");
        System.out.printf("%-20s : %s%n", "Designation",
                admin.getDesignation() != null ? admin.getDesignation() : "N/A");
        System.out.println("=========================================");
    }

    private void updateAdminProfile() {
        System.out.println("\n--- Update My Profile ---");
        System.out.println("Leave blank to keep current value.");

        System.out.print("Name (" + admin.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty())
            admin.setName(name);

        System.out.print("Phone (" + admin.getPhoneNumber() + "): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty())
            admin.setPhoneNumber(phone);

        System.out.print("Address (" + admin.getAddress() + "): ");
        String address = scanner.nextLine();
        if (!address.isEmpty())
            admin.setAddress(address);

        System.out.print("Emergency Contact ("
                + (admin.getEmergencyContact() == null ? "None" : admin.getEmergencyContact()) + "): ");
        String ec = scanner.nextLine();
        if (!ec.isEmpty())
            admin.setEmergencyContact(ec);

        if (adminService.updateEmployeeDetails(admin)) {
            System.out.println("Profile updated successfully.");
        } else {
            System.out.println("Failed to update profile.");
        }
    }

    private void updateEmployee() {
        System.out.print("Enter Employee ID to update: ");
        String empId = scanner.nextLine();
        Employee emp = employeeService.getEmployeeProfile(empId);
        if (emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("Updating details for: " + emp.getName());
        System.out.println("Leave blank to keep current value.");

        System.out.print("Name (" + emp.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty())
            emp.setName(name);

        System.out.print("Phone (" + emp.getPhoneNumber() + "): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty())
            emp.setPhoneNumber(phone);

        System.out.print("Address (" + emp.getAddress() + "): ");
        String address = scanner.nextLine();
        if (!address.isEmpty())
            emp.setAddress(address);

        System.out.print("Department (" + emp.getDepartment() + "): ");
        String dept = scanner.nextLine();
        if (!dept.isEmpty())
            emp.setDepartment(dept);

        System.out.print("Designation (" + emp.getDesignation() + "): ");
        String desig = scanner.nextLine();
        if (!desig.isEmpty())
            emp.setDesignation(desig);

        System.out.print("Manager ID (" + (emp.getManagerId() == null ? "None" : emp.getManagerId()) + "): ");
        String mgrId = scanner.nextLine();
        if (!mgrId.isEmpty())
            emp.setManagerId(mgrId.equalsIgnoreCase("none") ? null : mgrId);

        System.out.print("Emergency Contact ("
                + (emp.getEmergencyContact() == null ? "None" : emp.getEmergencyContact()) + "): ");
        String ec = scanner.nextLine();
        if (!ec.isEmpty())
            emp.setEmergencyContact(ec);

        System.out.print("Salary (" + emp.getSalary() + "): ");
        String salaryStr = scanner.nextLine();
        if (!salaryStr.isEmpty()) {
            try {
                emp.setSalary(Double.parseDouble(salaryStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid salary format. Skipping salary update.");
            }
        }

        System.out.print("Active (" + emp.isActive() + ") [true/false]: ");
        String activeStr = scanner.nextLine();
        if (!activeStr.isEmpty()) {
            emp.setActive(Boolean.parseBoolean(activeStr));
        }

        if (adminService.updateEmployeeDetails(emp)) {
            System.out.println("Employee updated successfully.");
        } else {
            System.out.println("Failed to update employee.");
        }
    }

    private void searchEmployees() {
        System.out.print("Enter search query (Name/Dept/Designation): ");
        String query = scanner.nextLine();
        List<Employee> results = employeeService.searchEmployees(query);
        if (results.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Employee emp : results) {
                System.out.println(emp);
            }
        }
    }

    private void deactivateEmployee() {
        System.out.print("Enter Employee ID to deactivate: ");
        String empId = scanner.nextLine();
        if (employeeService.deactivateEmployee(empId)) {
            System.out.println("Employee deactivated successfully.");
        } else {
            System.out.println("Failed to deactivate employee.");
        }
    }

    private Date getValidDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                if (!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    throw new IllegalArgumentException("Invalid format");
                }
                return Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    private void viewAuditLogs() {
        System.out.println("\n=== SYSTEM AUDIT LOGS ===");
        System.out.println("1. View All Logs");
        System.out.println("2. View Logs by Employee ID");
        System.out.println("3. View Logs by Action");
        System.out.println("0. Back");
        System.out.print("Enter choice: ");

        int choice = -1;
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine();
        } else {
            scanner.nextLine();
            System.out.println("Invalid input.");
            return;
        }

        List<com.revworkforce.entity.AuditLog> logs = null;

        switch (choice) {
            case 1:
                logs = auditLogService.getAllLogs();
                break;
            case 2:
                System.out.print("Enter Employee ID: ");
                String empId = scanner.nextLine();
                logs = auditLogService.getLogsByEmployee(empId);
                break;
            case 3:
                System.out.print("Enter Action keyword: ");
                String action = scanner.nextLine();
                logs = auditLogService.getLogsByAction(action);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (logs == null || logs.isEmpty()) {
            System.out.println("No audit logs found.");
            return;
        }

        System.out.println("\n" + "=".repeat(120));
        System.out.printf("%-8s | %-15s | %-25s | %-45s | %-20s%n",
                "Log ID", "Employee ID", "Action", "Details", "Timestamp");
        System.out.println("=".repeat(120));

        for (com.revworkforce.entity.AuditLog log : logs) {
            String details = log.getDetails();
            if (details != null && details.length() > 45) {
                details = details.substring(0, 42) + "...";
            }
            System.out.printf("%-8d | %-15s | %-25s | %-45s | %-20s%n",
                    log.getLogId(),
                    log.getEmployeeId() != null ? log.getEmployeeId() : "SYSTEM",
                    log.getAction(),
                    details != null ? details : "",
                    log.getTimestamp());
        }
        System.out.println("=".repeat(120));
    }
}
