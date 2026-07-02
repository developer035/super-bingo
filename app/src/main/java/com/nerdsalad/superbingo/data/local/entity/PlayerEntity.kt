package com.nerdsalad.superbingo.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nerdsalad.superbingo.domain.model.Player

@Entity(tableName = "players", indices = [Index(value = ["name"], unique = true)])
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val createdAt: Long
)

fun PlayerEntity.toDomain() = Player(id, name, createdAt)
fun Player.toEntity() = PlayerEntity(id, name, createdAt)
