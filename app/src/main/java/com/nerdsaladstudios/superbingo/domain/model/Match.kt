package com.nerdsaladstudios.superbingo.domain.model

data class Match(
    val id: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val totalPotAmount: Double,
    val playerIds: List<Long>,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)
