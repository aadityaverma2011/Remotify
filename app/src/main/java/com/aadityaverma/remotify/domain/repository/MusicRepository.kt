package com.aadityaverma.remotify.domain.repository

import androidx.paging.PagingData
import com.aadityaverma.remotify.data.datasource.Track

import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    fun searchMusic(searchQuery: String): Flow<PagingData<Track>>
}