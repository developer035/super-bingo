package com.nerdsaladstudios.superbingo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nerdsaladstudios.superbingo.data.local.entity.WinnerEntity

@Dao
interface WinnerDao {
    @Query("SELECT * FROM winners WHERE matchId = :matchId")
    suspend fun getForMatch(matchId: Long): List<WinnerEntity>

    @Query("SELECT * FROM winners WHERE playerId = :playerId")
    suspend fun getForPlayer(playerId: Long): List<WinnerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(winner: WinnerEntity): Long

    @Query("DELETE FROM winners WHERE matchId = :matchId")
    suspend fun deleteForMatch(matchId: Long)
}
