package com.nerdsaladstudios.superbingo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nerdsaladstudios.superbingo.data.local.entity.CalledNumberEntity

@Dao
interface CalledNumberDao {
    @Query("SELECT * FROM called_numbers WHERE matchId = :matchId ORDER BY callOrder ASC")
    suspend fun getForMatch(matchId: Long): List<CalledNumberEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(number: CalledNumberEntity): Long

    @Query("DELETE FROM called_numbers WHERE matchId = :matchId AND callOrder = (SELECT MAX(callOrder) FROM called_numbers WHERE matchId = :matchId)")
    suspend fun deleteLastForMatch(matchId: Long)

    @Query("SELECT number FROM called_numbers WHERE matchId = :matchId ORDER BY callOrder DESC LIMIT 1")
    suspend fun getLastNumber(matchId: Long): Int?

    @Query("DELETE FROM called_numbers WHERE matchId = :matchId")
    suspend fun deleteAllForMatch(matchId: Long)
}
