import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TaskManagerApp extends JFrame {
    private DatabaseManager dbManager;
    private TaskTableModel tableModel;
    private JTable table;

    public TaskManagerApp() {
        setTitle("Study Task Manager");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            dbManager = new DatabaseManager();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        initUI();
        loadTasks();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Study Planner - Task Manager", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(new Color(52, 73, 94));
        heading.setBorder(new EmptyBorder(20, 0, 20, 0));
        topPanel.add(heading, BorderLayout.CENTER);
        topPanel.setBackground(new Color(236, 240, 241));
        add(topPanel, BorderLayout.NORTH);

        tableModel = new TaskTableModel(List.of());
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(236, 240, 241));

        JButton addBtn = new JButton("Add Task");
        JButton editBtn = new JButton("Edit Task");
        JButton delBtn = new JButton("Delete Task");
        JButton refreshBtn = new JButton("Refresh");

        styleButton(addBtn, new Color(52, 152, 219));
        styleButton(editBtn, new Color(241, 196, 15));
        styleButton(delBtn, new Color(231, 76, 60));
        styleButton(refreshBtn, new Color(52, 73, 94));

        addBtn.addActionListener(e -> onAddTask());
        editBtn.addActionListener(e -> onEditTask());
        delBtn.addActionListener(e -> onDeleteTask());
        refreshBtn.addActionListener(e -> loadTasks());

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(delBtn);
        btnPanel.add(refreshBtn);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
    }

    private void loadTasks() {
        try {
            List<Task> tasks = dbManager.getTasks();
            tableModel.setTasks(tasks);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Cannot load tasks: " + e.getMessage());
        }
    }

    private void onAddTask() {
        TaskDialog dialog = new TaskDialog(this, "Add Task", null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            String title = dialog.getTaskTitle();
            String desc = dialog.getTaskDescription();
            LocalDate due = dialog.getTaskDueDate();
            String status = dialog.getTaskStatus();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task title is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                dbManager.addTask(new Task(title, desc, due, status));
                loadTasks();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to add task: " + e.getMessage());
            }
        }
    }

    private void onEditTask() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a task to edit.");
            return;
        }
        Task selected = tableModel.getTaskAt(row);
        TaskDialog dialog = new TaskDialog(this, "Edit Task", selected);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            String title = dialog.getTaskTitle();
            String desc = dialog.getTaskDescription();
            LocalDate due = dialog.getTaskDueDate();
            String status = dialog.getTaskStatus();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task title is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            selected.setTitle(title);
            selected.setDescription(desc);
            selected.setDueDate(due);
            selected.setStatus(status);
            try {
                dbManager.updateTask(selected);
                loadTasks();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to update task: " + e.getMessage());
            }
        }
    }

    private void onDeleteTask() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a task to delete.");
            return;
        }
        Task selected = tableModel.getTaskAt(row);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected task?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dbManager.deleteTask(selected.getId());
                loadTasks();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to delete task: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TaskManagerApp().setVisible(true);
        });
    }
}