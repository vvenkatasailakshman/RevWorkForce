package com.revworkforce.service;

import com.revworkforce.dao.NotificationDAO;
import com.revworkforce.dao.impl.NotificationDAOImpl;
import com.revworkforce.entity.Notification;

import java.util.List;

public class NotificationService {
    private NotificationDAO notificationDAO;
    private com.revworkforce.dao.EmployeeDAO employeeDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAOImpl();
        this.employeeDAO = new com.revworkforce.dao.impl.EmployeeDAOImpl();
    }

    public List<Notification> getNotificationsByEmployee(String employeeId) {
        return notificationDAO.getNotificationsByEmployee(employeeId);
    }

    public boolean markAsRead(int notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }

    public boolean createNotification(Notification notification) {
        return notificationDAO.createNotification(notification);
    }

    public int getUnreadCount(String employeeId) {
        return notificationDAO.getUnreadCount(employeeId);
    }

    public void checkAndGenerateReminders(com.revworkforce.entity.Employee loggedInUser) {
        // 1. Birthdays
        List<com.revworkforce.entity.Employee> bdays = employeeDAO.getUpcomingBirthdays();
        for (com.revworkforce.entity.Employee e : bdays) {
            // Check if it's EXACTLY today (the query might return upcoming 30 days)
            if (isToday(e.getDob())) {
                generateEventNotifications(e, "Birthday");
            }
        }

        // 2. Anniversaries
        List<com.revworkforce.entity.Employee> anns = employeeDAO.getUpcomingWorkAnniversaries();
        for (com.revworkforce.entity.Employee e : anns) {
            if (isToday(e.getJoiningDate())) {
                generateEventNotifications(e, "Work Anniversary");
            }
        }
    }

    private boolean isToday(java.sql.Date date) {
        if (date == null)
            return false;
        java.util.Calendar today = java.util.Calendar.getInstance();
        java.util.Calendar target = java.util.Calendar.getInstance();
        target.setTime(date);
        return today.get(java.util.Calendar.MONTH) == target.get(java.util.Calendar.MONTH) &&
                today.get(java.util.Calendar.DAY_OF_MONTH) == target.get(java.util.Calendar.DAY_OF_MONTH);
    }

    private void generateEventNotifications(com.revworkforce.entity.Employee e, String type) {
        String detailHeader = String.format("%s - %s (%s)", e.getEmployeeId(), e.getName(), e.getEmail());
        String message = type + " Reminder: " + detailHeader + " has a " + type + " today!";

        // Notify Employee
        createIfUnique(e.getEmployeeId(), "Happy " + type + "!");

        // Notify Manager
        if (e.getManagerId() != null) {
            createIfUnique(e.getManagerId(), message);
        }

        // Notify Admins
        List<com.revworkforce.entity.Employee> admins = employeeDAO.searchEmployees("ADMIN");
        for (com.revworkforce.entity.Employee admin : admins) {
            if (admin.getRole().equals("ADMIN")) {
                createIfUnique(admin.getEmployeeId(), message);
            }
        }
    }

    private void createIfUnique(String empId, String message) {
        if (!notificationDAO.existsToday(empId, message)) {
            Notification n = new Notification();
            n.setEmployeeId(empId);
            n.setMessage(message);
            notificationDAO.createNotification(n);
        }
    }
}
