package com.nerdsaladstudios.superbingo.di

import com.nerdsaladstudios.superbingo.util.NoOpNumberAnnouncer
import com.nerdsaladstudios.superbingo.util.NoOpSoundEffectPlayer
import com.nerdsaladstudios.superbingo.util.NumberAnnouncer
import com.nerdsaladstudios.superbingo.util.SoundEffectPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNumberAnnouncer(): NumberAnnouncer = NoOpNumberAnnouncer()

    @Provides
    @Singleton
    fun provideSoundEffectPlayer(): SoundEffectPlayer = NoOpSoundEffectPlayer()
}
