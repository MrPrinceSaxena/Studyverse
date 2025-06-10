import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status; // Pending, Completed
    private String priority; // High, Medium, Low
    private String category;
    private String recurrence; // None, Daily, Weekly, Monthly
    private String notes;

    public Task(int id, String title, String description, LocalDate dueDate, String status, String priority, String category, String recurrence, String notes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
        this.category = category;
        this.recurrence = recurrence;
        this.notes = notes;
    }

    public Task(String title, String description, LocalDate dueDate, String status, String priority, String category, String recurrence, String notes) {
        this(-1, title, description, dueDate, status, priority, category, recurrence, notes);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getRecurrence() { return recurrence; }
    public void setRecurrence(String recurrence) { this.recurrence = recurrence; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}