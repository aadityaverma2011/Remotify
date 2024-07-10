package com.aadityaverma.remotify.di

import android.app.Application
import android.content.SharedPreferences
import com.aadityaverma.remotify.data.api.SpotifyApiService
import com.aadityaverma.remotify.data.repository.MusicRepositoryImpl
import com.aadityaverma.remotify.domain.repository.MusicRepository
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
   fun provideMusicRepository(
       musicApi: SpotifyApiService,
       sharedPreferences: SharedPreferences
   ): MusicRepository{
       return MusicRepositoryImpl(musicApi,sharedPreferences)
   }

}