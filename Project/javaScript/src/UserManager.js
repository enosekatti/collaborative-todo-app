/**
 * User operations: async authentication, registration, profile management.
 */

import { Mutex } from './Mutex.js';
import * as TaskRepo from './TaskRepository.js';
import {
  createUser,
  getProfile,
  updateProfile,
  validateUsername,
  validatePassword,
  hashPassword,
} from './User.js';

const mutex = new Mutex();

/**
 * @param {string} id
 * @returns {string}
 */
function generateId() {
  return `user_${Date.now()}_${Math.random().toString(36).slice(2, 9)}`;
}

/**
 * Register a new user.
 * @param {string} username
 * @param {string} password
 * @param {{ displayName?: string, email?: string }} [profile]
 * @returns {Promise<{ success: boolean, user?: import('./User.js').UserProfile, error?: string }>}
 */
export async function register(username, password, profile = {}) {
  const u = validateUsername(username);
  if (!u.valid) return { success: false, error: u.error };
  const p = validatePassword(password);
  if (!p.valid) return { success: false, error: p.error };

  return mutex.runExclusive(async () => {
    const users = await TaskRepo.loadUsers();
    const normalized = username.trim().toLowerCase();
    if (users.some((x) => x.username.toLowerCase() === normalized)) {
      return { success: false, error: 'Username already taken' };
    }
    const passwordHash = await hashPassword(password);
    const user = createUser({
      id: generateId(),
      username: username.trim(),
      passwordHash,
      displayName: profile.displayName ?? username.trim(),
      email: profile.email ?? '',
    });
    users.push(user);
    await TaskRepo.saveUsers(users);
    return { success: true, user: getProfile(user) };
  });
}

/**
 * Authenticate user by username and password.
 * @param {string} username
 * @param {string} password
 * @returns {Promise<{ success: boolean, user?: import('./User.js').UserProfile, error?: string }>}
 */
export async function login(username, password) {
  const passwordHash = await hashPassword(password);
  return mutex.runExclusive(async () => {
    const users = await TaskRepo.loadUsers();
    const normalized = username.trim().toLowerCase();
    const user = users.find((u) => u.username.toLowerCase() === normalized);
    if (!user || user.passwordHash !== passwordHash) {
      return { success: false, error: 'Invalid username or password' };
    }
    return { success: true, user: getProfile(user) };
  });
}

/**
 * Get user by id (profile only).
 * @param {string} userId
 * @returns {Promise<import('./User.js').UserProfile | null>}
 */
export async function getById(userId) {
  return mutex.runExclusive(async () => {
    const users = await TaskRepo.loadUsers();
    const user = users.find((u) => u.id === userId);
    return user ? getProfile(user) : null;
  });
}

/**
 * Get user by username (internal; returns full user for assignee resolution).
 * @param {string} username
 * @returns {Promise<{ id: string, username: string } | null>}
 */
export async function getByUsername(username) {
  return mutex.runExclusive(async () => {
    const users = await TaskRepo.loadUsers();
    const normalized = username.trim().toLowerCase();
    const user = users.find((u) => u.username.toLowerCase() === normalized);
    return user ? { id: user.id, username: user.username } : null;
  });
}

/**
 * Update profile for a user (displayName, email).
 * @param {string} userId
 * @param {{ displayName?: string, email?: string }} updates
 * @returns {Promise<{ success: boolean, user?: import('./User.js').UserProfile, error?: string }>}
 */
export async function updateUserProfile(userId, updates) {
  return mutex.runExclusive(async () => {
    const users = await TaskRepo.loadUsers();
    const idx = users.findIndex((u) => u.id === userId);
    if (idx === -1) return { success: false, error: 'User not found' };
    users[idx] = updateProfile(users[idx], updates);
    await TaskRepo.saveUsers(users);
    return { success: true, user: getProfile(users[idx]) };
  });
}

export default {
  register,
  login,
  getById,
  getByUsername,
  updateUserProfile,
};
