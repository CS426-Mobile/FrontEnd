package com.example.bookstore

sealed class Screen(val route: String){
    object Home: Screen(route = "home_screen")
    object Filter: Screen(route = "filter_screen")
    object Detail: Screen(route = "detail_screen")

}