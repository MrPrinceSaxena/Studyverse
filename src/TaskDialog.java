import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TaskDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descField;
    private JTextField dueDateField;
    private JComboBox<String> statusBox, priorityBox, recurrenceBox;
    private JComboBox<String> categoryBox;
    private JTextArea notesField;
    private boolean confirmed = false;
    private Task task;

    public TaskDialog(Frame owner, List<Category> categories, Task task) {
        super(owner, true);
        setTitle(task == null ? "Add Task" : "Edit Task");
        setLayout(new BorderLayout(10, 10));
        this.task = task;

        JPanel fields = new JPanel(new GridLayout(0, 2, 5, 5));
        titleField = new JTextField(task == null ? "" : task.getTitle());
        descField = new JTextArea(task == null ? "" : task.getDescription(), 2, 20);
        dueDateField = new JTextField(task != null && task.getDueDate() != null ? task.getDueDate().toString() : "");
        statusBox = new JComboBox<>(new String[]{"Pending", "Completed"});
        if (task != null) statusBox.setSelectedItem(task.getStatus());
        priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        if (task != null) priorityBox.setSelectedItem(task.getPriority());
        recurrenceBox = new JComboBox<>(new String[]{"None", "Daily", "Weekly", "Monthly"});
        if (task != null) recurrenceBox.setSelectedItem(task.getRecurrence());

        // For category, allow entering a new category (editable combo box)
        String[] catNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) catNames[i] = categories.get(i).getName();
        categoryBox = new JComboBox<>(catNames);
        categoryBox.setEditable(true);
        if (task != null && task.getCategory() != null) {
            categoryBox.setSelectedItem(task.getCategory());
        }

        notesField = new JTextArea(task == null ? "" : task.getNotes(), 2, 20);

        fields.add(new JLabel("Title*"));
        fields.add(titleField);
        fields.add(new JLabel("Description"));
        fields.add(new JScrollPane(descField));
        fields.add(new JLabel("Due Date (YYYY-MM-DD)"));
        fields.add(dueDateField);
        fields.add(new JLabel("Status"));
        fields.add(statusBox);
        fields.add(new JLabel("Priority"));
        fields.add(priorityBox);
        fields.add(new JLabel("Category"));
        fields.add(categoryBox);
        fields.add(new JLabel("Recurrence"));
        fields.add(recurrenceBox);
        fields.add(new JLabel("Notes"));
        fields.add(new JScrollPane(notesField));

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        JPanel btnPanel = new JPanel();
        btnPanel.add(ok); btnPanel.add(cancel);

        add(fields, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);

        ok.addActionListener(e -> {
            if (validateFields()) {
                confirmed = true;
                setVisible(false);
            }
        });
        cancel.addActionListener(e -> setVisible(false));
    }

    private boolean validateFields() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String due = dueDateField.getText().trim();
        if (!due.isEmpty()) {
            try {
                LocalDate.parse(due);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid due date format. Use YYYY-MM-DD.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public boolean isConfirmed() { return confirmed; }

    public Task getTask() {
        if (!confirmed) return null;
        LocalDate dueDate = null;
        String due = dueDateField.getText().trim();
        if (!due.isEmpty()) dueDate = LocalDate.parse(due);
        String cat = (String) categoryBox.getSelectedItem();
        if (cat != null && cat.trim().isEmpty()) cat = null;
        return new Task(
            task == null ? -1 : task.getId(),
            titleField.getText().trim(),
            descField.getText().trim(),
            dueDate,
            statusBox.getSelectedItem().toString(),
            priorityBox.getSelectedItem().toString(),
            cat,
            recurrenceBox.getSelectedItem().toString(),
            notesField.getText().trim()
        );
    }
}