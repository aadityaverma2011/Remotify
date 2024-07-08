package com.aadityaverma.remotify.data

import com.spotify.protocol.types.Track

data class SpotifySearchResponse(
    val tracks: Tracks

)

data class Tracks(
    val items: List<Track>
)




