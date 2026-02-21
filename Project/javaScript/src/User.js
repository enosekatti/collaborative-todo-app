/**
 * User module: user object, profile methods, validation.
 */

/**
 * @typedef {Object} UserProfile
 * @property {string} id
 * @property {string} username
 * @property {string} displayName
 * @property {string} [email]
 * @property {string} createdAt
 */

/**
 * @typedef {Object} User
 * @property {string} id
 * @property {string} username
 * @property {string} passwordHash
 * @property {string} displayName
 * @property {string} email
 * @property {string} createdAt
 */

/**
 * Create a user object.
 * @param {Object} opts
 * @param {string} opts.id
 * @param {string} opts.username
 * @param {string} opts.passwordHash
 * @param {string} [opts.displayName]
 * @param {string} [opts.email]
 * @returns {{ id: string, username: string, passwordHash: string, displayName: string, email: string, createdAt: string }}
 */
export function createUser({ id, username, passwordHash, displayName = '', email = '' }) {
  const createdAt = new Date().toISOString();
  return {
    id,
    username,
    passwordHash,
    displayName: displayName || username,
    email: email || '',
    createdAt,
  };
}

/**
 * Get public profile (no password).
 * @param {import('./User.js').User} user
 * @returns {UserProfile}
 */
export function getProfile(user) {
  return {
    id: user.id,
    username: user.username,
    displayName: user.displayName,
    email: user.email,
    createdAt: user.createdAt,
  };
}

/**
 * Update profile fields (immutable; returns new user object).
 * @param {{ id: string, username: string, passwordHash: string, displayName: string, email: string, createdAt: string }} user
 * @param {{ displayName?: string, email?: string }} updates
 * @returns {{ id: string, username: string, passwordHash: string, displayName: string, email: string, createdAt: string }}
 */
export function updateProfile(user, updates) {
  return {
    ...user,
    displayName: updates.displayName !== undefined ? updates.displayName : user.displayName,
    email: updates.email !== undefined ? updates.email : user.email,
  };
}

/**
 * @param {string} username
 * @returns {{ valid: boolean, error?: string }}
 */
export function validateUsername(username) {
  if (typeof username !== 'string' || !username.trim()) {
    return { valid: false, error: 'Username is required' };
  }
  if (username.length < 3) {
    return { valid: false, error: 'Username must be at least 3 characters' };
  }
  if (!/^[a-zA-Z0-9_-]+$/.test(username)) {
    return { valid: false, error: 'Username may only contain letters, numbers, underscore, hyphen' };
  }
  return { valid: true };
}

/**
 * @param {string} password
 * @returns {{ valid: boolean, error?: string }}
 */
export function validatePassword(password) {
  if (typeof password !== 'string' || !password) {
    return { valid: false, error: 'Password is required' };
  }
  if (password.length < 4) {
    return { valid: false, error: 'Password must be at least 4 characters' };
  }
  return { valid: true };
}

/**
 * Simple hash for demo (use crypto in production).
 * @param {string} password
 * @returns {Promise<string>}
 */
export async function hashPassword(password) {
  const crypto = await import('crypto');
  return crypto.default.createHash('sha256').update(password).digest('hex');
}

export default {
  createUser,
  getProfile,
  updateProfile,
  validateUsername,
  validatePassword,
  hashPassword,
};
