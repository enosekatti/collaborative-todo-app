package com.basanta.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User operations: register, login, profile (language-specific: Optional, Mutex).
 */
public final class UserManager {
    private static final Mutex mutex = new Mutex();

    private static String generateId() {
        return "user_" + System.currentTimeMillis() + "_" + Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    public static Result<User.UserProfile> register(String username, String password, String displayName, String email) {
        User.ValidationResult u = User.validateUsername(username);
        if (!u.valid) return Result.fail(u.error);
        User.ValidationResult p = User.validatePassword(password);
        if (!p.valid) return Result.fail(p.error);

        return mutex.runExclusive(() -> {
            try {
                List<User> users = new ArrayList<>(TaskRepository.loadUsers());
                String normalized = username.trim().toLowerCase();
                boolean exists = users.stream().anyMatch(x -> x.getUsername().toLowerCase().equals(normalized));
                if (exists) return Result.fail("Username already taken");
                String hash = User.hashPassword(password);
                User user = User.create(generateId(), username.trim(), hash,
                    displayName != null && !displayName.isBlank() ? displayName.trim() : username.trim(),
                    email != null ? email.trim() : "");
                users.add(user);
                TaskRepository.saveUsers(users);
                return Result.ok(user.toProfile());
            } catch (IOException e) {
                return Result.fail(e.getMessage());
            }
        });
    }

    public static Result<User.UserProfile> login(String username, String password) {
        String passwordHash = User.hashPassword(password);
        return mutex.runExclusive(() -> {
            try {
                List<User> users = TaskRepository.loadUsers();
                String normalized = username.trim().toLowerCase();
                User user = users.stream()
                    .filter(u -> u.getUsername().toLowerCase().equals(normalized))
                    .findFirst()
                    .orElse(null);
                if (user == null || !user.getPasswordHash().equals(passwordHash))
                    return Result.fail("Invalid username or password");
                return Result.ok(user.toProfile());
            } catch (IOException e) {
                return Result.fail(e.getMessage());
            }
        });
    }

    public static Optional<User.UserProfile> getById(String userId) {
        return mutex.runExclusive(() -> {
            try {
                return TaskRepository.loadUsers().stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst()
                    .map(User::toProfile);
            } catch (IOException e) {
                return Optional.empty();
            }
        });
    }

    public static Result<User.UserProfile> updateUserProfile(String userId, String displayName, String email) {
        return mutex.runExclusive(() -> {
            try {
                List<User> users = new ArrayList<>(TaskRepository.loadUsers());
                int idx = -1;
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getId().equals(userId)) { idx = i; break; }
                }
                if (idx == -1) return Result.fail("User not found");
                User u = users.get(idx);
                users.set(idx, u.withProfile(displayName, email));
                TaskRepository.saveUsers(users);
                return Result.ok(users.get(idx).toProfile());
            } catch (IOException e) {
                return Result.fail(e.getMessage());
            }
        });
    }
}
