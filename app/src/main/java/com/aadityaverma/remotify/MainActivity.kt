package com.aadityaverma.remotify

import android.content.Intent
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
import com.aadityaverma.remotify.ui.theme.RemotifyTheme
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


private val clientId= "cfc7917d55a545c3b5ac2b350b77864f"
private val redirectURI= "remotify://callback"
private var spotifyAppRemote: SpotifyAppRemote? = null
class MainActivity : ComponentActivity() {


    private fun connected(){
        spotifyAppRemote?.let {
            val playlistURi= "spotify:playlist:62qcRDHNodjIt93acbHFo3"
            it.playerApi.play(playlistURi)
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d("Main Activity", track.name + "by" + track.artist.name)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val request: AuthorizationRequest =
            AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectURI)
                .setScopes(arrayOf("app-remote-control"))
                .build()

        AuthorizationClient.openLoginActivity(this, 1337, request)
        enableEdgeToEdge()
        setContent {
            RemotifyTheme {

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
                        connected()
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
                connected()
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
}

