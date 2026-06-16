package com.nerdsaladstudios.superbingo.domain.model

data class Player(
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)
