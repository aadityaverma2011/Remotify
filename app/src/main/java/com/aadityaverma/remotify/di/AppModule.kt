package com.aadityaverma.remotify.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.aadityaverma.remotify.data.api.SpotifyApiService
import com.aadityaverma.remotify.data.repository.MusicRepositoryImpl
import com.aadityaverma.remotify.domain.repository.MusicRepository
import com.aadityaverma.remotify.domain.usecases.Musicusecases
import com.aadityaverma.remotify.domain.usecases.SearchMusic
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMusicApi(): SpotifyApiService {
        return Retrofit.Builder().baseUrl("https://api.spotify.com/").addConverterFactory(GsonConverterFactory.create()).build().create(SpotifyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("access_token", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
   fun provideMusicRepository(
       musicApi: SpotifyApiService,
       sharedPreferences: SharedPreferences
   ): MusicRepository{
       return MusicRepositoryImpl(musicApi,sharedPreferences)
   }

    @Provides
    @Singleton
    fun providesMusicUseCases(
        musicRepository: MusicRepository
    ): Musicusecases{
        return Musicusecases(
            searchMusic = SearchMusic(musicRepository)
        )
    }

}