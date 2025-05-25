import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskTableModel extends AbstractTableModel {
    private final String[] columns = {"ID", "Title", "Description", "Due Date", "Status"};
    private List<Task> tasks;

    public TaskTableModel(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        fireTableDataChanged();
    }

    public Task getTaskAt(int row) {
        return tasks.get(row);
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Task t = tasks.get(row);
        switch (col) {
            case 0: return t.getId();
            case 1: return t.getTitle();
            case 2: return t.getDescription();
            case 3: return t.getDueDate() != null ? t.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
            case 4: return t.getStatus();
            default: return "";
        }
    }
}