package com.revworkforce.service;

import com.revworkforce.dao.AnnouncementDAO;
import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.dao.HolidayDAO;
import com.revworkforce.dao.impl.AnnouncementDAOImpl;
import com.revworkforce.dao.impl.EmployeeDAOImpl;
import com.revworkforce.dao.impl.HolidayDAOImpl;
import com.revworkforce.entity.Announcement;
import com.revworkforce.entity.Employee;
import com.revworkforce.entity.Holiday;

import java.util.List;

public class AdminService {
    private EmployeeDAO employeeDAO;
    private HolidayDAO holidayDAO;
    private AnnouncementDAO announcementDAO;
    private NotificationService notificationService;

    public AdminService() {
        this.employeeDAO = new EmployeeDAOImpl();
        this.holidayDAO = new HolidayDAOImpl();
        this.announcementDAO = new AnnouncementDAOImpl();
        this.notificationService = new NotificationService();
    }

    public boolean addNewEmployee(Employee employee) {
        // Hash the password before saving
        if (employee.getPassword() != null && !EmployeeService.isHashed(employee.getPassword())) {
            employee.setPassword(EmployeeService.hashPassword(employee.getPassword()));
        }
        return employeeDAO.addEmployee(employee) != null;
    }

    public boolean updateEmployeeDetails(Employee employee) {
        return employeeDAO.updateEmployee(employee);
    }

    public List<Employee> viewAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    public boolean addHoliday(Holiday holiday) {
        return holidayDAO.addHoliday(holiday);
    }

    public List<Holiday> viewAllHolidays() {
        return holidayDAO.getAllHolidays();
    }

    public boolean postAnnouncement(Announcement announcement) {
        boolean posted = announcementDAO.createAnnouncement(announcement);
        if (posted) {
            // Notify all employees
            List<Employee> employees = employeeDAO.getAllEmployees();
            for (Employee emp : employees) {
                if (!emp.getEmployeeId().equals(announcement.getPostedBy())) { // Don't notify the one who posted
                    com.revworkforce.entity.Notification notification = new com.revworkforce.entity.Notification();
                    notification.setEmployeeId(emp.getEmployeeId());
                    notification.setMessage("New Announcement: " + announcement.getTitle());
                    notification.setRead(false);
                    notification.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                    notificationService.createNotification(notification);
                }
            }
        }
        return posted;
    }

    public List<Announcement> viewAllAnnouncements() {
        return announcementDAO.getAllAnnouncements();
    }
}
