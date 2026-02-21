package com.basanta.todo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Data persistence: JSON file I/O (language-specific: Gson, NIO Path/Files).
 * Atomic writes via temp file + rename.
 */
public final class TaskRepository {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path DATA_DIR = Path.of("data").toAbsolutePath();
    private static final Path TASKS_FILE = DATA_DIR.resolve("tasks.json");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.json");

    private static void ensureDataDir() throws IOException {
        Files.createDirectories(DATA_DIR);
    }

    @SuppressWarnings("unchecked")
    private static <T> T readJson(Path path, java.lang.reflect.Type type) throws IOException {
        if (!Files.exists(path)) return (T) List.of();
        String content = Files.readString(path);
        if (content.isBlank()) return (T) List.of();
        try {
            return GSON.fromJson(content, type);
        } catch (Exception e) {
            return (T) List.of();
        }
    }

    private static void writeJson(Path path, Object data) throws IOException {
        ensureDataDir();
        Path tmp = path.getParent().resolve(path.getFileName() + "." + System.currentTimeMillis() + ".tmp");
        Files.writeString(tmp, GSON.toJson(data));
        Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
    }

    public static List<Task> loadTasks() throws IOException {
        ensureDataDir();
        List<TaskEntity> raw = readJson(TASKS_FILE, new TypeToken<List<TaskEntity>>(){}.getType());
        if (raw == null) return new ArrayList<>();
        List<Task> out = new ArrayList<>();
        for (TaskEntity e : raw) {
            if (e != null) out.add(e.toTask());
        }
        return out;
    }

    public static void saveTasks(List<Task> tasks) throws IOException {
        List<TaskEntity> out = new ArrayList<>();
        for (Task t : tasks) out.add(TaskEntity.from(t));
        writeJson(TASKS_FILE, out);
    }

    public static List<User> loadUsers() throws IOException {
        ensureDataDir();
        List<UserEntity> raw = readJson(USERS_FILE, new TypeToken<List<UserEntity>>(){}.getType());
        if (raw == null) return new ArrayList<>();
        List<User> out = new ArrayList<>();
        for (UserEntity e : raw) {
            if (e != null) out.add(e.toUser());
        }
        return out;
    }

    public static void saveUsers(List<User> users) throws IOException {
        List<UserEntity> out = new ArrayList<>();
        for (User u : users) out.add(UserEntity.from(u));
        writeJson(USERS_FILE, out);
    }

    /** POJO for Gson (no-arg constructor). */
    @SuppressWarnings("unused")
    public static class TaskEntity {
        public String id, title, description, category, status;
        public int priority;
        public String assignee, createdBy, createdAt, updatedAt;

        public TaskEntity() {}

        Task toTask() {
            return new Task(id, title, description, category, status, priority, assignee, createdBy, createdAt, updatedAt);
        }

        static TaskEntity from(Task t) {
            TaskEntity e = new TaskEntity();
            e.id = t.getId();
            e.title = t.getTitle();
            e.description = t.getDescription();
            e.category = t.getCategory();
            e.status = t.getStatus();
            e.priority = t.getPriority();
            e.assignee = t.getAssignee();
            e.createdBy = t.getCreatedBy();
            e.createdAt = t.getCreatedAt();
            e.updatedAt = t.getUpdatedAt();
            return e;
        }
    }

    @SuppressWarnings("unused")
    public static class UserEntity {
        public String id, username, passwordHash, displayName, email, createdAt;

        public UserEntity() {}

        User toUser() {
            return new User(id, username, passwordHash, displayName, email, createdAt);
        }

        static UserEntity from(User u) {
            UserEntity e = new UserEntity();
            e.id = u.getId();
            e.username = u.getUsername();
            e.passwordHash = u.getPasswordHash();
            e.displayName = u.getDisplayName();
            e.email = u.getEmail();
            e.createdAt = u.getCreatedAt();
            return e;
        }
    }
}
