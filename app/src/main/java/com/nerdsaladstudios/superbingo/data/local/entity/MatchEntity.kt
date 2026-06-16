package com.nerdsaladstudios.superbingo.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nerdsaladstudios.superbingo.domain.model.Match

@Entity(tableName = "matches", indices = [Index("createdAt")])
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val createdAt: Long,
    val totalPotAmount: Double,
    val playerIds: String,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)

fun MatchEntity.toDomain(): Match {
    val ids = if (playerIds.isBlank()) emptyList() else playerIds.split(",").mapNotNull { it.toLongOrNull() }
    return Match(
        id = id,
        createdAt = createdAt,
        totalPotAmount = totalPotAmount,
        playerIds = ids,
        isCompleted = isCompleted,
        completedAt = completedAt
    )
}

fun Match.toEntity() = MatchEntity(
    id = id,
    createdAt = createdAt,
    totalPotAmount = totalPotAmount,
    playerIds = playerIds.joinToString(","),
    isCompleted = isCompleted,
    completedAt = completedAt
)
