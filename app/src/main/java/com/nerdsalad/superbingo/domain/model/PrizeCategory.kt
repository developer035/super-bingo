package com.nerdsalad.superbingo.domain.model

data class PrizeCategory(
    val id: Long = 0,
    val matchId: Long = 0,
    val name: String,
    val amount: Double,
    val sortOrder: Int = 0
)
