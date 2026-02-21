/**
 * Task status constants for lifecycle tracking.
 * @readonly
 */
export const PENDING = 'PENDING';
export const IN_PROGRESS = 'IN_PROGRESS';
export const COMPLETED = 'COMPLETED';

/** @type {readonly string[]} */
export const ALL_STATUSES = Object.freeze([PENDING, IN_PROGRESS, COMPLETED]);

/**
 * @param {string} value
 * @returns {boolean}
 */
export function isValidStatus(value) {
  return typeof value === 'string' && ALL_STATUSES.includes(value);
}

export default {
  PENDING,
  IN_PROGRESS,
  COMPLETED,
  ALL_STATUSES,
  isValidStatus,
};
