package com.aadityaverma.remotify.data.repository

import com.aadityaverma.remotify.data.datasource.Track
import com.aadityaverma.remotify.data.datasource.UnifiedTrack

interface Player {
    fun playTrack(track: UnifiedTrack)


}