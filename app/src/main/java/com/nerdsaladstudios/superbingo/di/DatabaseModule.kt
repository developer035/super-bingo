package com.nerdsaladstudios.superbingo.di

import android.content.Context
import androidx.room.Room
import com.nerdsaladstudios.superbingo.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "superbingo_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providePlayerDao(db: AppDatabase) = db.playerDao()

    @Provides
    fun provideMatchDao(db: AppDatabase) = db.matchDao()

    @Provides
    fun providePrizeCategoryDao(db: AppDatabase) = db.prizeCategoryDao()

    @Provides
    fun provideWinnerDao(db: AppDatabase) = db.winnerDao()

    @Provides
    fun provideCalledNumberDao(db: AppDatabase) = db.calledNumberDao()
}
