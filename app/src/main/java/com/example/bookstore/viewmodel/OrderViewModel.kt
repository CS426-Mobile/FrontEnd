// OrderViewModel.kt
package com.example.bookstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.OrderDetailResponse
import com.example.bookstore.model.OrderResponse
import com.example.bookstore.repository.BookRepository
import com.example.bookstore.repository.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val orderRepository: OrderRepository

    init {
        orderRepository = OrderRepository()
    }

    // Insert a new customer order
    fun insertCustomerOrder(userEmail: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = orderRepository.insertCustomerOrder(userEmail)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }

    // Query all customer orders for a user
    // ViewModel Layer
    fun queryCustomerOrder(userEmail: String, onResult: (Boolean, List<OrderResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = orderRepository.queryCustomerOrder(userEmail)
            result.onSuccess { orderListResponse ->
                onResult(true, orderListResponse.order_list)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    // Query all order details for a specific order code
    fun queryOrderDetail(orderCode: String, onResult: (Boolean, List<OrderDetailResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = orderRepository.queryOrderDetail(orderCode)
            result.onSuccess { orderDetails ->
                onResult(true, orderDetails)
            }.onFailure {
                onResult(false, null)
            }
        }
    }
}
