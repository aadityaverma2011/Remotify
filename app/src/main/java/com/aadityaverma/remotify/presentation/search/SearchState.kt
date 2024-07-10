package com.aadityaverma.remotify.presentation.search

import androidx.paging.PagingData
import com.spotify.protocol.types.Track
import kotlinx.coroutines.flow.Flow

data class SearchState(
    val searchQuery: String= "",
    val tracks: Flow<PagingData<Track>>?=null
)
