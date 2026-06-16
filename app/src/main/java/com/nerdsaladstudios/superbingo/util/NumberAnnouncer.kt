package com.nerdsaladstudios.superbingo.util

import javax.inject.Inject

interface NumberAnnouncer {
    fun announce(number: Int)
    fun announceGameComplete()
    fun shutdown()
}

class NoOpNumberAnnouncer @Inject constructor() : NumberAnnouncer {
    override fun announce(number: Int) {}
    override fun announceGameComplete() {}
    override fun shutdown() {}
}
