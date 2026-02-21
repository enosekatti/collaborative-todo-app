/**
 * Tests for User module: createUser, getProfile, validation.
 */
import { describe, it } from 'node:test';
import assert from 'node:assert';
import {
  createUser,
  getProfile,
  validateUsername,
  validatePassword,
  updateProfile,
} from '../src/User.js';

describe('User', () => {
  it('createUser sets id, username, createdAt', () => {
    const u = createUser({
      id: 'u1',
      username: 'alice',
      passwordHash: 'hash',
    });
    assert.strictEqual(u.id, 'u1');
    assert.strictEqual(u.username, 'alice');
    assert.ok(u.createdAt);
  });

  it('getProfile omits passwordHash', () => {
    const u = createUser({ id: 'u1', username: 'bob', passwordHash: 'secret' });
    const p = getProfile(u);
    assert.strictEqual(p.username, 'bob');
    assert.strictEqual(p.id, 'u1');
    assert.ok(!('passwordHash' in p));
  });

  it('validateUsername rejects short or invalid', () => {
    assert.strictEqual(validateUsername('ab').valid, false);
    assert.strictEqual(validateUsername('a b').valid, false);
    assert.strictEqual(validateUsername('').valid, false);
  });

  it('validateUsername accepts valid', () => {
    assert.strictEqual(validateUsername('alice').valid, true);
    assert.strictEqual(validateUsername('user_1').valid, true);
  });

  it('validatePassword requires at least 4 chars', () => {
    assert.strictEqual(validatePassword('123').valid, false);
    assert.strictEqual(validatePassword('pass').valid, true);
  });

  it('updateProfile returns new user with updated fields', () => {
    const u = createUser({ id: 'u1', username: 'x', passwordHash: 'h', displayName: 'Old', email: 'old@x.com' });
    const u2 = updateProfile(u, { displayName: 'New' });
    assert.strictEqual(u2.displayName, 'New');
    assert.strictEqual(u2.email, 'old@x.com');
  });
});
