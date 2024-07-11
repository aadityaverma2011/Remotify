package com.aadityaverma.remotify.data.repository

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aadityaverma.remotify.data.SearchMusicPagingSource
import com.aadityaverma.remotify.data.api.SpotifyApiService
import com.aadityaverma.remotify.data.datasource.Track
import com.aadityaverma.remotify.domain.repository.MusicRepository

import kotlinx.coroutines.flow.Flow

class MusicRepositoryImpl(private val musicApi: SpotifyApiService, private val sharedPreferences: SharedPreferences):MusicRepository {
    override fun searchMusic(searchQuery: String): Flow<PagingData<Track>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
        ),pagingSourceFactory = {
            SearchMusicPagingSource(musicApi, searchQuery,sharedPreferences)
            }
        ).flow
    }

}