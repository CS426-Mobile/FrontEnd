// Request.kt
package com.example.bookstore.model

// User Request
data class LoginRequest(
    val user_email: String,
    val password: String
)

data class RegisterRequest(
    val user_email: String,
    val password: String,
    val password2: String
)

data class GetUserInfoRequest(
    val user_email: String
)

data class UpdateAddressRequest(
    val address: String
)

data class UpdateAddressWithEmailRequest(
    val user_email: String,
    val address: String
)

data class ChangePasswordRequest(
    val old_password: String,
    val new_password: String,
    val new_password2: String
)

data class LogoutRequest(val user_email: String)

// Author Request
data class AuthorRequest(
    val author_name: String,
    val num_follower: Int,
    val about: String,
    val author_image: String
)

// Book Request
data class BookCategoryRequest(
    val category_name: String = "",
    val rating_optional: String = "all",
    val price_optional: String = "no",
    val price_min: Double = 0.0,
    val price_max: Double = 99999999.0,
    val rating_sort: String = "none",
    val price_sort: String = "none"
)

// Customer Favorite Request
data class CustomerFavoriteRequest(
    val user_email: String,
    val book_name: String
)

// Customer Cart Request
data class CustomerCartRequest(
    val user_email: String,
    val book_name: String
)

// Customer Follow Request
data class CustomerFollowRequest(
    val author_name: String,
    val user_email: String
)
