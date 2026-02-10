package com.revworkforce.service;

import com.revworkforce.dao.LeaveDAO;
import com.revworkforce.dao.NotificationDAO;
import com.revworkforce.dao.impl.LeaveDAOImpl;
import com.revworkforce.dao.impl.NotificationDAOImpl;
import com.revworkforce.entity.LeaveRequest;
import com.revworkforce.entity.Notification;

import java.sql.Timestamp;
import java.util.List;

public class LeaveService {
    private LeaveDAO leaveDAO;
    private NotificationDAO notificationDAO;
    private com.revworkforce.dao.EmployeeDAO employeeDAO;

    public LeaveService() {
        this.leaveDAO = new LeaveDAOImpl();
        this.notificationDAO = new NotificationDAOImpl();
        this.employeeDAO = new com.revworkforce.dao.impl.EmployeeDAOImpl();
    }

    public boolean applyForLeave(LeaveRequest leaveRequest) {
        boolean success = leaveDAO.applyForLeave(leaveRequest);
        if (success) {
            // Fetch employee to get manager ID and name
            com.revworkforce.entity.Employee emp = employeeDAO.getEmployeeById(leaveRequest.getEmployeeId());
            if (emp != null) {
                String message = String.format("New Leave Request: %s (%s) applied for %s leave from %s to %s.",
                        emp.getName(), emp.getEmployeeId(), leaveRequest.getLeaveType(),
                        leaveRequest.getStartDate(), leaveRequest.getEndDate());

                // Notify Manager
                if (emp.getManagerId() != null && !emp.getManagerId().isEmpty()) {
                    createNotification(emp.getManagerId(), message);
                }

                // Notify Admins
                List<com.revworkforce.entity.Employee> admins = employeeDAO.searchEmployees("ADMIN");
                for (com.revworkforce.entity.Employee admin : admins) {
                    if ("ADMIN".equals(admin.getRole())) {
                        createNotification(admin.getEmployeeId(), message);
                    }
                }
            }
        }
        return success;
    }

    private void createNotification(String empId, String message) {
        Notification notification = new Notification();
        notification.setEmployeeId(empId);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notificationDAO.createNotification(notification);
    }

    public List<LeaveRequest> getMyLeaves(String employeeId) {
        return leaveDAO.getMyLeaves(employeeId);
    }

    public List<LeaveRequest> getPendingLeavesForManager(String managerId) {
        return leaveDAO.getPendingLeavesForManager(managerId);
    }

    public boolean approveLeave(int leaveId, String managerId, String comment) {
        boolean updated = leaveDAO.approveLeave(leaveId, managerId, comment);
        if (updated) {
            LeaveRequest leave = leaveDAO.getLeaveById(leaveId);
            if (leave != null) {
                Notification notification = new Notification();
                notification.setEmployeeId(leave.getEmployeeId());
                notification.setMessage("Your leave request for " + leave.getLeaveType() + " has been APPROVED.");
                notification.setRead(false);
                notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                notificationDAO.createNotification(notification);
            }
        }
        return updated;
    }

    public boolean rejectLeave(int leaveId, String managerId, String comment) {
        boolean updated = leaveDAO.rejectLeave(leaveId, managerId, comment);
        if (updated) {
            LeaveRequest leave = leaveDAO.getLeaveById(leaveId);
            if (leave != null) {
                Notification notification = new Notification();
                notification.setEmployeeId(leave.getEmployeeId());
                notification.setMessage("Your leave request for " + leave.getLeaveType() + " has been REJECTED.");
                notification.setRead(false);
                notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                notificationDAO.createNotification(notification);
            }
        }
        return updated;
    }

    public int getLeaveBalance(String employeeId, String leaveType) {
        return leaveDAO.getLeaveBalance(employeeId, leaveType);
    }

    public boolean cancelLeave(int leaveId) {
        return leaveDAO.cancelLeave(leaveId);
    }

    public List<LeaveRequest> getLeavesByDepartment(String department) {
        // Admin feature
        return leaveDAO.getLeavesByDepartment(department);
    }
}
