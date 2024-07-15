package com.aadityaverma.remotify.data.datasource

data class Image(
    val height: Int,
    val url: String,
    val width: Int
)

data class Album(
    val name: String,
    val uri: String,
    val images: List<Image>
)

data class Artist(
    val name: String,
    val uri: String
)

data class Track(
    val name: String,
    val uri: String,
    val artists: List<Artist>,
    val album: Album
)
data class Tracks(
    val items: List<Track>
)

