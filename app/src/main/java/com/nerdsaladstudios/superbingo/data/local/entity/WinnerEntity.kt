package com.nerdsaladstudios.superbingo.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nerdsaladstudios.superbingo.domain.model.Winner

@Entity(tableName = "winners", indices = [Index("matchId"), Index("playerId")])
data class WinnerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matchId: Long,
    val prizeCategoryId: Long,
    val playerId: Long,
    val prizeAmount: Double,
    val recordedAt: Long
)

fun WinnerEntity.toDomain(playerName: String = "", categoryName: String = "") = Winner(
    id = id,
    matchId = matchId,
    prizeCategoryId = prizeCategoryId,
    playerId = playerId,
    prizeAmount = prizeAmount,
    recordedAt = recordedAt,
    playerName = playerName,
    categoryName = categoryName
)

fun Winner.toEntity() = WinnerEntity(
    id = id,
    matchId = matchId,
    prizeCategoryId = prizeCategoryId,
    playerId = playerId,
    prizeAmount = prizeAmount,
    recordedAt = recordedAt
)
