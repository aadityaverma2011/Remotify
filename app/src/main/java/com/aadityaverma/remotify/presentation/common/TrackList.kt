package com.aadityaverma.remotify.presentation.common

import android.content.Context
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems


import com.aadityaverma.remotify.data.datasource.Track
import com.aadityaverma.remotify.data.datasource.UnifiedTrack
import com.aadityaverma.remotify.data.repository.Player
import com.aadityaverma.remotify.presentation.Dimens.ExtraSmallPadding2
import com.aadityaverma.remotify.presentation.Dimens.MediumPadding1
import com.aadityaverma.remotify.presentation.components.TrackCard

import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


 var spotifyAppRemote: SpotifyAppRemote? = null
@Composable
fun handlePagingResult(tracks: LazyPagingItems<UnifiedTrack>): Boolean {
    val loadState = tracks.loadState
    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }
    return when {
        loadState.refresh is LoadState.Loading -> {
            // ShimmerEffect()
            false
        }
        else -> {
            true
        }
    }
}

@Composable
fun TrackList(
    modifier: Modifier = Modifier,
    tracks: LazyPagingItems<UnifiedTrack>,
    callback: Player,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val handlePagingResult = handlePagingResult(tracks = tracks)
    if (handlePagingResult) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
            contentPadding = PaddingValues(all = ExtraSmallPadding2)
        ) {
            items(tracks.itemCount) { index ->
                tracks[index]?.let {
                    TrackCard(
                        track = it,
                        onClick = {
                            // Call the function to play the track via Spotify remote player
//                            playTrackViaSpotifyRemote(it)
                            callback.playTrack(it)
                        }
                    )
                }
            }
        }
    }
}

fun playTrackViaSpotifyRemote(track: Track, context: Context) {


}


//@Composable
//fun handlePagingResult(tracks: LazyPagingItems<Track>): Boolean {
//    val loadState= tracks.loadState
//    val error= when{
//        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
//        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
//        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
//        else -> null
//    }
//    return when{
//        loadState.refresh is LoadState.Loading -> {
////            ShimmerEffect()
//            false
//        }
//
//        else -> {
//            true
//        }
//    }
//}

//@Composable
//fun ShimmerEffect() {
//    Column(verticalArrangement = Arrangement.spacedBy(MediumPadding1)){
//        repeat(10){
//            TrackCardShimmerEffect(
//                modifier = Modifier.padding(horizontal = MediumPadding1)
//            )
//        }
//    }
//}


