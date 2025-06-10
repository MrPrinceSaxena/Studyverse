import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TaskTableModel extends AbstractTableModel {
    private final String[] columns = {"Title", "Description", "Due Date", "Status", "Priority", "Category", "Recurrence", "Notes"};
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
            case 0: return t.getTitle();
            case 1: return t.getDescription();
            case 2: return t.getDueDate();
            case 3: return t.getStatus();
            case 4: return t.getPriority();
            case 5: return t.getCategory();
            case 6: return t.getRecurrence();
            case 7: return t.getNotes();
            default: return "";
        }
    }
}