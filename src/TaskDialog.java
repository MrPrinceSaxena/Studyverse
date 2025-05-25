import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class TaskDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dueDateField; // format: yyyy-mm-dd
    private JComboBox<String> statusBox;
    private boolean confirmed = false;

    public TaskDialog(Frame owner, String title, Task task) {
        super(owner, title, true);
        setLayout(new BorderLayout(10, 10));
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Title:"));
        titleField = new JTextField(task != null ? task.getTitle() : "");
        panel.add(titleField);

        panel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(task != null ? task.getDescription() : "", 4, 20);
        panel.add(new JScrollPane(descriptionArea));

        panel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        dueDateField = new JTextField(task != null && task.getDueDate() != null ? task.getDueDate().toString() : "");
        panel.add(dueDateField);

        panel.add(new JLabel("Status:"));
        statusBox = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});
        if (task != null) statusBox.setSelectedItem(task.getStatus());
        panel.add(statusBox);

        add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");

        okBtn.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });
        cancelBtn.addActionListener(e -> setVisible(false));

        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);

        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getTaskTitle() {
        return titleField.getText().trim();
    }

    public String getTaskDescription() {
        return descriptionArea.getText().trim();
    }

    public LocalDate getTaskDueDate() {
        try {
            return dueDateField.getText().trim().isEmpty() ? null : LocalDate.parse(dueDateField.getText().trim());
        } catch (Exception e) {
            return null;
        }
    }

    public String getTaskStatus() {
        return (String) statusBox.getSelectedItem();
    }
}