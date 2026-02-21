/**
 * Data persistence: async JSON file I/O for tasks and users.
 */

import fs from 'fs/promises';
import path from 'path';
import { fileURLToPath } from 'url';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const DATA_DIR = path.resolve(__dirname, '..', 'data');
const TASKS_FILE = path.join(DATA_DIR, 'tasks.json');
const USERS_FILE = path.join(DATA_DIR, 'users.json');

/**
 * Ensure data directory exists.
 * @returns {Promise<void>}
 */
async function ensureDataDir() {
  await fs.mkdir(DATA_DIR, { recursive: true });
}

/**
 * Read JSON file; return default if missing or invalid.
 * @param {string} filePath
 * @param {unknown} defaultValue
 * @returns {Promise<unknown>}
 */
async function readJson(filePath, defaultValue) {
  try {
    const data = await fs.readFile(filePath, 'utf-8');
    return JSON.parse(data);
  } catch (err) {
    if (err?.code === 'ENOENT') return defaultValue;
    throw err;
  }
}

/**
 * Write JSON file (atomic write via temp file).
 * @param {string} filePath
 * @param {unknown} data
 * @returns {Promise<void>}
 */
async function writeJson(filePath, data) {
  await ensureDataDir();
  const tmp = `${filePath}.${Date.now()}.tmp`;
  await fs.writeFile(tmp, JSON.stringify(data, null, 2), 'utf-8');
  await fs.rename(tmp, filePath);
}

/**
 * Load all tasks.
 * @returns {Promise<import('./Task.js').Task[]>}
 */
export async function loadTasks() {
  await ensureDataDir();
  const raw = await readJson(TASKS_FILE, []);
  return Array.isArray(raw) ? raw : [];
}

/**
 * Save all tasks.
 * @param {import('./Task.js').Task[]} tasks
 * @returns {Promise<void>}
 */
export async function saveTasks(tasks) {
  await writeJson(TASKS_FILE, tasks);
}

/**
 * Load all users.
 * @returns {Promise<Array<{ id: string, username: string, passwordHash: string, displayName: string, email: string, createdAt: string }>>}
 */
export async function loadUsers() {
  await ensureDataDir();
  const raw = await readJson(USERS_FILE, []);
  return Array.isArray(raw) ? raw : [];
}

/**
 * Save all users.
 * @param {Array<{ id: string, username: string, passwordHash: string, displayName: string, email: string, createdAt: string }>} users
 * @returns {Promise<void>}
 */
export async function saveUsers(users) {
  await writeJson(USERS_FILE, users);
}

export default {
  loadTasks,
  saveTasks,
  loadUsers,
  saveUsers,
  ensureDataDir,
};
