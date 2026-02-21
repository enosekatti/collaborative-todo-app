package com.basanta.todo;

import java.util.List;

/**
 * Task status constants for lifecycle tracking.
 */
public final class TaskStatus {
    public static final String PENDING = "PENDING";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String COMPLETED = "COMPLETED";

    public static final List<String> ALL_STATUSES = List.of(PENDING, IN_PROGRESS, COMPLETED);

    private TaskStatus() {}

    public static boolean isValidStatus(String value) {
        return value != null && ALL_STATUSES.contains(value);
    }
}
