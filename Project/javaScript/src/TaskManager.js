/**
 * Task operations: async CRUD, filtering by category/status/assignee.
 * Uses Mutex for critical sections and TaskRepository for persistence.
 */

import { Mutex } from './Mutex.js';
import * as TaskRepo from './TaskRepository.js';
import { createTask, updateTask, validateTaskInput } from './Task.js';
import { PENDING } from './TaskStatus.js';

const mutex = new Mutex();

/**
 * Generate a simple unique id.
 * @returns {string}
 */
function generateId() {
  return `task_${Date.now()}_${Math.random().toString(36).slice(2, 9)}`;
}

/**
 * Create a task (locked).
 * @param {Object} opts
 * @param {string} opts.title
 * @param {string} [opts.description]
 * @param {string} opts.category
 * @param {string} [opts.status]
 * @param {number} [opts.priority]
 * @param {string} [opts.assignee]
 * @param {string} opts.createdBy
 * @returns {Promise<{ success: boolean, task?: import('./Task.js').Task, error?: string }>}
 */
export async function create(opts) {
  const validation = validateTaskInput({
    title: opts.title,
    description: opts.description,
    category: opts.category,
    status: opts.status ?? PENDING,
    priority: opts.priority,
  });
  if (!validation.valid) {
    return { success: false, error: validation.error };
  }

  return mutex.runExclusive(async () => {
    const tasks = await TaskRepo.loadTasks();
    const task = createTask({
      id: generateId(),
      title: opts.title.trim(),
      description: opts.description?.trim() ?? '',
      category: opts.category,
      status: opts.status ?? PENDING,
      priority: opts.priority ?? 0,
      assignee: opts.assignee?.trim() ?? '',
      createdBy: opts.createdBy,
    });
    tasks.push(task);
    await TaskRepo.saveTasks(tasks);
    return { success: true, task };
  });
}

/**
 * Get task by id.
 * @param {string} taskId
 * @returns {Promise<import('./Task.js').Task | null>}
 */
export async function getById(taskId) {
  return mutex.runExclusive(async () => {
    const tasks = await TaskRepo.loadTasks();
    return tasks.find((t) => t.id === taskId) ?? null;
  });
}

/**
 * List tasks with optional filters.
 * @param {Object} [filters]
 * @param {string} [filters.category]
 * @param {string} [filters.status]
 * @param {string} [filters.assignee]
 * @param {string} [filters.createdBy]
 * @returns {Promise<import('./Task.js').Task[]>}
 */
export async function list(filters = {}) {
  return mutex.runExclusive(async () => {
    let tasks = await TaskRepo.loadTasks();
    if (filters.category) tasks = tasks.filter((t) => t.category === filters.category);
    if (filters.status) tasks = tasks.filter((t) => t.status === filters.status);
    if (filters.assignee) tasks = tasks.filter((t) => t.assignee === filters.assignee);
    if (filters.createdBy) tasks = tasks.filter((t) => t.createdBy === filters.createdBy);
    return tasks;
  });
}

/**
 * Update a task (locked).
 * @param {string} taskId
 * @param {Partial<Pick<import('./Task.js').Task, 'title'|'description'|'category'|'status'|'priority'|'assignee'>>} updates
 * @returns {Promise<{ success: boolean, task?: import('./Task.js').Task, error?: string }>}
 */
export async function update(taskId, updates) {
  if (updates.category !== undefined && !(await import('./Category.js')).isValidCategory(updates.category)) {
    return { success: false, error: 'Invalid category' };
  }
  if (updates.status !== undefined && !(await import('./TaskStatus.js')).isValidStatus(updates.status)) {
    return { success: false, error: 'Invalid status' };
  }

  return mutex.runExclusive(async () => {
    const tasks = await TaskRepo.loadTasks();
    const idx = tasks.findIndex((t) => t.id === taskId);
    if (idx === -1) return { success: false, error: 'Task not found' };
    tasks[idx] = updateTask(tasks[idx], updates);
    await TaskRepo.saveTasks(tasks);
    return { success: true, task: tasks[idx] };
  });
}

/**
 * Delete a task (locked).
 * @param {string} taskId
 * @returns {Promise<{ success: boolean, error?: string }>}
 */
export async function remove(taskId) {
  return mutex.runExclusive(async () => {
    const tasks = await TaskRepo.loadTasks();
    const filtered = tasks.filter((t) => t.id !== taskId);
    if (filtered.length === tasks.length) return { success: false, error: 'Task not found' };
    await TaskRepo.saveTasks(filtered);
    return { success: true };
  });
}

export default {
  create,
  getById,
  list,
  update,
  remove,
};
