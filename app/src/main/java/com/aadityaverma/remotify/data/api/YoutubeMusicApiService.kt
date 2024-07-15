package com.aadityaverma.remotify.data.api


import com.aadityaverma.remotify.data.datasource.SearchResult

import com.aadityaverma.remotify.data.datasource.YtMusicResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeMusicApiService {
    @GET("search")
    fun search(@Query("Query") query: String): Call<YtMusicResponse>
}