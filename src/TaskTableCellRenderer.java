import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;

public class TaskTableCellRenderer extends DefaultTableCellRenderer {
    private TaskTableModel model;

    public TaskTableCellRenderer(TaskTableModel model) {
        this.model = model;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Task task = model.getTaskAt(row);
        LocalDate due = task.getDueDate();
        if (due != null && due.isBefore(LocalDate.now()) && !task.getStatus().equals("Completed")) {
            c.setBackground(new Color(255, 180, 180)); // Overdue: light red
        } else if (isSelected) {
            c.setBackground(table.getSelectionBackground());
        } else {
            c.setBackground(Color.white);
        }
        return c;
    }
}