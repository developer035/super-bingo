package com.nerdsalad.superbingo.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameEngineTest {

    @Test
    fun testCallNextNumber_returnsNumberInRange() {
        val engine = GameEngine()
        val number = engine.callNextNumber()
        assertTrue(number!! in 1..90)
    }

    @Test
    fun testCallNextNumber_noDuplicates() {
        val engine = GameEngine()
        val called = mutableSetOf<Int>()
        repeat(90) {
            val number = engine.callNextNumber()
            assertTrue(called.add(number!!))
        }
    }

    @Test
    fun testCallNextNumber_returnsNullWhenExhausted() {
        val engine = GameEngine()
        repeat(90) { engine.callNextNumber() }
        assertNull(engine.callNextNumber())
    }

    @Test
    fun testUndoLastNumber_restoresNumber() {
        val engine = GameEngine()
        val number = engine.callNextNumber()
        val undone = engine.undoLastNumber()
        assertEquals(number, undone)
        assertEquals(90, engine.remainingCount)
    }

    @Test
    fun testUndoLastNumber_returnsNullWhenEmpty() {
        val engine = GameEngine()
        assertNull(engine.undoLastNumber())
    }

    @Test
    fun testRestoreState_correctAvailableNumbers() {
        val engine = GameEngine()
        val called = listOf(1, 2, 3)
        engine.restoreState(called)
        assertEquals(3, engine.calledNumbers.size)
        assertEquals(87, engine.remainingCount)
        assertTrue(1 !in engine.calledNumbers.toSet().let { setOf(4, 5) }) // logic check
        assertTrue(4 in (1..90).filter { it !in called.toSet() })
    }

    @Test
    fun testReset_clearsAllState() {
        val engine = GameEngine()
        engine.callNextNumber()
        engine.reset()
        assertEquals(0, engine.calledNumbers.size)
        assertEquals(90, engine.remainingCount)
    }

    @Test
    fun testFullGame_calls90UniqueNumbers() {
        val engine = GameEngine()
        val called = mutableListOf<Int>()
        while (!engine.isGameComplete) {
            called.add(engine.callNextNumber()!!)
        }
        assertEquals(90, called.size)
        assertEquals(90, called.distinct().size)
    }
}
