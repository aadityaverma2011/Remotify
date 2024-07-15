package com.aadityaverma.remotify

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph
import androidx.navigation.compose.rememberNavController
import com.aadityaverma.remotify.data.SpotifySearchResponse
import com.aadityaverma.remotify.data.api.SpotifyApiService
import com.aadityaverma.remotify.data.datasource.SearchResult

import com.aadityaverma.remotify.data.datasource.YtMusicResponse
import com.aadityaverma.remotify.data.repository.Player
import com.aadityaverma.remotify.di.AppModule.provideMusic2Api
import com.aadityaverma.remotify.di.AppModule.provideMusicApi
//import com.aadityaverma.remotify.data.datasource.RetrofitBuild
import com.aadityaverma.remotify.presentation.navgraph.Navigation

import com.aadityaverma.remotify.presentation.search.SearchViewModel
import com.aadityaverma.remotify.ui.theme.RemotifyTheme
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Query
import java.net.HttpURLConnection
import java.net.URL


private val clientId= "cfc7917d55a545c3b5ac2b350b77864f"
private val redirectURI= "remotify://callback"
var spotifyAppRemote: SpotifyAppRemote? = null
private var token: String? = null

@AndroidEntryPoint
class MainActivity : ComponentActivity(),Player {
    private lateinit var sharedPreferences: SharedPreferences

    private suspend fun connected(){
//        spotifyAppRemote?.let {
//            val playlistURi= "spotify:playlist:62qcRDHNodjIt93acbHFo3"
//            it.playerApi.play("spotify:track:5aScaOmYqiIOIbLS1ZuqHt")
//            it.playerApi.subscribeToPlayerState().setEventCallback {
//                val track: Track = it.track
//                Log.d("Main Activity", track.name + "by" + track.artist.name)
//            }
//        }
//        playTrack()
        val searchquery:String = "namastute seedhe maut"
//        searchTrack(searchquery, sharedPreferences.getString("access_token",null).toString())
        val newlogstring = getProfile(sharedPreferences.getString("access_token",null))
//        Log.d(TAG,"profilex"+ newlogstring )

        Log.d(TAG,"checkmark" )
        searchTrack2(searchquery)
    }


    private fun saveToken(accessToken: String?, TOKEN_KEY: String) {
        accessToken?.let {
            token = it
            with(sharedPreferences.edit()) {
                putString(TOKEN_KEY, it)
                apply()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("access_token", Context.MODE_PRIVATE)
        val request: AuthorizationRequest =
            AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectURI)
                .setScopes(arrayOf("app-remote-control","streaming"))
                .build()

        AuthorizationClient.openLoginActivity(this, 1337, request)
        enableEdgeToEdge()
        setContent {
            RemotifyTheme {
                        Navigation()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1337) {
            val response: AuthorizationResponse = AuthorizationClient.getResponse(resultCode, data)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    // do whatever you need with the access token `response.accessToken`



                    token=response.accessToken
                    saveToken(response.accessToken,"access_token")
                    Log.d(TAG,"i am so dumb" + sharedPreferences.getString("access_token", null))
                    // ADD YOUR CODE HERE and it should be able to play !
                }

                AuthorizationResponse.Type.ERROR -> {
                    Log.e(TAG, "Auth error : " + response.error)
                }

                else -> {
                    Log.e(TAG, "Auth result: " + response.type)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectURI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                CoroutineScope(Dispatchers.Main).launch {
                    connected()
                }
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }
    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }

    }

    suspend fun getProfile(accessToken: String?):String{
        return withContext(Dispatchers.IO){
            val url= URL("https://api.spotify.com/v1/me")
            val connection= url.openConnection() as HttpURLConnection
            connection.setRequestProperty("Authorization", "Bearer $accessToken")
            val inputStream = connection.inputStream
            val response = inputStream.bufferedReader().use { it.readText() }
            response
        }
    }
    fun searchTrack2(query: String) {
        val ytmusicApi = provideMusic2Api()
        val call = ytmusicApi.search(query)

        call.enqueue(object : Callback<YtMusicResponse> {
            override fun onResponse(call: Call<YtMusicResponse>, response: Response<YtMusicResponse>) {
                if (response.isSuccessful) {
                    val ytMusicResponse = response.body()
                    val tracks = ytMusicResponse?.searchResults ?: emptyList()

                    tracks.forEach { track ->
                        println("Track ID: ${track.videoId ?: "Unknown"}")
                        println("Track Name: ${track.title ?: "Unknown"}")
                        val artists = track.artists?.joinToString { it.name ?: "Unknown" } ?: "Unknown"
                        println("Artists: $artists")
                        val thumbnails = track.thumbnails?.joinToString { it.url ?: "Unknown" } ?: "Unknown"
                        println("Thumbnails: $thumbnails")
                        println("-----")
                    }
                } else {
                    println("Error: ${response.code()}")
                    // Handle specific error codes if needed
                }
            }

            override fun onFailure(call: Call<YtMusicResponse>, t: Throwable) {
                println("Failure: ${t.message}")
                // Handle failure scenario, e.g., retry logic, error messages
            }
        })
    }



    fun searchTrack(query: String, token: String) {
        val spotifyApiService = provideMusicApi()
        val call = spotifyApiService.searchTracks("Bearer $token", query)
        call.enqueue(object : retrofit2.Callback<SpotifySearchResponse> {
            override fun onResponse(
                call: retrofit2.Call<SpotifySearchResponse>,
                response: retrofit2.Response<SpotifySearchResponse>
            ) {
                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.toString() // or use the appropriate method to get the raw JSON string
                    println("Raw JSON response: $jsonResponse")
                    val tracks = response.body()?.tracks?.items ?: emptyList()
                    tracks.forEach { track ->
                        println("Track ID: ${track.uri}")
                        track.album.images.firstOrNull()?.let {
                            println("Image: ${it.url}")
                        } ?: println("Image: No image available")
                        println("Track Name: ${track.name}")
                        println("Artists: ${track.artists.joinToString { it.name }}")
                        println("Album: ${track.album.name}")
                        println("-----")
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<SpotifySearchResponse>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })
    }

    override fun playTrack(track: com.aadityaverma.remotify.data.datasource.UnifiedTrack) {
        spotifyAppRemote?.let {
            it.playerApi.play(track.uri)
        }
    }
}

