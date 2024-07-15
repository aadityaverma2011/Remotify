package com.aadityaverma.remotify.data.datasource



data class UnifiedTrack(
    val name: String,
    val uri: String,
    val artists: List<String>,
    val albumArtUrl: String,
    val source: String
)
