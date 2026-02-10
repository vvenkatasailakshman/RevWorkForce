package com.revworkforce.service;

import com.revworkforce.dao.AuditLogDAO;
import com.revworkforce.dao.impl.AuditLogDAOImpl;
import com.revworkforce.entity.AuditLog;

import java.sql.Timestamp;
import java.util.List;

public class AuditLogService {
    private AuditLogDAO auditLogDAO;

    public AuditLogService() {
        this.auditLogDAO = new AuditLogDAOImpl();
    }

    public void logAction(String employeeId, String action, String details) {
        AuditLog log = new AuditLog();
        log.setEmployeeId(employeeId);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));
        auditLogDAO.createLog(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogDAO.getAllLogs();
    }

    public List<AuditLog> getLogsByEmployee(String employeeId) {
        return auditLogDAO.getLogsByEmployee(employeeId);
    }

    public List<AuditLog> getLogsByAction(String action) {
        return auditLogDAO.getLogsByAction(action);
    }
}
