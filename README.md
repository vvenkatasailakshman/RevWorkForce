# RevWorkForce – HR Management System

RevWorkForce is a console-based Human Resource Management (HRM) application developed using Java and MySQL. 
The system helps organizations manage employees, departments, leave requests, and performance reviews 
through a role-based access model.

---

## Project Overview

The application supports three main roles:
- Employee
- Manager
- Admin

Each role has specific permissions to ensure secure and structured workflow within the system.

---

## Features

- Employee registration and login
- Role-based access control (Employee, Manager, Admin)
- Department-wise employee management
- Leave application and approval workflow
- Performance review and rating system
- Attendance tracking support
- Logging and exception handling

---

## Technologies Used

- Java 17 (OpenJDK)
- JDBC
- MySQL
- Log4j / Java Logger
- Git and GitHub

---

## Project Structure

com.revworkforce  
├── model  
├── dao  
├── service  
├── util  
└── main  

---

## Database Design

The project uses a relational database designed using an ER diagram with the following entities:
- Employee
- Department
- Leave_Request
- Performance_Review
- Attendance

Relationships are maintained using primary and foreign keys to ensure data integrity.

---

## How to Run the Project

1. Install Java 17 and MySQL
2. Clone the project repository
3. Create the required database in MySQL
4. Update database credentials in DB connection utility
5. Run the main application class
6. Interact with the system using console menus

---

## User Roles

Employee:
- Apply for leave
- View leave status
- View personal details

Manager:
- Approve or reject leave requests
- Perform employee performance reviews

Admin:
- Manage employees and departments
- Full system access

---

## Future Enhancements

- Web-based interface
- Spring Boot integration
- Authentication using JWT
- Email notifications
- Advanced reporting dashboard

---

## Author

Venkata Sai Lakshman Viswanadhapalli

---

## License

This project is created for learning and academic purposes.
