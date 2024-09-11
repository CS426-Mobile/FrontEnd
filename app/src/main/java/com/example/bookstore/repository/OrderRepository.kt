// OrderRepository.kt
package com.example.bookstore.repository

import com.example.bookstore.model.OrderDetailResponse
import com.example.bookstore.model.OrderListResponse
import com.example.bookstore.model.OrderRequest
import com.example.bookstore.model.OrderResponse
import com.example.bookstore.network.RetrofitInstance

class OrderRepository {

    // Insert a new customer order
    suspend fun insertCustomerOrder(userEmail: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.insertCustomerOrder(OrderRequest(userEmail))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Order placed successfully")
            } else {
                Result.failure(Exception("Failed to place order. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Repository Layer
    suspend fun queryCustomerOrder(userEmail: String): Result<OrderListResponse> {
        return try {
            val response = RetrofitInstance.api.queryCustomerOrder(userEmail)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No orders found"))
            } else {
                Result.failure(Exception("Failed to fetch orders. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    // Query all order details for a specific order code
    suspend fun queryOrderDetail(orderCode: String): Result<List<OrderDetailResponse>> {
        return try {
            val response = RetrofitInstance.api.queryOrderDetail(orderCode)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No order details found"))
            } else {
                Result.failure(Exception("Failed to fetch order details. Code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }
}
