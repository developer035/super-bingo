package com.nerdsalad.superbingo.util

import javax.inject.Inject

interface SoundEffectPlayer {
    fun playNumberCalled()
    fun playWinnerRecorded()
    fun playGameComplete()
}

class NoOpSoundEffectPlayer @Inject constructor() : SoundEffectPlayer {
    override fun playNumberCalled() {}
    override fun playWinnerRecorded() {}
    override fun playGameComplete() {}
}
