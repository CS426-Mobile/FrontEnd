// CategoryRepository.kt
package com.example.bookstore.repository

import com.example.bookstore.model.CategoryResponse
import com.example.bookstore.network.RetrofitInstance

class CategoryRepository {

    // Fetch all categories
    suspend fun getAllCategories(): Result<List<CategoryResponse>> {
        return try {
            val response = RetrofitInstance.api.getAllCategories()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No categories found"))
            } else {
                Result.failure(Exception("Failed to fetch categories. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }
}
