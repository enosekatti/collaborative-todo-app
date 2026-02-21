/**
 * Tests for Category module (Day 2: basic testing).
 */
import { describe, it } from 'node:test';
import assert from 'node:assert';
import { ALL_CATEGORIES, isValidCategory, WORK, PERSONAL, OTHER } from '../src/Category.js';

describe('Category', () => {
  it('ALL_CATEGORIES includes WORK, PERSONAL, OTHER', () => {
    assert.ok(ALL_CATEGORIES.includes(WORK));
    assert.ok(ALL_CATEGORIES.includes(PERSONAL));
    assert.ok(ALL_CATEGORIES.includes(OTHER));
    assert.strictEqual(ALL_CATEGORIES.length, 5);
  });

  it('isValidCategory accepts valid categories', () => {
    assert.strictEqual(isValidCategory('WORK'), true);
    assert.strictEqual(isValidCategory('PERSONAL'), true);
    assert.strictEqual(isValidCategory('OTHER'), true);
  });

  it('isValidCategory rejects invalid values', () => {
    assert.strictEqual(isValidCategory(''), false);
    assert.strictEqual(isValidCategory('INVALID'), false);
    assert.strictEqual(isValidCategory(null), false);
    assert.strictEqual(isValidCategory(undefined), false);
  });
});
