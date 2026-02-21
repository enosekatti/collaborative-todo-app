package com.basanta.todo;
import java.time.Instant;

/**
 * Task model: immutable object, validation.
 * Language-specific: final class, Instant for timestamps.
 */
public final class Task {
    private final String id;
    private final String title;
    private final String description;
    private final String category;
    private final String status;
    private final int priority;
    private final String assignee;
    private final String createdBy;
    private final String createdAt;
    private final String updatedAt;

    public Task(String id, String title, String description, String category, String status,
                int priority, String assignee, String createdBy, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description != null ? description : "";
        this.category = category;
        this.status = status != null ? status : TaskStatus.PENDING;
        this.priority = priority;
        this.assignee = assignee != null ? assignee : "";
        this.createdBy = createdBy;
        this.createdAt = createdAt != null ? createdAt : Instant.now().toString();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public int getPriority() { return priority; }
    public String getAssignee() { return assignee; }
    public String getCreatedBy() { return createdBy; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    public static Task create(String id, String title, String description, String category, String status,
                              int priority, String assignee, String createdBy) {
        String now = Instant.now().toString();
        return new Task(id, title, description, category, status != null ? status : TaskStatus.PENDING,
            priority, assignee, createdBy, now, now);
    }

    /** Immutable update: returns new Task (null means keep existing). */
    public Task withUpdates(String title, String description, String category, String status, Integer priority, String assignee) {
        return new Task(
            id,
            title != null ? title : this.title,
            description != null ? description : this.description,
            category != null ? category : this.category,
            status != null ? status : this.status,
            priority != null ? priority : this.priority,
            assignee != null ? assignee : this.assignee,
            createdBy,
            createdAt,
            Instant.now().toString()
        );
    }

    public static User.ValidationResult validateTitle(String title) {
        if (title == null || title.isBlank())
            return User.ValidationResult.error("Title is required");
        if (title.length() > 200)
            return User.ValidationResult.error("Title must be at most 200 characters");
        return User.ValidationResult.ok();
    }

    public static User.ValidationResult validateTaskInput(String title, String description, String category, String status, Integer priority) {
        User.ValidationResult tr = validateTitle(title);
        if (!tr.valid) return tr;
        if (!Category.isValidCategory(category))
            return User.ValidationResult.error("Invalid category. Must be one of: WORK, PERSONAL, SHOPPING, HEALTH, OTHER");
        if (status != null && !TaskStatus.isValidStatus(status))
            return User.ValidationResult.error("Invalid status. Must be one of: PENDING, IN_PROGRESS, COMPLETED");
        if (priority != null && (priority < 0 || priority > 10))
            return User.ValidationResult.error("Priority must be between 0 and 10");
        return User.ValidationResult.ok();
    }
}
