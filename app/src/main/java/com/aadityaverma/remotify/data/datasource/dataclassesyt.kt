package com.aadityaverma.remotify.data.datasource

import com.google.gson.annotations.SerializedName

data class YtMusicResponse(
    @SerializedName("results")
    val searchResults: List<SearchResult>
)

data class SearchResult(
    @SerializedName("videoId")
    val videoId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("artists")
    val artists: List<Artist>,
    @SerializedName("thumbnails")
    val thumbnails: List<Thumbnail>
)



data class Thumbnail(
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
)




