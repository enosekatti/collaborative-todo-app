/**
 * Queue-based Mutex for critical section protection in async code.
 * Ensures only one async block runs at a time per mutex instance.
 */
export class Mutex {
  constructor() {
    /** @type {Promise<void>} */
    this._lock = Promise.resolve();
  }

  /**
   * Acquire lock, run fn, then release. Serializes concurrent callers.
   * @template T
   * @param {() => Promise<T>} fn
   * @returns {Promise<T>}
   */
  async runExclusive(fn) {
    const prev = this._lock;
    let release = /** @type {(value: void) => void} */ (() => {});
    this._lock = new Promise((resolve) => {
      release = resolve;
    });
    try {
      await prev;
      return await fn();
    } finally {
      release();
    }
  }
}

export default Mutex;
