# StudyVerse - Java Swing Task Manager

A GUI-based task manager application built with Java Swing and MySQL. Easily add, edit, and manage your tasks with a modern interface and persistent storage.

---

## Features

- **Add, edit, delete tasks** (with title, description, due date, status)
- **Persistent storage** using MySQL database
- **Modern GUI** with Java Swing
- **Customizable table** with sorting
- **Responsive layout** and visual appeal

---

## Project Structure

```
StudyVerse/
  ├── src/
  │   ├── TaskManagerApp.java
  │   ├── DatabaseManager.java
  │   ├── Task.java
  │   ├── TaskTableModel.java
  │   └── TaskDialog.java
  ├── lib/
  │   └── mysql-connector-j-9.3.0.jar
  ├── .env         # (optional, for DB credentials)
  └── README.md
```

---

## Database Schema

Create the following table in your MySQL database:

```sql
CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    status VARCHAR(50) DEFAULT 'Pending'
);
```

---

## Setup Instructions

1. **Install JDK** (Java 8 or above) and MySQL.
2. **Clone or download** this project.
3. **Set up the database:**
   - Create a database (e.g., `taskmanager`).
   - Run the SQL above to create the `tasks` table.
4. **Configure database credentials:**
   - Either edit `DatabaseManager.java` to hardcode your DB credentials,
   - Or create a `.env` file in the project root with:
     ```
     DB_URL=jdbc:mysql://localhost:3306/taskmanager
     DB_USER=your_mysql_username
     DB_PASS=your_mysql_password
     ```
5. **Place MySQL JDBC Driver JAR** in the `lib` folder (e.g., `mysql-connector-j-9.3.0.jar`).

---

## Build & Run

From the project root, run:

```sh
javac -cp "lib/mysql-connector-j-9.3.0.jar" src/*.java
java -cp "lib/mysql-connector-j-9.3.0.jar:src" TaskManagerApp
```

> On Windows, use `;` instead of `:` in the classpath.

---

## Screenshots

> *Insert screenshots of your application GUI here!*

---

## Marking Rubric Checklist

- [x] JDK & IDE setup
- [x] Project Structure Defined
- [x] MySQL Schema & Table Created
- [x] JDBC Connectivity
- [x] Model & DAO Classes
- [x] Visual Appeal and UI Aesthetics
- [x] Component Placement & Alignment
- [x] Responsiveness & Accessibility

---

## Credits

- Java Swing for GUI
- MySQL for database
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) for JDBC driver

---

## License

This project is for educational purposes only.
