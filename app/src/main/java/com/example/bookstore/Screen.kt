package com.example.bookstore

sealed class Screen(val route: String){
    object Home: Screen(route = "home_screen")
    object Account: Screen(route = "account_screen")
    object Author: Screen(route = "author_screen")
    object Book: Screen(route = "book_screen")
    object Cart: Screen(route = "cart_screen")
    object ListOrder: Screen(route = "listOrder_screen")
    object Favorite: Screen(route = "favorite_screen")
    object ListAuthor: Screen(route = "listAuthor_screen")
    object Order: Screen(route = "order_screen")
    object Search: Screen(route = "search_screen")
    object Success: Screen(route = "success_screen")
}