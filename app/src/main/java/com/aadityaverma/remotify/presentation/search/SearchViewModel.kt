package com.aadityaverma.remotify.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.aadityaverma.remotify.domain.repository.MusicRepository
import com.aadityaverma.remotify.domain.usecases.Musicusecases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val musicusecases: Musicusecases
): ViewModel() {
    private var _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.UpdateSearchQuery -> {
                _state.value = state.value.copy(searchQuery = event.query)
            }

            is SearchEvent.SearchMusic -> {
                searchMusic()
            }
        }
    }
    private fun searchMusic() {
        val tracks= musicusecases.searchMusic(state.value.searchQuery).cachedIn(viewModelScope)
        _state.value = state.value.copy(tracks = tracks)
    }

}