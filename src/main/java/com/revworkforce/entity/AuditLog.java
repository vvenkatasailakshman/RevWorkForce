package com.revworkforce.entity;

import java.sql.Timestamp;

public class AuditLog {
    private int logId;
    private String employeeId;
    private String action;
    private String details;
    private Timestamp timestamp;

    public AuditLog() {
    }

    public AuditLog(int logId, String employeeId, String action, String details, Timestamp timestamp) {
        this.logId = logId;
        this.employeeId = employeeId;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "logId=" + logId +
                ", employeeId='" + employeeId + '\'' +
                ", action='" + action + '\'' +
                ", details='" + details + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
