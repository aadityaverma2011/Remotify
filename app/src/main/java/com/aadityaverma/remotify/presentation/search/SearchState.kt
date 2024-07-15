package com.aadityaverma.remotify.presentation.search

import androidx.paging.PagingData
import com.aadityaverma.remotify.data.datasource.Track
import com.aadityaverma.remotify.data.datasource.UnifiedTrack

import kotlinx.coroutines.flow.Flow

data class SearchState(
    val searchQuery: String= "",
    val tracks: Flow<PagingData<UnifiedTrack>>?=null
)
