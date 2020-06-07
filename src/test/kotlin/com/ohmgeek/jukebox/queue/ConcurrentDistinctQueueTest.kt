package com.ohmgeek.jukebox.queue

import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal class ConcurrentDistinctQueueTest {
    private val sut = ConcurrentDistinctQueue<String>()

    @Test
    fun canAddElement() {
        // Given a set of strings
        val elem1 = "A"
        val elem2 = "B"
        val elem3 = "C"

        // When we add them to the queue in order
        sut.add(elem1)
        sut.add(elem2)
        sut.add(elem3)

        // Then size should be three
        awaitUntilSizeIs(3)

        // Then Peek should display first element only
        assertEquals(elem1, sut.peek())

        // Then Pop should remove
        assertEquals(elem1, sut.pop())
        assertEquals(elem2, sut.pop())
        assertEquals(elem3, sut.pop())
    }

    @Test
    fun shouldAddElementOnceWhenAddingDuplicates() {
        // Given a single element
        val elem = "A"

        // When we add several of the same elements at once
        val threadPool = Executors.newFixedThreadPool(3)

        threadPool.submit { sut.add(elem) }
        threadPool.submit { sut.add(elem) }
        threadPool.submit { sut.add(elem) }

        // Then await for consistency (up to 3 seconds)
        awaitUntilSizeIs(1)

        // Verify only one element exists, and this is the the one added
        assertEquals(sut.peek(), elem)
        assertEquals(sut.size, 1)
    }

    @Test
    fun canIterateOverEntireQueue() {
        // Given a set of strings
        val elements = listOf("A", "B", "C")

        // When we add them to the queue in order
        sut.add(elements[0])
        sut.add(elements[1])
        sut.add(elements[2])

        awaitUntilSizeIs(3)

        val iterator = sut.iterator()
        for (elem in elements) {
            assertEquals(elem, iterator.next())
        }
    }

    private fun awaitUntilSizeIs(value: Int) {
        await().dontCatchUncaughtExceptions().timeout(3, TimeUnit.SECONDS)
                .until { sut.size == value }

        // First, verify there are three items
        assertEquals(value, sut.size)
    }
}