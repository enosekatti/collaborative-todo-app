package com.basanta.todo;

import java.util.List;

/**
 * Task category constants (language-specific: immutable list).
 * Extensible: add new values and include in ALL_CATEGORIES.
 */
public final class Category {
    public static final String WORK = "WORK";
    public static final String PERSONAL = "PERSONAL";
    public static final String SHOPPING = "SHOPPING";
    public static final String HEALTH = "HEALTH";
    public static final String OTHER = "OTHER";

    /** Immutable list of all valid categories. */
    public static final List<String> ALL_CATEGORIES = List.of(WORK, PERSONAL, SHOPPING, HEALTH, OTHER);

    private Category() {}

    public static boolean isValidCategory(String value) {
        return value != null && ALL_CATEGORIES.contains(value);
    }
}
