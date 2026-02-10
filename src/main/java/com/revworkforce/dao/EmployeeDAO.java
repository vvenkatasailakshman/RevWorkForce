package com.revworkforce.dao;

import java.util.List;

import com.revworkforce.entity.Employee;

public interface EmployeeDAO {
    Employee getEmployeeByEmail(String email);

    Employee getEmployeeById(String id);

    Employee getEmployeeByIdOrEmail(String identifier);

    List<Employee> getAllEmployees();

    String addEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    boolean changePassword(String employeeId, String newPassword);

    List<Employee> getEmployeesByManager(String managerId);

    List<Employee> getUpcomingBirthdays();

    List<Employee> getUpcomingWorkAnniversaries();

    List<Employee> searchEmployees(String query);

    boolean deactivateEmployee(String employeeId);
}
