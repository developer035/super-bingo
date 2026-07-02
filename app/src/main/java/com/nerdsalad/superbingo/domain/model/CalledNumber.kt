package com.nerdsalad.superbingo.domain.model

data class CalledNumber(
    val id: Long = 0,
    val matchId: Long,
    val number: Int,
    val calledAt: Long = System.currentTimeMillis(),
    val callOrder: Int
)
