package com.revworkforce.service;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.dao.impl.EmployeeDAOImpl;
import com.revworkforce.dao.AnnouncementDAO;
import com.revworkforce.dao.HolidayDAO;
import com.revworkforce.dao.impl.AnnouncementDAOImpl;
import com.revworkforce.dao.impl.HolidayDAOImpl;
import com.revworkforce.entity.Announcement;
import com.revworkforce.entity.Employee;
import com.revworkforce.entity.Holiday;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public class EmployeeService {
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    private EmployeeDAO employeeDAO;
    private HolidayDAO holidayDAO;
    private AnnouncementDAO announcementDAO;

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAOImpl();
        this.holidayDAO = new HolidayDAOImpl();
        this.announcementDAO = new AnnouncementDAOImpl();
    }

    // ... existing methods

    public List<Holiday> getHolidays() {
        return holidayDAO.getAllHolidays();
    }

    public List<Announcement> getAnnouncements() {
        return announcementDAO.getAllAnnouncements();
    }

    public String register(Employee employee) {
        // Hash the password before saving
        if (employee.getPassword() != null && !isHashed(employee.getPassword())) {
            employee.setPassword(hashPassword(employee.getPassword()));
        }
        return employeeDAO.addEmployee(employee);
    }

    public Employee login(String identifier, String password) {
        Employee employee = employeeDAO.getEmployeeByIdOrEmail(identifier);
        if (employee != null && verifyPassword(password, employee.getPassword())) {
            return employee;
        }
        return null;
    }

    // Overloaded for email login if needed, though requirements said Employee ID
    public Employee loginByEmail(String email, String password) {
        Employee employee = employeeDAO.getEmployeeByEmail(email);
        if (employee != null && verifyPassword(password, employee.getPassword())) {
            return employee;
        }
        return null;
    }

    public Employee getEmployeeProfile(String employeeId) {
        return employeeDAO.getEmployeeById(employeeId);
    }

    public boolean updateProfile(Employee employee) {
        return employeeDAO.updateEmployee(employee);
    }

    public List<Employee> getUpcomingBirthdays() {
        return employeeDAO.getUpcomingBirthdays();
    }

    public List<Employee> getUpcomingWorkAnniversaries() {
        return employeeDAO.getUpcomingWorkAnniversaries();
    }

    public List<Employee> searchEmployees(String query) {
        return employeeDAO.searchEmployees(query);
    }

    public boolean deactivateEmployee(String employeeId) {
        return employeeDAO.deactivateEmployee(employeeId);
    }

    public boolean changePassword(String employeeId, String currentPassword, String newPassword) {
        Employee emp = employeeDAO.getEmployeeById(employeeId);
        if (emp != null && verifyPassword(currentPassword, emp.getPassword())) {
            String hashedNewPassword = hashPassword(newPassword);
            return employeeDAO.changePassword(employeeId, hashedNewPassword);
        }
        return false;
    }

    public Employee getManager(String managerId) {
        if (managerId == null || managerId.trim().isEmpty())
            return null;
        return employeeDAO.getEmployeeById(managerId);
    }

    public List<Employee> getDirectReports(String managerId) {
        return employeeDAO.getEmployeesByManager(managerId);
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeDAO.getEmployeeByEmail(email);
    }

    public boolean verifySecurityAnswer(Employee employee, String answer) {
        if (employee == null || employee.getSecurityAnswer() == null || answer == null) {
            return false;
        }
        return employee.getSecurityAnswer().trim().equalsIgnoreCase(answer.trim());
    }

    public boolean resetPassword(String employeeId, String newPassword) {
        String hashedNewPassword = hashPassword(newPassword);
        return employeeDAO.changePassword(employeeId, hashedNewPassword);
    }

    // --- Hashing Utility Methods ---

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private static String hashWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static String hashPassword(String password) {
        String salt = generateSalt();
        String hash = hashWithSalt(password, salt);
        return salt + ":" + hash;
    }

    public static boolean verifyPassword(String password, String storedHash) {
        if (storedHash == null || !storedHash.contains(":")) {
            return password.equals(storedHash);
        }
        String[] parts = storedHash.split(":", 2);
        if (parts.length != 2)
            return false;
        String salt = parts[0];
        String hash = parts[1];
        String newHash = hashWithSalt(password, salt);
        return hash.equals(newHash);
    }

    public static boolean isHashed(String storedPassword) {
        return storedPassword != null && storedPassword.contains(":");
    }
}
