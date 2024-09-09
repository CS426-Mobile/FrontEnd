// CustomerFavoriteRepository.kt
package com.example.bookstore.repository

import com.example.bookstore.model.CustomerFavoriteRequest
import com.example.bookstore.model.CustomerFavoriteResponse
import com.example.bookstore.network.RetrofitInstance

class CustomerFavoriteRepository {

    suspend fun insertCustomerFavorite(userEmail: String, bookName: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.insertCustomerFavorite(CustomerFavoriteRequest(userEmail, bookName))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Insert successful")
            } else {
                Result.failure(Exception("Failed to insert favorite. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun deleteCustomerFavorite(userEmail: String, bookName: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.deleteCustomerFavorite(CustomerFavoriteRequest(userEmail, bookName))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Delete successful")
            } else {
                Result.failure(Exception("Failed to delete favorite. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun queryCustomerFavorite(userEmail: String): Result<List<CustomerFavoriteResponse>> {
        return try {
            val response = RetrofitInstance.api.queryCustomerFavorite(userEmail)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No favorites found"))
            } else {
                Result.failure(Exception("Failed to fetch favorites. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Check if a customer favorite exists
    suspend fun queryCustomerFavoriteExist(userEmail: String, bookName: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.queryCustomerFavoriteExist(userEmail, bookName)
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Record exists")
            } else {
                Result.failure(Exception("Failed to check existence. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }
}
