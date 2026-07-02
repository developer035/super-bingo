package com.nerdsalad.superbingo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nerdsalad.superbingo.data.local.converter.Converters
import com.nerdsalad.superbingo.data.local.dao.CalledNumberDao
import com.nerdsalad.superbingo.data.local.dao.MatchDao
import com.nerdsalad.superbingo.data.local.dao.PlayerDao
import com.nerdsalad.superbingo.data.local.dao.PrizeCategoryDao
import com.nerdsalad.superbingo.data.local.dao.WinnerDao
import com.nerdsalad.superbingo.data.local.entity.CalledNumberEntity
import com.nerdsalad.superbingo.data.local.entity.MatchEntity
import com.nerdsalad.superbingo.data.local.entity.PlayerEntity
import com.nerdsalad.superbingo.data.local.entity.PrizeCategoryEntity
import com.nerdsalad.superbingo.data.local.entity.WinnerEntity

@Database(
    entities = [
        PlayerEntity::class,
        MatchEntity::class,
        PrizeCategoryEntity::class,
        WinnerEntity::class,
        CalledNumberEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun matchDao(): MatchDao
    abstract fun prizeCategoryDao(): PrizeCategoryDao
    abstract fun winnerDao(): WinnerDao
    abstract fun calledNumberDao(): CalledNumberDao
}
