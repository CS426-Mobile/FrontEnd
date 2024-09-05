// Response.kt
package com.example.bookstore.network

// User Response
data class LoginResponse(val message: String)

data class RegisterResponse(val message: String)

data class UserInfoResponse(
    val user: UserInfo
)

data class UserInfo(
    val user_email: String,
    val address: String
)

data class UpdateAddressResponse(val message: String)

data class GetAddressResponse(val address: String)

data class ChangePasswordResponse(val message: String)

data class LogoutResponse(val message: String)

// Author Response
data class AuthorResponse(
    val author_name: String,
    val num_follower: Int,
    val about: String,
    val author_image: String
)

data class SimpleAuthorResponse(
    val author_name: String,
    val author_image: String
)



