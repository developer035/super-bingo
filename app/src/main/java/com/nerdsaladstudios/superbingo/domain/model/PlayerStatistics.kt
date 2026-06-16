package com.nerdsaladstudios.superbingo.domain.model

data class PlayerStatistics(
    val player: Player,
    val gamesPlayed: Int = 0,
    val totalWins: Int = 0,
    val totalPrizeMoney: Double = 0.0,
    val cornersWon: Int = 0,
    val linesWon: Int = 0,
    val housesWon: Int = 0,
    val laddusWon: Int = 0,
    val bpWon: Int = 0,
    val lastWinDate: Long? = null
)
