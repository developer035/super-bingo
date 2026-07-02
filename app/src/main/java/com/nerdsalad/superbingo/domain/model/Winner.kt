package com.nerdsalad.superbingo.domain.model

data class Winner(
    val id: Long = 0,
    val matchId: Long,
    val prizeCategoryId: Long,
    val playerId: Long,
    val prizeAmount: Double,
    val recordedAt: Long = System.currentTimeMillis(),
    val playerName: String = "",
    val categoryName: String = ""
)
