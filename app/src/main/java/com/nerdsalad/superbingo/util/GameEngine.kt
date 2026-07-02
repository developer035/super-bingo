package com.nerdsalad.superbingo.util

class GameEngine {
    private val _calledNumbers = mutableListOf<Int>()
    private val _availableNumbers = (1..90).toMutableList().also { it.shuffle() }

    val calledNumbers: List<Int> get() = _calledNumbers.toList()
    val currentNumber: Int? get() = _calledNumbers.lastOrNull()
    val previousNumber: Int? get() = if (_calledNumbers.size >= 2) _calledNumbers[_calledNumbers.size - 2] else null
    val remainingCount: Int get() = _availableNumbers.size
    val isGameComplete: Boolean get() = _availableNumbers.isEmpty()

    fun callNextNumber(): Int? {
        if (_availableNumbers.isEmpty()) return null
        val number = _availableNumbers.removeAt(0)
        _calledNumbers.add(number)
        return number
    }

    fun undoLastNumber(): Int? {
        if (_calledNumbers.isEmpty()) return null
        val last = _calledNumbers.removeAt(_calledNumbers.size - 1)
        _availableNumbers.add(last)
        _availableNumbers.sort()
        return last
    }

    fun showAds(type: String){

    }

    fun restoreState(called: List<Int>) {
        _calledNumbers.clear()
        _calledNumbers.addAll(called)
        _availableNumbers.clear()
        _availableNumbers.addAll((1..90).filter { it !in called.toSet() })
        // Note: shuffle _availableNumbers if you want the next call to be random again
        _availableNumbers.shuffle()
    }

    fun reset() {
        _calledNumbers.clear()
        _availableNumbers.clear()
        _availableNumbers.addAll((1..90).toList().shuffled())
    }
}
