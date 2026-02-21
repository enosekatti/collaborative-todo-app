package com.basanta.todo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

/**
 * User model: immutable object, profile, validation.
 * Language-specific: final class, encapsulation, SHA-256 via MessageDigest.
 */
public final class User {
    private final String id;
    private final String username;
    private final String passwordHash;
    private final String displayName;
    private final String email;
    private final String createdAt;

    public User(String id, String username, String passwordHash, String displayName, String email, String createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.displayName = displayName != null ? displayName : username;
        this.email = email != null ? email : "";
        this.createdAt = createdAt != null ? createdAt : Instant.now().toString();
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public String getCreatedAt() { return createdAt; }

    /** Public profile (no password). */
    public UserProfile toProfile() {
        return new UserProfile(id, username, displayName, email, createdAt);
    }

    public static User create(String id, String username, String passwordHash, String displayName, String email) {
        return new User(id, username, passwordHash, displayName, email, Instant.now().toString());
    }

    /** Immutable update: returns new User. */
    public User withProfile(String displayName, String email) {
        return new User(id, username, passwordHash,
            displayName != null ? displayName : this.displayName,
            email != null ? email : this.email,
            createdAt);
    }

    public static ValidationResult validateUsername(String username) {
        if (username == null || username.isBlank())
            return ValidationResult.error("Username is required");
        if (username.trim().length() < 3)
            return ValidationResult.error("Username must be at least 3 characters");
        if (!username.matches("^[a-zA-Z0-9_-]+$"))
            return ValidationResult.error("Username may only contain letters, numbers, underscore, hyphen");
        return ValidationResult.ok();
    }

    public static ValidationResult validatePassword(String password) {
        if (password == null || password.isEmpty())
            return ValidationResult.error("Password is required");
        if (password.length() < 4)
            return ValidationResult.error("Password must be at least 4 characters");
        return ValidationResult.ok();
    }

    /** SHA-256 hash for password storage (language-specific: MessageDigest). */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /** DTO for public profile (no password). */
    public static final class UserProfile {
        public final String id;
        public final String username;
        public final String displayName;
        public final String email;
        public final String createdAt;

        public UserProfile(String id, String username, String displayName, String email, String createdAt) {
            this.id = id;
            this.username = username;
            this.displayName = displayName;
            this.email = email;
            this.createdAt = createdAt;
        }
    }

    /** Validation result (success or error message). */
    public static final class ValidationResult {
        public final boolean valid;
        public final String error;

        private ValidationResult(boolean valid, String error) {
            this.valid = valid;
            this.error = error;
        }
        static ValidationResult ok() { return new ValidationResult(true, null); }
        static ValidationResult error(String msg) { return new ValidationResult(false, msg); }
    }
}
