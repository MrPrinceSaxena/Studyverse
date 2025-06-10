import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String DB_URL = EnvLoader.get("DB_URL", "jdbc:mysql://localhost:3306/taskmanager");
    private static final String DB_USER = EnvLoader.get("DB_USER", "root");
    private static final String DB_PASS = EnvLoader.get("DB_PASS", "");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // Category Utilities
    public static ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    public static int addCategory(String name) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            // Ignore duplicate entry error, return existing id if needed
        }
        return -1;
    }

    public static Integer getCategoryIdByName(String categoryName) {
        if (categoryName == null) return null;
        String sql = "SELECT id FROM categories WHERE name = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Task Utilities
    public static ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, c.name as category FROM tasks t LEFT JOIN categories c ON t.category_id = c.id ORDER BY t.due_date";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tasks.add(new Task(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null,
                    rs.getString("status"),
                    rs.getString("priority"),
                    rs.getString("category"),
                    rs.getString("recurrence"),
                    rs.getString("notes")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tasks;
    }

    public static void addTask(Task task) {
        Integer categoryId = getCategoryIdByName(task.getCategory());
        String sql = "INSERT INTO tasks (title, description, due_date, status, priority, category_id, recurrence, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setDate(3, task.getDueDate() != null ? Date.valueOf(task.getDueDate()) : null);
            ps.setString(4, task.getStatus());
            ps.setString(5, task.getPriority());
            if (categoryId == null) ps.setNull(6, java.sql.Types.INTEGER); else ps.setInt(6, categoryId);
            ps.setString(7, task.getRecurrence());
            ps.setString(8, task.getNotes());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void updateTask(Task task) {
        Integer categoryId = getCategoryIdByName(task.getCategory());
        String sql = "UPDATE tasks SET title=?, description=?, due_date=?, status=?, priority=?, category_id=?, recurrence=?, notes=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setDate(3, task.getDueDate() != null ? Date.valueOf(task.getDueDate()) : null);
            ps.setString(4, task.getStatus());
            ps.setString(5, task.getPriority());
            if (categoryId == null) ps.setNull(6, java.sql.Types.INTEGER); else ps.setInt(6, categoryId);
            ps.setString(7, task.getRecurrence());
            ps.setString(8, task.getNotes());
            ps.setInt(9, task.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}