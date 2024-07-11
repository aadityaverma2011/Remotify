package com.aadityaverma.remotify.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.aadityaverma.remotify.data.repository.Player
import com.aadityaverma.remotify.presentation.Dimens.MediumPadding1
import com.aadityaverma.remotify.presentation.common.TrackList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    state: SearchState,
    event: (SearchEvent) -> Unit,

) {

    val isActive = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(
                top = MediumPadding1,
                start = MediumPadding1,
                end = MediumPadding1
            )
            .statusBarsPadding()
    ) {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { event(SearchEvent.UpdateSearchQuery(it)) },
            onSearch = {  event(SearchEvent.SearchMusic)},
            active = isActive.value,
            onActiveChange = { isActive.value = it },
            placeholder = {
                Text(
                    text = "enter your search here"
                )
            }
        ){
            null
        }
        Spacer(modifier = Modifier.height(MediumPadding1))
        state.tracks?.let {
            val tracks = it.collectAsLazyPagingItems()
            TrackList(
                tracks = tracks,
                callback = (LocalContext.current as Player), // Cast context to Player interface
                onClick = {
                    // Handle click event if needed
                }
            )

        }
    }
}