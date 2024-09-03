package com.example.bookstore

sealed class Screen(val route: String){
    object Home: Screen(route = "home_screen")
    object Filter: Screen(route = "filter_screen")
    object Author: Screen(route = "author_screen")
    object Book: Screen(route = "book_screen")
    object Cart: Screen(route = "cart_screen")
}