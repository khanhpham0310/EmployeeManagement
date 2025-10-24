DROP DATABASE IF EXISTS employeemanagementsystem;
CREATE DATABASE employeemanagementsystem;
USE employeemanagementsystem;

CREATE TABLE login (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'user'
);

CREATE TABLE employee (
    emID VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    fname VARCHAR(50) NOT NULL,
    dob DATE NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    address VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(50) NOT NULL,
    education VARCHAR(50) NOT NULL,
    designation VARCHAR(50) NOT NULL,
    cccd VARCHAR(20) NOT NULL UNIQUE
);

-- =====================
-- ATTENDANCE TABLE
-- =====================
CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    emID VARCHAR(10),
    date DATE NOT NULL,
    status ENUM('Present', 'Absent', 'Late') NOT NULL,
    FOREIGN KEY (emID) REFERENCES employee(emID) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (emID, date)
);

CREATE TABLE payroll (
    payroll_id INT AUTO_INCREMENT PRIMARY KEY,
    emID VARCHAR(10),
    month INT NOT NULL,
    year INT NOT NULL,
    base_salary DECIMAL(10,2) NOT NULL,
    late_days INT NOT NULL,
    deduction_percentage DECIMAL(5,2) NOT NULL,
    deduction_amount DECIMAL(10,2) NOT NULL,
    final_salary DECIMAL(10,2) NOT NULL,
    calculation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (emID) REFERENCES employee(emID) ON DELETE CASCADE,
    UNIQUE KEY unique_payroll (emID, month, year)
);

CREATE TABLE audit_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(50) NOT NULL,
    emID VARCHAR(10),
    username VARCHAR(50),
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (emID) REFERENCES employee(emID) ON DELETE SET NULL,
    FOREIGN KEY (username) REFERENCES login(username) ON DELETE SET NULL
);

INSERT INTO login (username, password, role)
VALUES ('admin', '123456', 'admin');

INSERT INTO employee (emID, name, fname, dob, salary, address, phone, email, education, designation, cccd)
VALUES 
('100001', 'John Doe', 'James Doe', '1990-01-01', 50000, '123 Main St', '0123456789', 'john.doe@example.com', 'Bachelor''s', 'Developer', '123456789012'),
('100002', 'Anna Smith', 'Robert Smith', '1992-07-14', 48000, '45 Oak Avenue', '0987654321', 'anna.smith@example.com', 'Master''s', 'HR Manager', '987654321098'),
('100003', 'Michael Johnson', 'Peter Johnson', '1988-03-22', 55000, '78 Pine Road', '0912345678', 'michael.johnson@example.com', 'Bachelor''s', 'System Analyst', '111222333444'),
('100004', 'Linda Brown', 'Thomas Brown', '1995-10-10', 46000, '9 Maple Street', '0938765432', 'linda.brown@example.com', 'Bachelor''s', 'Accountant', '555666777888');

INSERT INTO payroll (emID, month, year, base_salary, late_days, deduction_percentage, deduction_amount, final_salary)
VALUES 
('100001', 10, 2025, 50000, 2, 4.00, 2000, 48000),
('100002', 10, 2025, 48000, 0, 0.00, 0, 48000);

SELECT * FROM login;
SELECT * FROM employee;
SELECT * FROM payroll;