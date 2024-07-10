package com.aadityaverma.remotify.presentation.search

sealed class SearchEvent {
    data class UpdateSearchQuery(val query: String) : SearchEvent()
    object SearchMusic : SearchEvent()
}