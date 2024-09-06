// BookRepository.kt
package com.example.bookstore.repository

import com.example.bookstore.network.BookCategoryResponse
import com.example.bookstore.network.BookResponse
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.network.SimpleBookResponse

class BookRepository {

    suspend fun get10Books(): Result<List<SimpleBookResponse>> {
        return try {
            val response = RetrofitInstance.api.get10Books()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No books found"))
            } else {
                Result.failure(Exception("Failed to fetch books. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun get20Books(): Result<List<SimpleBookResponse>> {
        return try {
            val response = RetrofitInstance.api.get20Books()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No books found"))
            } else {
                Result.failure(Exception("Failed to fetch books. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun getBooksByCategory(
        categoryName: String,
        ratingOptional: String = "all",
        priceOptional: String = "no",
        priceMin: Double = 0.0,
        priceMax: Double = 99999999.0,
        ratingSort: String = "none",
        priceSort: String = "none"
    ): Result<List<BookCategoryResponse>> {
        return try {
            val response = RetrofitInstance.api.getBooksByCategory(
                categoryName, ratingOptional, priceOptional, priceMin, priceMax, ratingSort, priceSort
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No books found"))
            } else {
                Result.failure(Exception("Failed to fetch books. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun getBookInfo(bookName: String): Result<BookResponse> {
        return try {
            val response = RetrofitInstance.api.getBookInfo(bookName)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Book not found"))
            } else {
                Result.failure(Exception("Failed to fetch book info. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun getNumBooksByAuthor(authorName: String): Result<Int> {
        return try {
            val response = RetrofitInstance.api.getNumBooksByAuthor(authorName)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.num_books)
                } ?: Result.failure(Exception("No books found for author"))
            } else {
                Result.failure(Exception("Failed to fetch number of books. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun getAuthorCategories(authorName: String): Result<List<String>> {
        return try {
            val response = RetrofitInstance.api.getAuthorCategories(authorName)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.categories)
                } ?: Result.failure(Exception("No categories found"))
            } else {
                Result.failure(Exception("Failed to fetch categories. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun getRelatedBooks(bookName: String): Result<List<SimpleBookResponse>> {
        return try {
            val response = RetrofitInstance.api.getRelatedBooks(bookName)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No related books found"))
            } else {
                Result.failure(Exception("Failed to fetch related books. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }
}
