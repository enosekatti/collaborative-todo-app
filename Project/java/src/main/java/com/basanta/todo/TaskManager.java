package com.basanta.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Task operations: CRUD and filtering (language-specific: Optional, streams, Mutex).
 */
public final class TaskManager {
    private static final Mutex mutex = new Mutex();

    private static String generateId() {
        return "task_" + System.currentTimeMillis() + "_" + Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    public static Result<Task> create(String title, String description, String category, String status,
                                      int priority, String assignee, String createdBy) {
        String cat = category != null ? category : Category.OTHER;
        String st = status != null ? status : TaskStatus.PENDING;
        User.ValidationResult vr = Task.validateTaskInput(title, description, cat, st, priority);
        if (!vr.valid) return Result.fail(vr.error);

        return mutex.runExclusive(() -> {
            try {
                List<Task> tasks = new ArrayList<>(TaskRepository.loadTasks());
                Task task = Task.create(generateId(), title != null ? title.trim() : "",
                    description != null ? description.trim() : "", cat, st, priority,
                    assignee != null ? assignee.trim() : "", createdBy);
                tasks.add(task);
                TaskRepository.saveTasks(tasks);
                return Result.ok(task);
            } catch (IOException e) {
                return Result.fail(e.getMessage());
            }
        });
    }

    public static Optional<Task> getById(String taskId) {
        return mutex.runExclusive(() -> {
            try {
                return TaskRepository.loadTasks().stream()
                    .filter(t -> t.getId().equals(taskId))
                    .findFirst();
            } catch (IOException e) {
                return Optional.empty();
            }
        });
    }

    /** List with optional filters (streams for filtering). */
    public static List<Task> list(String category, String status, String assignee, String createdBy) {
        return mutex.runExclusive(() -> {
            try {
                List<Task> tasks = new ArrayList<>(TaskRepository.loadTasks());
                if (category != null && !category.isBlank())
                    tasks = tasks.stream().filter(t -> category.equals(t.getCategory())).toList();
                if (status != null && !status.isBlank())
                    tasks = tasks.stream().filter(t -> status.equals(t.getStatus())).toList();
                if (assignee != null && !assignee.isBlank())
                    tasks = tasks.stream().filter(t -> assignee.equals(t.getAssignee())).toList();
                if (createdBy != null && !createdBy.isBlank())
                    tasks = tasks.stream().filter(t -> createdBy.equals(t.getCreatedBy())).toList();
                return new ArrayList<>(tasks);
            } catch (IOException e) {
                return List.of();
            }
        });
    }

    public static Result<Task> update(String taskId, String title, String description, String category, String status, Integer priority, String assignee) {
        if (category != null && !Category.isValidCategory(category)) return Result.fail("Invalid category");
        if (status != null && !TaskStatus.isValidStatus(status)) return Result.fail("Invalid status");

        return mutex.runExclusive(() -> {
            try {
                List<Task> tasks = new ArrayList<>(TaskRepository.loadTasks());
                int idx = -1;
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getId().equals(taskId)) { idx = i; break; }
                }
                if (idx == -1) return Result.fail("Task not found");
                Task t = tasks.get(idx);
                tasks.set(idx, t.withUpdates(title, description, category, status, priority, assignee));
                TaskRepository.saveTasks(tasks);
                return Result.ok(tasks.get(idx));
            } catch (IOException e) {
                return Result.fail(e.getMessage());
            }
        });
    }

    public static Result<Void> remove(String taskId) {
        return mutex.runExclusive(() -> {
            try {
                List<Task> tasks = new ArrayList<>(TaskRepository.loadTasks());
                boolean removed = tasks.removeIf(t -> t.getId().equals(taskId));
                if (!removed) return Result.fail("Task not found");
                TaskRepository.saveTasks(tasks);
                return Result.ok(null);
            } catch (IOException e) {
                return Result.fail(e.getMessage());
            }
        });
    }
}
