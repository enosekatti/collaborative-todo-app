package com.basanta.todo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Mutex for critical section protection (language-specific: ReentrantLock).
 * Serializes concurrent access to shared data.
 */
public final class Mutex {
    private final Lock lock = new ReentrantLock();

    public <T> T runExclusive(java.util.function.Supplier<T> fn) {
        lock.lock();
        try {
            return fn.get();
        } finally {
            lock.unlock();
        }
    }

    public void runExclusive(Runnable fn) {
        lock.lock();
        try {
            fn.run();
        } finally {
            lock.unlock();
        }
    }
}
