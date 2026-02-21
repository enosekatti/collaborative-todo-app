/**
 * Task module: task object, task methods, validation.
 */

import { isValidCategory } from './Category.js';
import { isValidStatus } from './TaskStatus.js';

/**
 * @typedef {Object} Task
 * @property {string} id
 * @property {string} title
 * @property {string} description
 * @property {string} category
 * @property {string} status
 * @property {number} priority
 * @property {string} assignee
 * @property {string} createdBy
 * @property {string} createdAt
 * @property {string} updatedAt
 */

/**
 * Create a task object.
 * @param {Object} opts
 * @param {string} opts.id
 * @param {string} opts.title
 * @param {string} [opts.description]
 * @param {string} opts.category
 * @param {string} [opts.status]
 * @param {number} [opts.priority]
 * @param {string} [opts.assignee]
 * @param {string} opts.createdBy
 * @returns {Task}
 */
export function createTask({
  id,
  title,
  description = '',
  category,
  status = 'PENDING',
  priority = 0,
  assignee = '',
  createdBy,
}) {
  const now = new Date().toISOString();
  return {
    id,
    title,
    description: description || '',
    category,
    status: status || 'PENDING',
    priority: typeof priority === 'number' ? priority : 0,
    assignee: assignee || '',
    createdBy,
    createdAt: now,
    updatedAt: now,
  };
}

/**
 * Update task fields (returns new task object).
 * @param {Task} task
 * @param {Partial<Pick<Task, 'title'|'description'|'category'|'status'|'priority'|'assignee'>>} updates
 * @returns {Task}
 */
export function updateTask(task, updates) {
  const updatedAt = new Date().toISOString();
  return {
    ...task,
    ...updates,
    updatedAt,
  };
}

/**
 * @param {string} title
 * @returns {{ valid: boolean, error?: string }}
 */
export function validateTitle(title) {
  if (typeof title !== 'string' || !title.trim()) {
    return { valid: false, error: 'Title is required' };
  }
  if (title.length > 200) {
    return { valid: false, error: 'Title must be at most 200 characters' };
  }
  return { valid: true };
}

/**
 * @param {Object} input
 * @param {string} input.title
 * @param {string} [input.description]
 * @param {string} input.category
 * @param {string} [input.status]
 * @param {number} [input.priority]
 * @returns {{ valid: boolean, error?: string }}
 */
export function validateTaskInput({ title, description, category, status, priority }) {
  const titleResult = validateTitle(title);
  if (!titleResult.valid) return titleResult;
  if (!isValidCategory(category)) {
    return { valid: false, error: `Invalid category. Must be one of: WORK, PERSONAL, SHOPPING, HEALTH, OTHER` };
  }
  if (status !== undefined && !isValidStatus(status)) {
    return { valid: false, error: `Invalid status. Must be one of: PENDING, IN_PROGRESS, COMPLETED` };
  }
  if (priority !== undefined && (typeof priority !== 'number' || priority < 0 || priority > 10)) {
    return { valid: false, error: 'Priority must be a number between 0 and 10' };
  }
  return { valid: true };
}

export default {
  createTask,
  updateTask,
  validateTitle,
  validateTaskInput,
};
