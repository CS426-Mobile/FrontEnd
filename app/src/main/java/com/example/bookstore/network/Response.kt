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

// Book Response
data class BookResponse(
    val book_name: String,
    val author_name: String,
    val book_image: String,
    val price: Double,
    val num_1_star: Int,
    val num_2_star: Int,
    val num_3_star: Int,
    val num_4_star: Int,
    val num_5_star: Int,
    val book_description: String,
    val public_date: String, // Date in "yyyy-MM-dd" format
    val book_language: String,
    val book_weight: Double,
    val book_page: Int,
    val average_rating: Double
)

data class SimpleBookResponse(
    val book_name: String,
    val author_name: String,
    val book_image: String,
    val average_rating: Float
)

data class BookCategoryResponse(
    val book_name: String,
    val author_name: String,
    val book_image: String
)

data class BookCountResponse(
    val num_books: Int
)

data class AuthorCategoriesResponse(
    val categories: List<String>
)

// CustomerFavorite Response
data class CustomerFavoriteResponse(
    val book_name: String,
    val author_name: String,
    val book_image: String
)


