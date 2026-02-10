package com.revworkforce.dao.impl;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.entity.Employee;
import com.revworkforce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public Employee getEmployeeByEmail(String email) {
        String query = "SELECT * FROM employees WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return null; // Not found
    }

    @Override
    public Employee getEmployeeById(String id) {
        String query = "SELECT * FROM employees WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return null;
    }

    @Override
    public Employee getEmployeeByIdOrEmail(String identifier) {
        String query = "SELECT * FROM employees WHERE employee_id = ? OR email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, identifier);
            pstmt.setString(2, identifier);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return employees;
    }

    @Override
    public String addEmployee(Employee employee) {
        String newId = generateId(employee.getRole());
        String query = "INSERT INTO employees (employee_id, name, email, password, phone_number, address, department, designation, role, manager_id, joining_date, dob, emergency_contact, salary, active, security_question, security_answer) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newId);
            pstmt.setString(2, employee.getName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getPassword());
            pstmt.setString(5, employee.getPhoneNumber());
            pstmt.setString(6, employee.getAddress());
            pstmt.setString(7, employee.getDepartment());
            pstmt.setString(8, employee.getDesignation());
            pstmt.setString(9, employee.getRole());
            if (employee.getManagerId() != null && !employee.getManagerId().isEmpty()) {
                pstmt.setString(10, employee.getManagerId());
            } else {
                pstmt.setNull(10, Types.VARCHAR);
            }
            pstmt.setDate(11, employee.getJoiningDate());
            pstmt.setDate(12, employee.getDob());
            pstmt.setString(13, employee.getEmergencyContact());
            pstmt.setDouble(14, employee.getSalary());
            pstmt.setBoolean(15, employee.isActive());
            pstmt.setString(16, employee.getSecurityQuestion());
            pstmt.setString(17, employee.getSecurityAnswer());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return newId;
            }
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        String query = "UPDATE employees SET name=?, phone_number=?, address=?, department=?, designation=?, manager_id=?, dob=?, emergency_contact=?, salary=?, active=? WHERE employee_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPhoneNumber());
            pstmt.setString(3, employee.getAddress());
            pstmt.setString(4, employee.getDepartment());
            pstmt.setString(5, employee.getDesignation());
            if (employee.getManagerId() != null && !employee.getManagerId().isEmpty()) {
                pstmt.setString(6, employee.getManagerId());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
            }
            pstmt.setDate(7, employee.getDob());
            pstmt.setString(8, employee.getEmergencyContact());
            pstmt.setDouble(9, employee.getSalary());
            pstmt.setBoolean(10, employee.isActive());
            pstmt.setString(11, employee.getEmployeeId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deactivateEmployee(String employeeId) {
        String query = "UPDATE employees SET active = FALSE WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // System.err.println("Error deactivating employee: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean changePassword(String employeeId, String newPassword) {
        String query = "UPDATE employees SET password=? WHERE employee_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, employeeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // System.err.println("Error: Database operation failed. " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Employee> getEmployeesByManager(String managerId) {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees WHERE manager_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, managerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private String generateId(String role) {
        String prefix = "EMP";
        if ("ADMIN".equalsIgnoreCase(role)) {
            prefix = "ADM";
        } else if ("MANAGER".equalsIgnoreCase(role)) {
            prefix = "MGR";
        }

        int maxId = 100; // Start looking from 100 so next is 101

        String query = "SELECT employee_id FROM employees WHERE role = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String currentId = rs.getString("employee_id");
                if (currentId != null && currentId.startsWith(prefix)) {
                    try {
                        int numPart = Integer.parseInt(currentId.substring(3));
                        if (numPart > maxId) {
                            maxId = numPart;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore malformed IDs
                    }
                }
            }
        } catch (SQLException e) {
            // System.err.println("Error generating ID: " + e.getMessage());
        }
        return prefix + (maxId + 1);
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee(
                rs.getString("employee_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getString("department"),
                rs.getString("designation"),
                rs.getString("role"),
                rs.getString("manager_id"),
                rs.getDate("joining_date"),
                rs.getDate("dob"),
                rs.getString("emergency_contact"),
                rs.getDouble("salary"),
                rs.getBoolean("active"));
        emp.setSecurityQuestion(rs.getString("security_question"));
        emp.setSecurityAnswer(rs.getString("security_answer"));
        return emp;
    }

    @Override
    public List<Employee> getUpcomingBirthdays() {
        List<Employee> employees = new ArrayList<>();
        // Fetching all and filtering in Java to be DB-agnostic and handle year wrapping
        // easily
        String query = "SELECT * FROM employees";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Employee emp = mapResultSetToEmployee(rs);
                if (emp.getDob() != null && isUpcoming(emp.getDob())) {
                    employees.add(emp);
                }
            }
        } catch (SQLException e) {
            // System.err.println("Error fetching birthdays: " + e.getMessage());
        }
        return employees;
    }

    @Override
    public List<Employee> getUpcomingWorkAnniversaries() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Employee emp = mapResultSetToEmployee(rs);
                if (emp.getJoiningDate() != null && isUpcoming(emp.getJoiningDate())) {
                    employees.add(emp);
                }
            }
        } catch (SQLException e) {
            // System.err.println("Error fetching anniversaries: " + e.getMessage());
        }
        return employees;
    }

    private boolean isUpcoming(Date date) {
        java.time.LocalDate target = date.toLocalDate();
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate nextMonth = today.plusDays(30);

        // Adjust target year to current year to compare month/day
        java.time.LocalDate currentYearTarget = target.withYear(today.getYear());

        // If it's already passed this year, check next year (e.g. Dec 30 vs Jan 1)
        if (currentYearTarget.isBefore(today) && !currentYearTarget.equals(today)) {
            currentYearTarget = currentYearTarget.plusYears(1);
        }

        return !currentYearTarget.isBefore(today) && !currentYearTarget.isAfter(nextMonth);
    }

    @Override
    public List<Employee> searchEmployees(String queryStr) { // Renamed param to avoid conflict
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE lower(name) LIKE ? OR lower(department) LIKE ? OR lower(designation) LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + queryStr.toLowerCase() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            // System.err.println("Error searching employees: " + e.getMessage());
        }
        return employees;
    }
}
