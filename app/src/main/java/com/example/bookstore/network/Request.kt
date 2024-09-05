// Request.kt
package com.example.bookstore.network

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
    val user_email: String,
    val address: String
)

data class ChangePasswordRequest(
    val old_password: String,
    val new_password: String,
    val new_password2: String
)
