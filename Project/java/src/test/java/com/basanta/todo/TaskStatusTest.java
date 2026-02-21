package com.basanta.todo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusTest {

    @Test
    void allStatusesIncludesPendingAndCompleted() {
        assertTrue(TaskStatus.ALL_STATUSES.contains(TaskStatus.PENDING));
        assertTrue(TaskStatus.ALL_STATUSES.contains(TaskStatus.COMPLETED));
        assertEquals(3, TaskStatus.ALL_STATUSES.size());
    }

    @Test
    void isValidStatusAcceptsValid() {
        assertTrue(TaskStatus.isValidStatus("PENDING"));
        assertTrue(TaskStatus.isValidStatus("IN_PROGRESS"));
    }

    @Test
    void isValidStatusRejectsInvalid() {
        assertFalse(TaskStatus.isValidStatus("DONE"));
        assertFalse(TaskStatus.isValidStatus(null));
    }
}
