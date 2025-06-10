import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerApp extends JFrame {
    private TaskTableModel tableModel;
    private JTable taskTable;
    private List<Category> categories;
    private JComboBox<String> filterStatusBox;
    private JComboBox<String> filterPriorityBox;
    private JComboBox<String> filterCategoryBox;

    public TaskManagerApp() {
        setTitle("StudyVerse - Task Manager");
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        reloadCategories();
        List<Task> tasks = DatabaseManager.getTasks();
        tableModel = new TaskTableModel(tasks);

        taskTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Status:"));
        filterStatusBox = new JComboBox<>(new String[]{"All", "Pending", "Completed"});
        filterPanel.add(filterStatusBox);

        filterPanel.add(new JLabel("Priority:"));
        filterPriorityBox = new JComboBox<>(new String[]{"All", "High", "Medium", "Low"});
        filterPanel.add(filterPriorityBox);

        filterPanel.add(new JLabel("Category:"));
        ArrayList<String> catNames = new ArrayList<>();
        catNames.add("All");
        for (Category c : categories) catNames.add(c.getName());
        filterCategoryBox = new JComboBox<>(catNames.toArray(new String[0]));
        filterPanel.add(filterCategoryBox);

        JButton btnFilter = new JButton("Filter");
        btnFilter.addActionListener(e -> applyFilters());
        filterPanel.add(btnFilter);

        JButton btnAdd = new JButton("Add Task");
        JButton btnEdit = new JButton("Edit Task");
        JButton btnDelete = new JButton("Delete Task");
        JButton btnMark = new JButton("Toggle Complete");
        JButton btnAddCat = new JButton("Add Category");

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDelete); btnPanel.add(btnMark); btnPanel.add(btnAddCat);

        add(filterPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> onAddTask());
        btnEdit.addActionListener(e -> onEditTask());
        btnDelete.addActionListener(e -> onDeleteTask());
        btnMark.addActionListener(e -> onToggleStatus());
        btnAddCat.addActionListener(e -> onAddCategory());

        // Overdue highlighting
        taskTable.setDefaultRenderer(Object.class, new TaskTableCellRenderer(tableModel));

        setVisible(true);
    }

    private void reloadCategories() {
        categories = DatabaseManager.getCategories();
        if (filterCategoryBox != null) {
            filterCategoryBox.removeAllItems();
            filterCategoryBox.addItem("All");
            for (Category c : categories) filterCategoryBox.addItem(c.getName());
        }
    }

    private void refreshTasks() {
        tableModel.setTasks(DatabaseManager.getTasks());
    }

    private void onAddTask() {
        TaskDialog dialog = new TaskDialog(this, categories, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Task t = dialog.getTask();
            // If category is new, add it
            if (t.getCategory() != null && DatabaseManager.getCategoryIdByName(t.getCategory()) == null) {
                DatabaseManager.addCategory(t.getCategory());
                reloadCategories();
            }
            DatabaseManager.addTask(t);
            refreshTasks();
        }
    }

    private void onEditTask() {
        int row = taskTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a task to edit.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Task t = tableModel.getTaskAt(row);
        TaskDialog dialog = new TaskDialog(this, categories, t);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Task newTask = dialog.getTask();
            newTask.setId(t.getId());
            // If category is new, add it
            if (newTask.getCategory() != null && DatabaseManager.getCategoryIdByName(newTask.getCategory()) == null) {
                DatabaseManager.addCategory(newTask.getCategory());
                reloadCategories();
            }
            DatabaseManager.updateTask(newTask);
            refreshTasks();
        }
    }

    private void onDeleteTask() {
        int row = taskTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a task to delete.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Task t = tableModel.getTaskAt(row);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected task?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseManager.deleteTask(t.getId());
            refreshTasks();
        }
    }

    private void onToggleStatus() {
        int row = taskTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a task to toggle status.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Task t = tableModel.getTaskAt(row);
        t.setStatus(t.getStatus().equals("Completed") ? "Pending" : "Completed");
        DatabaseManager.updateTask(t);
        refreshTasks();
    }

    private void onAddCategory() {
        String name = JOptionPane.showInputDialog(this, "Category name:");
        if (name != null && !name.trim().isEmpty()) {
            DatabaseManager.addCategory(name.trim());
            reloadCategories();
        }
    }

    private void applyFilters() {
        String status = filterStatusBox.getSelectedItem().toString();
        String priority = filterPriorityBox.getSelectedItem().toString();
        String category = filterCategoryBox.getSelectedItem().toString();
        List<Task> filtered = new ArrayList<>();
        for (Task t : DatabaseManager.getTasks()) {
            boolean ok = true;
            if (!status.equals("All") && !t.getStatus().equals(status)) ok = false;
            if (!priority.equals("All") && !t.getPriority().equals(priority)) ok = false;
            if (!category.equals("All") && (t.getCategory() == null || !t.getCategory().equals(category))) ok = false;
            if (ok) filtered.add(t);
        }
        tableModel.setTasks(filtered);
    }

    public static void main(String[] args) {
        // Set a modern look and feel
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(TaskManagerApp::new);
    }
}