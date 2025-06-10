CREATE DATABASE IF NOT EXISTS taskmanager;
USE taskmanager;

CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    status VARCHAR(50) DEFAULT 'Pending',
    priority VARCHAR(10) DEFAULT 'Medium',
    category_id INT DEFAULT NULL,
    recurrence VARCHAR(20) DEFAULT 'None',
    notes TEXT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);