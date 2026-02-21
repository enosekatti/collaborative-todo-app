/**
 * Task category constants. Extensible: add new keys and include in ALL_CATEGORIES.
 * @readonly
 */
export const WORK = 'WORK';
export const PERSONAL = 'PERSONAL';
export const SHOPPING = 'SHOPPING';
export const HEALTH = 'HEALTH';
export const OTHER = 'OTHER';

/** @type {readonly string[]} */
export const ALL_CATEGORIES = Object.freeze([
  WORK,
  PERSONAL,
  SHOPPING,
  HEALTH,
  OTHER,
]);

/**
 * @param {string} value
 * @returns {boolean}
 */
export function isValidCategory(value) {
  return typeof value === 'string' && ALL_CATEGORIES.includes(value);
}

export default {
  WORK,
  PERSONAL,
  SHOPPING,
  HEALTH,
  OTHER,
  ALL_CATEGORIES,
  isValidCategory,
};
