package com.aadityaverma.remotify.data

import android.content.SharedPreferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aadityaverma.remotify.data.api.SpotifyApiService
import com.spotify.protocol.types.Track
import retrofit2.Call
import retrofit2.awaitResponse
import retrofit2.http.Query




class SearchMusicPagingSource(
    private val api: SpotifyApiService,
    private val searchQuery: String,
    private val sharedPreferences: SharedPreferences

) :PagingSource<Int,Track>(){
    override fun getRefreshKey(state: PagingState<Int, Track>): Int? {
        return state.anchorPosition?.let { anchorPage ->
            val anchorPage = state.closestPageToPosition(anchorPage)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> {
            val position = params.key ?: 1
            val pageSize = params.loadSize

            return try {
                val token = sharedPreferences.getString("access_token", null)
                val response = api.searchTracks("Bearer $token", searchQuery, "track").awaitResponse()

                if (response.isSuccessful) {
                    val searchResponse = response.body()
                    val tracks = searchResponse?.tracks?.items ?: emptyList()

                    LoadResult.Page(
                        data = tracks,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = if (tracks.isEmpty()) null else position + 1
                    )
                } else {
                    LoadResult.Error(Throwable("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }


}