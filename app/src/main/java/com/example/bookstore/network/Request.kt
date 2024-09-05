// Request.kt
package com.example.bookstore.network

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