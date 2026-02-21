package com.basanta.todo;

/**
 * Result type for operations that can fail with a message (no exceptions).
 */
public final class Result<T> {
    public final boolean success;
    public final T value;
    public final String error;

    private Result(boolean success, T value, String error) {
        this.success = success;
        this.value = value;
        this.error = error;
    }

    public static <T> Result<T> ok(T value) {
        return new Result<>(true, value, null);
    }

    public static <T> Result<T> fail(String error) {
        return new Result<>(false, null, error);
    }
}
