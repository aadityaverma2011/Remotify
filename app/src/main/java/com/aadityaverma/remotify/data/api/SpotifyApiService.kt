package com.aadityaverma.remotify.data.api

import com.aadityaverma.remotify.data.SpotifySearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApiService {
    @GET("v1/search")
    fun searchTracks(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("type") type: String = "track"
    ): Call<SpotifySearchResponse>
}