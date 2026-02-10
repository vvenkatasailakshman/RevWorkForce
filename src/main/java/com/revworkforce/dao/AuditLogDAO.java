package com.revworkforce.dao;

import java.util.List;

import com.revworkforce.entity.AuditLog;

public interface AuditLogDAO {
    boolean createLog(AuditLog auditLog);

    List<AuditLog> getAllLogs();

    List<AuditLog> getLogsByEmployee(String employeeId);

    List<AuditLog> getLogsByAction(String action);
}
