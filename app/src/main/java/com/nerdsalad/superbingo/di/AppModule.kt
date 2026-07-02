package com.nerdsalad.superbingo.di

import com.nerdsalad.superbingo.util.NoOpNumberAnnouncer
import com.nerdsalad.superbingo.util.NoOpSoundEffectPlayer
import com.nerdsalad.superbingo.util.NumberAnnouncer
import com.nerdsalad.superbingo.util.SoundEffectPlayer
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
