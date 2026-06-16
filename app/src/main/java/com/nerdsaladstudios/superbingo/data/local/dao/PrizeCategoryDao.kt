package com.nerdsaladstudios.superbingo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nerdsaladstudios.superbingo.data.local.entity.PrizeCategoryEntity

@Dao
interface PrizeCategoryDao {
    @Query("SELECT * FROM prize_categories WHERE matchId = :matchId ORDER BY sortOrder ASC")
    suspend fun getForMatch(matchId: Long): List<PrizeCategoryEntity>

    @Insert
    suspend fun insertAll(categories: List<PrizeCategoryEntity>)

    @Insert
    suspend fun insert(category: PrizeCategoryEntity): Long

    @Delete
    suspend fun delete(category: PrizeCategoryEntity)

    @Query("DELETE FROM prize_categories WHERE matchId = :matchId")
    suspend fun deleteForMatch(matchId: Long)
}
