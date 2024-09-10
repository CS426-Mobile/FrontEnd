// CustomerFollowRepository.kt
package com.example.bookstore.repository

import com.example.bookstore.model.CustomerFollowRequest
import com.example.bookstore.model.CustomerFollowResponse
import com.example.bookstore.network.RetrofitInstance

class CustomerFollowRepository {

    // Query authors a user follows
    suspend fun queryCustomerFollow(userEmail: String): Result<List<CustomerFollowResponse>> {
        return try {
            val response = RetrofitInstance.api.queryCustomerFollow(userEmail)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No authors found"))
            } else {
                Result.failure(Exception("Failed to fetch followed authors. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Toggle follow/unfollow for an author
    suspend fun toggleFollow(userEmail: String, authorName: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.toggleFollow(CustomerFollowRequest(authorName, userEmail))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Operation successful")
            } else {
                Result.failure(Exception("Failed to toggle follow. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Get the number of followers for an author
    suspend fun getNumFollowersForAuthor(authorName: String): Result<Int> {
        return try {
            val response = RetrofitInstance.api.getNumFollowersForAuthor(authorName)
            if (response.isSuccessful) {
                response.body()?.num_follower?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No follower count available"))
            } else {
                Result.failure(Exception("Failed to get follower count. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Query follow status of an author by user
    suspend fun queryFollow(authorName: String, userEmail: String): Result<Boolean> {
        return try {
            val response = RetrofitInstance.api.queryFollow(authorName, userEmail)
            if (response.isSuccessful) {
                response.body()?.follow?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No follow status found"))
            } else {
                Result.failure(Exception("Failed to query follow status. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }
}
