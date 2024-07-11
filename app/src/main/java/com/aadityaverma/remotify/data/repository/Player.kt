package com.aadityaverma.remotify.data.repository

import com.aadityaverma.remotify.data.datasource.Track

interface Player {
    fun playTrack(track: Track)


}