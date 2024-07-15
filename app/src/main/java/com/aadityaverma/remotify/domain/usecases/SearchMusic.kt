package com.aadityaverma.remotify.domain.usecases

import androidx.paging.PagingData
import com.aadityaverma.remotify.data.datasource.Track
import com.aadityaverma.remotify.data.datasource.UnifiedTrack
import com.aadityaverma.remotify.domain.repository.MusicRepository

import kotlinx.coroutines.flow.Flow

class SearchMusic(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(searchquery: String): Flow<PagingData<UnifiedTrack>> {
        return musicRepository.searchMusic(searchquery)
    }

}