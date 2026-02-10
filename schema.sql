-- Database Schema for RevWorkForce
-- Database Schema for RevWorkforce
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS revworkforce;
USE revworkforce;

-- 1. Employees Table
-- We removed DROP TABLE statements to prevent accidental data loss.
-- Creation is handled via CREATE TABLE IF NOT EXISTS.

CREATE TABLE IF NOT EXISTS employees (
    employe,e_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- In a real app, store hashed passwords
    phone_number VARCHAR(20),
    address TEXT,
    department VARCHAR(50),
    designation VARCHAR(50),
    role ENUM('EMPLOYEE', 'MANAGER', 'ADMIN') NOT NULL,
    manager_id VARCHAR(20),
    joining_date DATE,
    dob DATE,
    emergency_contact VARCHAR(100),
    salary DECIMAL(10,2),
    active BOOLEAN DEFAULT TRUE,
    security_question VARCHAR(255),
    security_answer VARCHAR(255),
    FOREIGN KEY (manager_id) REFERENCES employees(employee_id)
);

-- 2. Leaves Table
CREATE TABLE IF NOT EXISTS leaves (
    leave_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id VARCHAR(20) NOT NULL,
    leave_type ENUM('CASUAL', 'SICK', 'PAID', 'PRIVILEGE') NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'REVOKED') DEFAULT 'PENDING',
    manager_comment TEXT,
    applied_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- 3. Leave Balances Table
CREATE TABLE IF NOT EXISTS leave_balances (
    employee_id VARCHAR(20) PRIMARY KEY,
    casual_leave INT DEFAULT 12,
    sick_leave INT DEFAULT 12,
    paid_leave INT DEFAULT 15,
    privilege_leave INT DEFAULT 0,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- 4. Performance Reviews Table
CREATE TABLE IF NOT EXISTS performance_reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id VARCHAR(20) NOT NULL,
    review_period VARCHAR(50) NOT NULL, -- e.g., "2023-Q1" or "2023-Annual"
    achievements TEXT,
    areas_of_improvement TEXT,
    self_rating INT CHECK (self_rating BETWEEN 1 AND 5),
    manager_feedback TEXT,
    manager_rating INT CHECK (manager_rating BETWEEN 1 AND 5),
    status ENUM('DRAFT', 'SUBMITTED', 'REVIEWED') DEFAULT 'DRAFT',
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- 5. Goals Table
CREATE TABLE IF NOT EXISTS goals (
    goal_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id VARCHAR(20) NOT NULL,
    description TEXT NOT NULL,
    deadline DATE,
    priority ENUM('HIGH', 'MEDIUM', 'LOW') DEFAULT 'MEDIUM',
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING',
    success_metrics TEXT,
    manager_comment TEXT,
    reviewed_by VARCHAR(20),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- 6. Holidays Table
CREATE TABLE IF NOT EXISTS holidays (
    holiday_id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    name VARCHAR(100) NOT NULL
);

-- 7. Announcements Table
CREATE TABLE IF NOT EXISTS announcements (
    announcement_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    posted_by VARCHAR(20),
    posted_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (posted_by) REFERENCES employees(employee_id)
);

-- 8. Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- 9. Audit Logs Table
CREATE TABLE IF NOT EXISTS audit_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id VARCHAR(20),
    action VARCHAR(100) NOT NULL,
    details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Initial Admin User (Password: admin123)
-- Using INSERT IGNORE to prevent duplicate key errors on subsequent runs
INSERT IGNORE INTO employees (employee_id, name, email, password, role, joining_date) 
VALUES ('ADM101', 'System Admin', 'admin@revworkforce.com', 'admin123', 'ADMIN', CURDATE());
