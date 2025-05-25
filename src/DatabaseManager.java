import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// If you want to use .env file, see the bottom for a simple loader example.
public class DatabaseManager {
    // Use these variables for DB connection.
    // If you want to use a .env file, see below.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/taskmanager";
    private static final String DB_USER = "root"; // Change as needed
    private static final String DB_PASS = "YOUR_PWD";     // Change as needed

    private Connection conn;

    public DatabaseManager() throws SQLException {
        try {
            // Explicitly load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Ensure the connector JAR is in your classpath.", e);
        }
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public void addTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, due_date, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, task.getDueDate() != null ? Date.valueOf(task.getDueDate()) : null);
            stmt.setString(4, task.getStatus());
            stmt.executeUpdate();
        }
    }

    public List<Task> getTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY due_date";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task t = new Task(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null,
                    rs.getString("status")
                );
                tasks.add(t);
            }
        }
        return tasks;
    }

    public void updateTask(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title=?, description=?, due_date=?, status=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, task.getDueDate() != null ? Date.valueOf(task.getDueDate()) : null);
            stmt.setString(4, task.getStatus());
            stmt.setInt(5, task.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (conn != null) conn.close();
    }
}

/* --- If you want to use a .env file for credentials, add this simple loader class: --- */
/*
import java.io.*;
import java.util.*;

class Env {
    private static final Properties props = new Properties();
    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
                String[] parts = line.split("=", 2);
                if (parts.length == 2)
                    props.setProperty(parts[0].trim(), parts[1].trim());
            }
        } catch (IOException e) {
            System.err.println("Could not load .env file: " + e.getMessage());
        }
    }
    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
*/
// Then replace the DB_URL, DB_USER, and DB_PASS lines with:
/*
private static final String DB_URL = Env.get("DB_URL", "jdbc:mysql://localhost:3306/taskmanager");
private static final String DB_USER = Env.get("DB_USER", "root");
private static final String DB_PASS = Env.get("DB_PASS", "");
*/
