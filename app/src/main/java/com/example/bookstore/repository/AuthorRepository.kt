// AuthorRepository.kt
package com.example.bookstore.repository

import com.example.bookstore.network.AuthorResponse
import com.example.bookstore.network.AuthorRequest
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.network.SimpleAuthorResponse

class AuthorRepository {

    // Get author by name
    suspend fun getAuthor(authorName: String): Result<AuthorResponse> {
        return try {
            val response = RetrofitInstance.api.getAuthor(authorName)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Author not found"))
            } else {
                Result.failure(Exception("Failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Add a new author
    suspend fun addAuthor(authorRequest: AuthorRequest): Result<String> {
        return try {
            val response = RetrofitInstance.api.addAuthor(authorRequest)
            if (response.isSuccessful) {
                Result.success("Author added successfully")
            } else {
                Result.failure(Exception("Failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Get all authors
    suspend fun getAllAuthors(): Result<List<AuthorResponse>> {
        return try {
            val response = RetrofitInstance.api.getAllAuthors()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No authors found"))
            } else {
                Result.failure(Exception("Failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Get top 5 popular authors
    suspend fun getTop5PopularAuthors(): Result<List<AuthorResponse>> {
        return try {
            val response = RetrofitInstance.api.getTop5PopularAuthors()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No authors found"))
            } else {
                Result.failure(Exception("Failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Get simple author info
    suspend fun getSimpleAuthors(authorName: String): Result<List<SimpleAuthorResponse>> {
        return try {
            val response = RetrofitInstance.api.getSimpleAuthors(authorName)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No authors found"))
            } else {
                Result.failure(Exception("Failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Get matching authors by string
    suspend fun getMatchingAuthors(authorName: String): Result<List<SimpleAuthorResponse>> {
        return try {
            val response = RetrofitInstance.api.getMatchingAuthors(authorName)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No authors found"))
            } else {
                Result.failure(Exception("Failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Get all info of an author
    suspend fun getAuthorInfo(authorName: String): Result<AuthorResponse> {
        return try {
            val response = RetrofitInstance.api.getAuthorInfo(authorName)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Author not found"))
            } else {
                Result.failure(Exception("Failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }
}
