package com.nerdsalad.superbingo.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nerdsalad.superbingo.domain.model.CalledNumber

@Entity(
    tableName = "called_numbers",
    indices = [
        Index("matchId"),
        Index(value = ["matchId", "number"], unique = true)
    ]
)
data class CalledNumberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matchId: Long,
    val number: Int,
    val calledAt: Long,
    val callOrder: Int
)

fun CalledNumberEntity.toDomain() = CalledNumber(id, matchId, number, calledAt, callOrder)
fun CalledNumber.toEntity() = CalledNumberEntity(id, matchId, number, calledAt, callOrder)
