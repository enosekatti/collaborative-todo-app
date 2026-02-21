package com.basanta.todo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void createSetsDefaults() {
        Task t = Task.create("t1", "Title", null, "WORK", null, 0, null, "u1");
        assertEquals("Title", t.getTitle());
        assertEquals(TaskStatus.PENDING, t.getStatus());
        assertEquals(0, t.getPriority());
    }

    @Test
    void withUpdatesReturnsNewTask() {
        Task t = Task.create("t1", "Old", null, "WORK", null, 0, null, "u1");
        Task t2 = t.withUpdates("New", null, null, "COMPLETED", null, null);
        assertEquals("New", t2.getTitle());
        assertEquals("COMPLETED", t2.getStatus());
        assertNotSame(t, t2);
    }

    @Test
    void validateTitleRejectsEmpty() {
        assertFalse(Task.validateTitle("").valid);
        assertFalse(Task.validateTitle("   ").valid);
    }

    @Test
    void validateTaskInputRejectsInvalidCategory() {
        User.ValidationResult r = Task.validateTaskInput("T", null, "INVALID", null, null);
        assertFalse(r.valid);
    }

    @Test
    void validateTaskInputAcceptsValid() {
        assertTrue(Task.validateTaskInput("My Task", null, "WORK", null, null).valid);
    }
}
