# 💼 RevWorkForce – HR Management System  

RevWorkForce is a console-based Human Resource Management (HRM) application developed using **Java and MySQL**.  
It helps organizations manage employees, departments, leave requests, attendance, and performance reviews using a **secure role-based access system**.

---

## 📌 Project Overview  
RevWorkForce streamlines HR operations through structured workflows and role-based permissions.

👥 **Supported Roles**  
- 👨‍💼 Employee  
- 🧑‍💻 Manager  
- 🛡️ Admin  

Each role has specific permissions to ensure **security and proper workflow**.

---

## ✨ Features  

🔐 **Authentication & Roles**
- Employee registration and login  
- Role-based access control (Employee, Manager, Admin)  

🏢 **Employee & Department Management**
- Department-wise employee management  
- Admin controls employee records  

📅 **Leave Management**
- Apply for leave  
- Leave approval/rejection workflow  
- Track leave status  

⭐ **Performance Management**
- Performance review system  
- Employee rating by manager  

⏱️ **Attendance**
- Attendance tracking support  

🧾 **System Utilities**
- Logging using Log4j / Java Logger  
- Exception handling  
- Structured layered architecture  

---

## 🛠️ Technologies Used  

- ☕ Java 17 (OpenJDK)  
- 🔗 JDBC  
- 🛢️ MySQL  
- 📜 Log4j / Java Logger  
- 🌐 Git & GitHub  

---

## 🏗️ System Architecture  

The project follows a **layered architecture** for better maintainability and scalability.

### 🔹 Architecture Layers  

📦 **Model Layer**
- Contains entity classes (Employee, Department, Leave, etc.)

🗄️ **DAO Layer**
- Handles database operations using JDBC  
- CRUD operations  

⚙️ **Service Layer**
- Business logic implementation  
- Role-based validations  

🧰 **Utility Layer**
- Database connection  
- Logger setup  
- Helper functions  

🚀 **Main Layer**
- Console UI  
- Menu-driven interaction  

### 📂 Project Structure
com.revworkforce
├── model 📦 Entity classes
├── dao 🗄️ Database operations
├── service ⚙️ Business logic
├── util 🧰 Utilities & DB connection
└── main 🚀 Main console app

---

## 🗃️ Database Design (ER Diagram)

The system uses a relational database with proper primary and foreign key relationships.

### 📊 Entities  

👤 **Employee**
- emp_id (PK)  
- name  
- email  
- role  
- dept_id (FK)  

🏢 **Department**
- dept_id (PK)  
- dept_name  

📝 **Leave_Request**
- leave_id (PK)  
- emp_id (FK)  
- leave_type  
- status  

⭐ **Performance_Review**
- review_id (PK)  
- emp_id (FK)  
- rating  
- comments  

⏱️ **Attendance**
- attendance_id (PK)  
- emp_id (FK)  
- date  
- status  

### 🔗 Relationships  
- One Department → Many Employees  
- One Employee → Many Leave Requests  
- One Employee → Many Reviews  
- One Employee → Attendance Records  

Ensures **data integrity using primary & foreign keys**.

---

## 📑 SRS (Software Requirements Specification)

### 🎯 Functional Requirements  
✔ Employee registration & login  
✔ Role-based access control  
✔ Manage employees & departments  
✔ Apply & approve leave  
✔ Performance review system  
✔ Attendance tracking  
✔ Console-based navigation  

### ⚡ Non-Functional Requirements  
🔒 Security – Role-based access  
⚡ Performance – Fast JDBC operations  
🧩 Maintainability – Layered architecture  
📈 Scalability – Can extend to web app  
🛠️ Reliability – Exception handling & logging  

---

## ▶️ How to Run the Project  

1️⃣ Install Java 17 and MySQL  
2️⃣ Clone repository from GitHub  
3️⃣ Create database in MySQL  
4️⃣ Update DB credentials in connection file  
5️⃣ Run main class  
6️⃣ Use console menu to interact  

---

## 👥 User Roles & Permissions  

### 👨‍💼 Employee  
- Apply for leave  
- View leave status  
- View personal details  

### 🧑‍💻 Manager  
- Approve/reject leave  
- Give performance reviews  

### 🛡️ Admin  
- Manage employees  
- Manage departments  
- Full system access  

---

## 🚀 Future Enhancements  

🌐 Web-based interface (Spring Boot + React)  
🔐 JWT Authentication  
📧 Email notifications  
📊 Reporting dashboard  
☁️ Cloud database integration  

---

## 👨‍💻 Author  
**Venkata Sai Lakshman Viswanadhapalli**

---

## 📜 License  
This project is created for **learning and academic purposes**.
