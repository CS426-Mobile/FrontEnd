package com.example.bookstore

sealed class Screen(val route: String){
    object Home: Screen(route = "home_screen")
    object Account: Screen(route = "account_screen")
    object Cart: Screen(route = "cart_screen")
    object ListOrder: Screen(route = "listOrder_screen")
    object Favorite: Screen(route = "favorite_screen")
    object ListAuthor: Screen(route = "listAuthor_screen")
    object Search: Screen(route = "search_screen")
    object Success: Screen(route = "success_screen")

    object Author: Screen(route = "author_screen/{author_name}") {
        fun passAuthorName(authorName: String): String {
            return "author_screen/$authorName"
        }
    }
    object Book: Screen(route = "book_screen/{book_name}") {
        fun passBookName(bookName: String): String {
            return "book_screen/$bookName"
        }
    }
    object Order: Screen(route = "order_screen/{orderID}") {
        fun passOrderID(orderID: String): String {
            return "order_screen/$orderID"
        }
    }
}