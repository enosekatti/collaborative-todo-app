package com.basanta.todo;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * CLI entry point: menu-driven user and task management (same flow as JS index.js).
 */
public final class Main {
    private static User.UserProfile currentUser;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Collaborative To-Do — CLI (Java)");
        run();
    }

    private static String question(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static void print(String msg) {
        System.out.println(msg);
    }

    private static void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            print("  (no tasks)");
            return;
        }
        for (Task t : tasks) {
            print("  [" + t.getId() + "] " + t.getTitle() + " | " + t.getCategory() + " | " + t.getStatus()
                + " | assignee: " + (t.getAssignee().isEmpty() ? "-" : t.getAssignee()) + " | priority: " + t.getPriority());
        }
    }

    private static String mainMenu() {
        print("\n--- Collaborative To-Do ---");
        if (currentUser != null) {
            print("Logged in as: " + currentUser.username);
            print("  1. List my tasks");
            print("  2. List tasks by category");
            print("  3. List tasks by status");
            print("  4. List tasks assigned to me");
            print("  5. Create task");
            print("  6. Update task");
            print("  7. Delete task");
            print("  8. Logout");
            print("  0. Exit");
        } else {
            print("  (Log in to create and manage tasks)");
            print("  1. Register");
            print("  2. Login");
            print("  0. Exit");
        }
        return question("Choice: ");
    }

    private static void handleListMyTasks() {
        List<Task> tasks = TaskManager.list(null, null, null, currentUser.id);
        print("\nMy tasks (created by me):");
        printTasks(tasks);
    }

    private static void handleListByCategory() {
        print("Categories: " + String.join(", ", Category.ALL_CATEGORIES));
        String cat = question("Category: ").toUpperCase();
        List<Task> tasks = TaskManager.list(cat.isEmpty() ? null : cat, null, null, null);
        if (!cat.isEmpty() && !Category.ALL_CATEGORIES.contains(cat)) {
            print("Invalid category. Showing results for that input anyway.");
        }
        printTasks(tasks);
    }

    private static void handleListByStatus() {
        print("Statuses: " + String.join(", ", TaskStatus.ALL_STATUSES));
        String status = question("Status: ").toUpperCase();
        List<Task> tasks = TaskManager.list(null, status.isEmpty() ? null : status, null, null);
        printTasks(tasks);
    }

    private static void handleListAssignedToMe() {
        List<Task> tasks = TaskManager.list(null, null, currentUser.username, null);
        print("\nTasks assigned to me:");
        printTasks(tasks);
    }

    private static void handleCreateTask() {
        String title = question("Title: ");
        String description = question("Description (optional): ");
        print("Category: " + String.join(", ", Category.ALL_CATEGORIES));
        String category = question("Category: ").toUpperCase();
        if (category.isEmpty()) category = Category.OTHER;
        String priStr = question("Priority (0-10, default 0): ");
        int priority = priStr.isEmpty() ? 0 : Integer.parseInt(priStr);
        String assignee = question("Assignee username (optional): ");
        Result<Task> result = TaskManager.create(title, description, category, null, priority,
            assignee.isEmpty() ? null : assignee, currentUser.id);
        if (result.success) {
            print("Task created: " + result.value.getId());
        } else {
            print("Error: " + result.error);
        }
    }

    private static void handleUpdateTask() {
        String id = question("Task ID: ");
        Optional<Task> opt = TaskManager.getById(id);
        if (opt.isEmpty()) {
            print("Task not found.");
            return;
        }
        Task task = opt.get();
        print("Current: " + task.getTitle() + " | " + task.getStatus() + " | " + task.getCategory());
        String title = question("New title (Enter to keep): ");
        String status = question("New status PENDING|IN_PROGRESS|COMPLETED (Enter to keep): ");
        String category = question("New category (Enter to keep): ");
        String newTitle = title.isEmpty() ? null : title;
        String newStatus = status.isEmpty() ? null : status.toUpperCase();
        String newCategory = category.isEmpty() ? null : category.toUpperCase();
        Result<Task> result = TaskManager.update(id, newTitle, null, newCategory, newStatus, null, null);
        if (result.success) print("Task updated.");
        else print("Error: " + result.error);
    }

    private static void handleDeleteTask() {
        String id = question("Task ID: ");
        Result<Void> result = TaskManager.remove(id);
        if (result.success) print("Task deleted.");
        else print("Error: " + result.error);
    }

    private static void handleRegister() {
        String username = question("Username: ");
        String password = question("Password: ");
        String displayName = question("Display name (optional): ");
        String email = question("Email (optional): ");
        Result<User.UserProfile> result = UserManager.register(username, password,
            displayName.isEmpty() ? null : displayName, email.isEmpty() ? null : email);
        if (result.success) {
            print("Registered. You can now login.");
        } else {
            print("Error: " + result.error);
        }
    }

    private static void handleLogin() {
        String username = question("Username: ");
        String password = question("Password: ");
        Result<User.UserProfile> result = UserManager.login(username, password);
        if (result.success) {
            currentUser = result.value;
            print("Logged in as " + result.value.username);
        } else {
            print("Error: " + result.error);
        }
    }

    private static void run() {
        while (true) {
            String choice = mainMenu();
            if ("0".equals(choice)) {
                print("Bye.");
                scanner.close();
                System.exit(0);
            }
            if (currentUser != null) {
                switch (choice) {
                    case "1" -> handleListMyTasks();
                    case "2" -> handleListByCategory();
                    case "3" -> handleListByStatus();
                    case "4" -> handleListAssignedToMe();
                    case "5" -> handleCreateTask();
                    case "6" -> handleUpdateTask();
                    case "7" -> handleDeleteTask();
                    case "8" -> {
                        currentUser = null;
                        print("Logged out.");
                    }
                    default -> print("Invalid option.");
                }
            } else {
                switch (choice) {
                    case "1" -> handleRegister();
                    case "2" -> handleLogin();
                    default -> print("Invalid option.");
                }
            }
        }
    }
}
