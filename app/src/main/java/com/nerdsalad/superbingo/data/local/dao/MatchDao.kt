package com.nerdsalad.superbingo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nerdsalad.superbingo.data.local.entity.MatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY createdAt DESC")
    fun getAllMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :id")
    suspend fun getById(id: Long): MatchEntity?

    @Insert
    suspend fun insert(match: MatchEntity): Long

    @Update
    suspend fun update(match: MatchEntity)

    @Delete
    suspend fun delete(match: MatchEntity)

    @Query("SELECT * FROM matches WHERE (',' || playerIds || ',') LIKE '%,' || :playerId || ',%' AND isCompleted = 1")
    suspend fun getCompletedMatchesForPlayer(playerId: Long): List<MatchEntity>
}
