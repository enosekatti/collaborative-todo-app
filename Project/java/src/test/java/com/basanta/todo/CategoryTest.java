package com.basanta.todo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Day 2: Basic testing for Category.
 */
class CategoryTest {

    @Test
    void allCategoriesIncludesWorkPersonalOther() {
        assertTrue(Category.ALL_CATEGORIES.contains(Category.WORK));
        assertTrue(Category.ALL_CATEGORIES.contains(Category.PERSONAL));
        assertTrue(Category.ALL_CATEGORIES.contains(Category.OTHER));
        assertEquals(5, Category.ALL_CATEGORIES.size());
    }

    @Test
    void isValidCategoryAcceptsValid() {
        assertTrue(Category.isValidCategory("WORK"));
        assertTrue(Category.isValidCategory("PERSONAL"));
    }

    @Test
    void isValidCategoryRejectsInvalid() {
        assertFalse(Category.isValidCategory(""));
        assertFalse(Category.isValidCategory("INVALID"));
        assertFalse(Category.isValidCategory(null));
    }
}
