package com.aadityaverma.remotify.presentation.components

import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aadityaverma.remotify.data.datasource.Track
import com.aadityaverma.remotify.data.datasource.UnifiedTrack
import com.aadityaverma.remotify.presentation.Dimens.TrackCardSize
import com.spotify.protocol.types.Artist
import com.spotify.protocol.types.ImageUri




@Composable
fun TrackCard(
    modifier: Modifier= Modifier,
    track: UnifiedTrack,
    onClick:(() -> Unit)? = null
){
    val context= LocalContext.current
    Row (
        modifier= Modifier.clickable { onClick?.invoke() },
    ){
        AsyncImage(
            modifier = Modifier
                .size(56.dp)
                .clip(MaterialTheme.shapes.small),
            model= ImageRequest.Builder(context).data(track.albumArtUrl).build(),
            contentDescription= null,
            contentScale= ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.weight(1f)
            ) {

            Text(text = track.name, maxLines = 1 , style= MaterialTheme.typography.titleMedium)
            Text(text = track.artists.first(), maxLines = 1 , style= MaterialTheme.typography.labelSmall)

        }

    }
}


//@Preview
//@Composable
//fun TrackCardPreview(){
//    TrackCard(track = Track(Artist("Seedhe Maut"," "), null, null , 0L,"Namastute"," ", ImageUri("https://i.scdn.co/image/ab67616d00004851d65e2670b7176415b9d88a59"),false,false  )
//    )
//}