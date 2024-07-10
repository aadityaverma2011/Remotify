package com.aadityaverma.remotify.domain.repository

import androidx.paging.PagingData
import com.spotify.protocol.types.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    fun searchMusic(searchQuery: String): Flow<PagingData<Track>>
}