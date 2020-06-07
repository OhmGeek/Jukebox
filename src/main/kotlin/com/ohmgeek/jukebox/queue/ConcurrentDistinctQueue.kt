package com.ohmgeek.jukebox.queue

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors

class ConcurrentDistinctQueue<T> {
    private val delegate = ConcurrentLinkedQueue<T>()
    private val executor = Executors.newSingleThreadExecutor()

    /**
     * Add an element to the queue if it isn't already present.
     *
     * Note: this will execute asynchronously, so the element won't immediately be in the
     * queue after adding (you'll need to wait).
     *
     * @param element : the element to add to the queue
     */
    fun add(element: T) {
        // Here, we submit the addition to a thread pool.
        // This will execute the addition and validation in a single block
        // yielding atomicity.
        executor.submit {
            if (element !in delegate) {
                delegate.add(element)
            }
        }
    }

    /**
     * Get and remove the item at the top of the queue.
     *
     * @return element on the head of the queue
     */
    fun pop(): T {
        return delegate.remove()
    }

    /**
     * Remove a specific element from the queue.
     *
     * @param element : the item to remove
     */
    fun remove(element: T) {
        delegate.run { remove(element) }
    }

    operator fun iterator(): MutableIterator<T> {
        return delegate.iterator()
    }

    /**
     * Return the element at the top of the queue, but don't remove it.
     */
    fun peek(): T {
        return delegate.peek()
    }

    /**
     * Get the number of unique items currently in the queue (not including values that are pending validation)
     */
    val size: Int
        get() = delegate.size
}