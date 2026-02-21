/**
 * Tests for Task module: createTask, updateTask, validation.
 */
import { describe, it } from 'node:test';
import assert from 'node:assert';
import {
  createTask,
  updateTask,
  validateTitle,
  validateTaskInput,
} from '../src/Task.js';

describe('Task', () => {
  it('createTask sets defaults for status and priority', () => {
    const t = createTask({
      id: 't1',
      title: 'Test',
      category: 'WORK',
      createdBy: 'u1',
    });
    assert.strictEqual(t.title, 'Test');
    assert.strictEqual(t.status, 'PENDING');
    assert.strictEqual(t.priority, 0);
    assert.ok(t.createdAt);
    assert.ok(t.updatedAt);
  });

  it('updateTask returns new task with updated fields', () => {
    const t = createTask({ id: 't1', title: 'Old', category: 'WORK', createdBy: 'u1' });
    const t2 = updateTask(t, { title: 'New', status: 'COMPLETED' });
    assert.strictEqual(t2.title, 'New');
    assert.strictEqual(t2.status, 'COMPLETED');
    assert.ok(t2.updatedAt);
  });

  it('validateTitle rejects empty', () => {
    assert.strictEqual(validateTitle('').valid, false);
    assert.strictEqual(validateTitle('   ').valid, false);
  });

  it('validateTaskInput rejects invalid category', () => {
    const r = validateTaskInput({ title: 'T', category: 'INVALID' });
    assert.strictEqual(r.valid, false);
  });

  it('validateTaskInput accepts valid input', () => {
    const r = validateTaskInput({ title: 'My Task', category: 'WORK' });
    assert.strictEqual(r.valid, true);
  });
});
