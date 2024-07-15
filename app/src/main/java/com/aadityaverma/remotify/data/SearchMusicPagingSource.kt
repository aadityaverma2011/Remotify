package com.aadityaverma.remotify.data

import android.content.SharedPreferences
import android.service.controls.ControlsProviderService.TAG
import android.util.Log

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aadityaverma.remotify.data.api.SpotifyApiService
import com.aadityaverma.remotify.data.api.YoutubeMusicApiService
import com.aadityaverma.remotify.data.datasource.Track
import com.aadityaverma.remotify.data.datasource.UnifiedTrack

import retrofit2.Call
import retrofit2.awaitResponse
import retrofit2.http.Query




class SearchMusicPagingSource(
    private val spotifyApi: SpotifyApiService,
    private val youtubeApi: YoutubeMusicApiService,
    private val searchQuery: String,
    private val sharedPreferences: SharedPreferences
) : PagingSource<Int, UnifiedTrack>() {

    override fun getRefreshKey(state: PagingState<Int, UnifiedTrack>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnifiedTrack> {
        val position = params.key ?: 1
        val pageSize = params.loadSize

        return try {
            val token = sharedPreferences.getString("access_token", null)
            Log.d(TAG, "Access token: $token")

            val spotifyResponse = spotifyApi.searchTracks("Bearer $token", searchQuery, "track").awaitResponse()
            val youtubeResponse = youtubeApi.search(searchQuery).awaitResponse()

            Log.d(TAG, "Spotify response: ${spotifyResponse.code()} ${spotifyResponse.message()}")
            Log.d(TAG, "YouTube response: ${youtubeResponse.code()} ${youtubeResponse.message()}")

            if (spotifyResponse.isSuccessful && youtubeResponse.isSuccessful) {
                val spotifyTracks = spotifyResponse.body()?.tracks?.items ?: emptyList()
                val youtubeTracks = youtubeResponse.body()?.searchResults ?: emptyList()

                Log.d(TAG, "Spotify tracks count: ${spotifyTracks.size}")
                Log.d(TAG, "YouTube tracks count: ${youtubeTracks.size}")

                val combinedTracks = mutableListOf<UnifiedTrack>()

                combinedTracks.addAll(spotifyTracks.map { spotifyTrack ->
                    UnifiedTrack(
                        name = spotifyTrack.name,
                        uri = spotifyTrack.uri,
                        artists = spotifyTrack.artists.map { it.name },
                        albumArtUrl = spotifyTrack.album.images.firstOrNull()?.url ?: "",
                        source = "Spotify"
                    )
                })

                combinedTracks.addAll(youtubeTracks.map { youtubeTrack ->
                    UnifiedTrack(
                        name = youtubeTrack.title?:"null",
                        uri = youtubeTrack.videoId ?: "null",
                        artists = youtubeTrack.artists?.mapNotNull { it.name } ?: listOf("Unknown Artist"),
                        albumArtUrl = youtubeTrack.thumbnails.firstOrNull()?.url ?: "",
                        source = "YouTube"
                    )
                })

                Log.d(TAG, "Combined tracks count: ${combinedTracks.size}")
                Log.d(TAG, "Last track source: ${combinedTracks.lastOrNull()?.source}")

                LoadResult.Page(
                    data = combinedTracks,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (combinedTracks.isEmpty()) null else position + 1
                )
            } else {
                Log.e(TAG, "Error: ${spotifyResponse.code()} ${spotifyResponse.message()} / ${youtubeResponse.code()} ${youtubeResponse.message()}")
                LoadResult.Error(Throwable("Error: ${spotifyResponse.code()} ${spotifyResponse.message()} / ${youtubeResponse.code()} ${youtubeResponse.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}", e)
            LoadResult.Error(e)
        }
    }
}