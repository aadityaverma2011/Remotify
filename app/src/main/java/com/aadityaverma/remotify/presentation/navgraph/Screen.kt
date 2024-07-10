package com.aadityaverma.remotify.presentation.navgraph

sealed class Screen (val route: String){
    object SearchScreen: Screen("search_screen")
}