package com.example.bookstore

sealed class Screen(val route: String){
    object Home: Screen(route = "home_screen")
}