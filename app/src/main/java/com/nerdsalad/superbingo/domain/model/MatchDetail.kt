package com.nerdsalad.superbingo.domain.model

data class MatchDetail(
    val match: Match,
    val players: List<Player>,
    val prizeCategories: List<PrizeCategory>,
    val winners: List<Winner>,
    val calledNumbers: List<CalledNumber>
)
