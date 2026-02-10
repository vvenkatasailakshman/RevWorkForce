package com.revworkforce.ui;

import com.revworkforce.service.EmployeeService;
import com.revworkforce.entity.Employee;
import com.revworkforce.service.AuditLogService;

import java.util.Scanner;

public class LoginMenu {
    private EmployeeService employeeService;
    private AuditLogService auditLogService;
    private Scanner scanner;

    public LoginMenu() {
        this.employeeService = new EmployeeService();
        this.auditLogService = new AuditLogService();
        this.scanner = new Scanner(System.in);
    }

    public void display() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Login");
            System.out.println("2. Register (Admin, Manager, Employee)");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exit/Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegister();
                    break;
                case "3":
                    handleForgotPassword();
                    break;
                case "4":
                    System.out.println("Exiting application. Goodbye! (Or go back to Login)");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleLogin() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter Employee ID or Email: ");
        String idInput = scanner.nextLine();

        // Validate input
        String employeeId = idInput.trim();
        if (employeeId.isEmpty()) {
            System.out.println("Identifier cannot be empty.");
            return;
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        Employee employee = employeeService.login(employeeId, password);

        if (employee != null) {
            System.out.println("Login Successful! Welcome, " + employee.getName());
            auditLogService.logAction(employee.getEmployeeId(), "LOGIN", "User logged in successfully");
            redirectUser(employee);
        } else {
            System.out.println("Invalid credentials. Please try again.");
            auditLogService.logAction(employeeId, "LOGIN_FAILED", "Failed login attempt");
        }
    }

    private void handleRegister() {
        System.out.println("\n=== REGISTER ===");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        String email = "";
        while (true) {
            System.out.print("Enter Email (or 0 to cancel): ");
            email = scanner.nextLine().trim();

            if (email.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }

            if (email.endsWith("@gmail.com") || email.endsWith("@revature.com")) {
                break;
            } else {
                System.out.println("Invalid email! Email must end with @gmail.com or @revature.com");
            }
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.println("Select Role:");
        System.out.println("1. EMPLOYEE");
        System.out.println("2. MANAGER");
        System.out.println("3. ADMIN");
        System.out.print("Enter choice: ");
        String roleChoice = scanner.nextLine();
        String role = "EMPLOYEE"; // Default

        switch (roleChoice) {
            case "1":
                role = "EMPLOYEE";
                break;
            case "2":
                role = "MANAGER";
                break;
            case "3":
                role = "ADMIN";
                break;
            default:
                System.out.println("Invalid choice, defaulting to EMPLOYEE.");
        }

        // Security Question for Password Recovery
        System.out.println("\n--- Password Recovery Setup ---");
        System.out.println("Choose a security question:");
        System.out.println("1. What is your favorite city?");
        System.out.println("2. What is your favorite food?");
        System.out.println("3. What city were you born in?");
        System.out.println("4. What is your favorite game");
        System.out.print("Enter choice (1-4): ");
        String questionChoice = scanner.nextLine();

        String securityQuestion = "";
        switch (questionChoice) {
            case "1":
                securityQuestion = "What is your favorite city";
                break;
            case "2":
                securityQuestion = "What is your favorite food?";
                break;
            case "3":
                securityQuestion = "What city were you born in?";
                break;
            case "4":
                securityQuestion = "What is your favorite game?";
                break;
            default:
                securityQuestion = "What is your favorite movie?";
        }

        System.out.print("Your Answer: ");
        String securityAnswer = scanner.nextLine();

        Employee newEmployee = new Employee();
        newEmployee.setName(name);
        newEmployee.setEmail(email);
        newEmployee.setPassword(password);
        newEmployee.setRole(role);
        newEmployee.setSecurityQuestion(securityQuestion);
        newEmployee.setSecurityAnswer(securityAnswer);
        // Set other default fields if necessary (joining date usually handled by DB
        // default or current date)
        newEmployee.setJoiningDate(new java.sql.Date(System.currentTimeMillis()));

        String newEmployeeId = employeeService.register(newEmployee);
        if (newEmployeeId != null) {
            System.out.println("Registration Successful!");
            System.out.println("IMPORTANT: Your Employee ID is: " + newEmployeeId);
            System.out.println("Please use this ID to login.");
            auditLogService.logAction(newEmployeeId, "REGISTRATION",
                    "New user registered: " + name + " (" + role + ")");
        } else {
            System.out.println("Registration Failed. Email might be duplicate.");
        }
    }

    private void handleForgotPassword() {
        System.out.println("\n=== FORGOT PASSWORD ===");
        System.out.print("Enter your registered Email: ");
        String email = scanner.nextLine().trim();

        Employee employee = employeeService.getEmployeeByEmail(email);
        if (employee == null) {
            System.out.println("If this email exists in our system, recovery steps will be provided.");
            return;
        }

        System.out.println("Security Question: " + employee.getSecurityQuestion());
        System.out.print("Your Answer: ");
        String answer = scanner.nextLine().trim();

        if (employeeService.verifySecurityAnswer(employee, answer)) {
            System.out.print("Enter New Password: ");
            String newPassword = scanner.nextLine();
            System.out.print("Confirm New Password: ");
            String confirmPassword = scanner.nextLine();

            if (newPassword.equals(confirmPassword)) {
                if (employeeService.resetPassword(employee.getEmployeeId(), newPassword)) {
                    System.out.println("Password reset successful! You can now login with your new password.");
                    auditLogService.logAction(employee.getEmployeeId(), "PASSWORD_RESET",
                            "Password was reset via security question.");
                } else {
                    System.out.println("Error: Failed to reset password. Please try again later.");
                }
            } else {
                System.out.println("Error: Passwords do not match. Recovery cancelled.");
            }
        } else {
            System.out.println("Incorrect answer. Please contact system admin to reset password.");
        }
    }

    private void redirectUser(Employee employee) {
        String role = employee.getRole();
        switch (role) {
            case "ADMIN":
                new AdminMenu(employee).display();
                break;
            case "MANAGER":
                new ManagerMenu(employee).display();
                break;
            case "EMPLOYEE":
                new EmployeeMenu(employee).display();
                break;
            default:
                System.out.println("Unknown role: " + role);
        }
    }
}
