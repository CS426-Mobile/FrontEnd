// CustomerCartViewModel.kt
package com.example.bookstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.CustomerCartResponse
import com.example.bookstore.repository.CustomerCartRepository
import kotlinx.coroutines.launch

class CustomerCartViewModel(application: Application) : AndroidViewModel(application) {
    private val customerCartRepository: CustomerCartRepository

    init {
        customerCartRepository = CustomerCartRepository()
    }
    
    // Insert a book into the cart
    fun insertCustomerCartBook(userEmail: String, bookName: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = customerCartRepository.insertCustomerCartBook(userEmail, bookName)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }

    // Delete a book from the cart
    fun deleteCustomerCartBook(userEmail: String, bookName: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = customerCartRepository.deleteCustomerCartBook(userEmail, bookName)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }

    // Increase the number of books in the cart
    fun increaseNumBooks(userEmail: String, bookName: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = customerCartRepository.increaseNumBooks(userEmail, bookName)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }

    // Decrease the number of books in the cart
    fun decreaseNumBooks(userEmail: String, bookName: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = customerCartRepository.decreaseNumBooks(userEmail, bookName)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }

    // Calculate the total price of the cart
    fun calculateTotalPrice(userEmail: String, onResult: (Boolean, Float?) -> Unit) {
        viewModelScope.launch {
            val result = customerCartRepository.calculateTotalPrice(userEmail)
            result.onSuccess { totalPrice ->
                onResult(true, totalPrice)
            }.onFailure { exception ->
                onResult(false, null)
            }
        }
    }

    // Query all customer cart books for a user
    fun queryCustomerCartBooks(userEmail: String, onResult: (Boolean, List<CustomerCartResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = customerCartRepository.queryCustomerCartBooks(userEmail)
            result.onSuccess { cartBooks ->
                onResult(true, cartBooks)
            }.onFailure {
                onResult(false, null)
            }
        }
    }
}
