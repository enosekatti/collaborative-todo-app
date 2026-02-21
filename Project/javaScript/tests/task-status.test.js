/**
 * Tests for TaskStatus module.
 */
import { describe, it } from 'node:test';
import assert from 'node:assert';
import { ALL_STATUSES, isValidStatus, PENDING, COMPLETED } from '../src/TaskStatus.js';

describe('TaskStatus', () => {
  it('ALL_STATUSES includes PENDING and COMPLETED', () => {
    assert.ok(ALL_STATUSES.includes(PENDING));
    assert.ok(ALL_STATUSES.includes(COMPLETED));
    assert.strictEqual(ALL_STATUSES.length, 3);
  });

  it('isValidStatus accepts valid statuses', () => {
    assert.strictEqual(isValidStatus('PENDING'), true);
    assert.strictEqual(isValidStatus('IN_PROGRESS'), true);
    assert.strictEqual(isValidStatus('COMPLETED'), true);
  });

  it('isValidStatus rejects invalid values', () => {
    assert.strictEqual(isValidStatus(''), false);
    assert.strictEqual(isValidStatus('DONE'), false);
    assert.strictEqual(isValidStatus(null), false);
  });
});
