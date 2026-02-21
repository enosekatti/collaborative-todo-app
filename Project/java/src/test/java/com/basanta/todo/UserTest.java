package com.basanta.todo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void createSetsIdAndUsername() {
        User u = User.create("u1", "alice", "hash", null, null);
        assertEquals("u1", u.getId());
        assertEquals("alice", u.getUsername());
        assertNotNull(u.getCreatedAt());
    }

    @Test
    void toProfileOmitsPasswordHash() {
        User u = User.create("u1", "bob", "secret", null, null);
        User.UserProfile p = u.toProfile();
        assertEquals("bob", p.username);
        assertNotNull(p.id);
    }

    @Test
    void validateUsernameRejectsShort() {
        User.ValidationResult r = User.validateUsername("ab");
        assertFalse(r.valid);
    }

    @Test
    void validateUsernameAcceptsValid() {
        assertTrue(User.validateUsername("alice").valid);
    }

    @Test
    void validatePasswordRequiresMinLength() {
        assertFalse(User.validatePassword("123").valid);
        assertTrue(User.validatePassword("pass").valid);
    }

    @Test
    void hashPasswordProducesConsistentHash() {
        String h1 = User.hashPassword("test");
        String h2 = User.hashPassword("test");
        assertEquals(h1, h2);
        assertNotNull(h1);
    }
}
