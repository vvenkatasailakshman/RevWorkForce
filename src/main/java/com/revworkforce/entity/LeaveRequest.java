package com.revworkforce.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class LeaveRequest {
    private int leaveId;
    private String employeeId;
    private String leaveType; // CASUAL, SICK, PAID, PRIVILEGE
    private Date startDate;
    private Date endDate;
    private String reason;
    private String status; // PENDING, APPROVED, REJECTED
    private String managerComment;
    private Timestamp appliedOn;

    public LeaveRequest() {
    }

    public LeaveRequest(int leaveId, String employeeId, String leaveType, Date startDate, Date endDate, String reason,
            String status, String managerComment, Timestamp appliedOn) {
        this.leaveId = leaveId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.managerComment = managerComment;
        this.appliedOn = appliedOn;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public Timestamp getAppliedOn() {
        return appliedOn;
    }

    public void setAppliedOn(Timestamp appliedOn) {
        this.appliedOn = appliedOn;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "leaveId=" + leaveId +
                ", employeeId='" + employeeId + '\'' +
                ", leaveType='" + leaveType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
