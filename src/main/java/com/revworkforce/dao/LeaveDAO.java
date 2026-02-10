package com.revworkforce.dao;

import java.util.List;

import com.revworkforce.entity.LeaveRequest;

public interface LeaveDAO {
    boolean applyForLeave(LeaveRequest leaveRequest);

    List<LeaveRequest> getMyLeaves(String employeeId);

    List<LeaveRequest> getPendingLeavesForManager(String managerId);

    boolean approveLeave(int leaveId, String managerId, String comment);

    boolean rejectLeave(int leaveId, String managerId, String comment);

    int getLeaveBalance(String employeeId, String leaveType); // Returns remaining days

    // For initializing balance for new employees
    void initializeLeaveBalance(String employeeId);

    LeaveRequest getLeaveById(int leaveId);

    List<LeaveRequest> getAllLeaves(); // Admin

    boolean cancelLeave(int leaveId);

    List<LeaveRequest> getLeavesByDepartment(String department);
}
