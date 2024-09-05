// Response.kt
package com.example.bookstore.network

data class LoginResponse(val message: String)

data class RegisterResponse(val message: String)

data class UserInfoResponse(
    val user: UserInfo
)

data class UserInfo(
    val user_email: String,
    val address: String
)

data class LogoutResponse(val message: String)