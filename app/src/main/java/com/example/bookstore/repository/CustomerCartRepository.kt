// CustomerCartRepository.kt
package com.example.bookstore.repository

import com.example.bookstore.model.CustomerCartRequest
import com.example.bookstore.model.CustomerCartResponse
import com.example.bookstore.network.RetrofitInstance

class CustomerCartRepository {

    // Insert a book into the cart
    suspend fun insertCustomerCartBook(userEmail: String, bookName: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.insertCustomerCartBook(CustomerCartRequest(userEmail, bookName))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Insert successful")
            } else {
                Result.failure(Exception("Failed to insert cart item. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Delete a book from the cart
    suspend fun deleteCustomerCartBook(userEmail: String, bookName: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.deleteCustomerCartBook(CustomerCartRequest(userEmail, bookName))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Delete successful")
            } else {
                Result.failure(Exception("Failed to delete cart item. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Increase the number of books in the cart
    suspend fun increaseNumBooks(userEmail: String, bookName: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.increaseNumBooks(CustomerCartRequest(userEmail, bookName))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Increase successful")
            } else {
                Result.failure(Exception("Failed to increase cart item. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Decrease the number of books in the cart
    suspend fun decreaseNumBooks(userEmail: String, bookName: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.decreaseNumBooks(CustomerCartRequest(userEmail, bookName))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Decrease successful")
            } else {
                Result.failure(Exception("Failed to decrease cart item. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Calculate the total price of the cart
    suspend fun calculateTotalPrice(userEmail: String): Result<Float> {
        return try {
            val response = RetrofitInstance.api.calculateTotalPrice(userEmail)
            if (response.isSuccessful) {
                Result.success(response.body()?.total_price ?: 0f)
            } else {
                Result.failure(Exception("Failed to calculate total price. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Query all customer cart books for a user
    suspend fun queryCustomerCartBooks(userEmail: String): Result<List<CustomerCartResponse>> {
        return try {
            val response = RetrofitInstance.api.queryCustomerCartBooks(userEmail)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No cart books found"))
            } else {
                Result.failure(Exception("Failed to fetch cart books. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }
}

