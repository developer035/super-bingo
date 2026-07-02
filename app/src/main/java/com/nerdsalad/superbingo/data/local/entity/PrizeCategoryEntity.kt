package com.nerdsalad.superbingo.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nerdsalad.superbingo.domain.model.PrizeCategory

@Entity(tableName = "prize_categories", indices = [Index("matchId")])
data class PrizeCategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matchId: Long,
    val name: String,
    val amount: Double,
    val sortOrder: Int
)

fun PrizeCategoryEntity.toDomain() = PrizeCategory(id, matchId, name, amount, sortOrder)
fun PrizeCategory.toEntity() = PrizeCategoryEntity(id, matchId, name, amount, sortOrder)
