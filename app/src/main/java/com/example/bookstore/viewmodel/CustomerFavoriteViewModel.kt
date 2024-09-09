// CustomerFavoriteViewModel.kt
package com.example.bookstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.BookStoreDatabase
import com.example.bookstore.network.CustomerFavoriteResponse
import com.example.bookstore.repository.CustomerFavoriteRepository
import com.example.bookstore.repository.UserRepository
import kotlinx.coroutines.launch

class CustomerFavoriteViewModel(application: Application) : AndroidViewModel(application)  {
    private val customerFavoriteRepository: CustomerFavoriteRepository

    init {
        customerFavoriteRepository = CustomerFavoriteRepository()
    }

    fun insertCustomerFavorite(userEmail: String, bookName: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = customerFavoriteRepository.insertCustomerFavorite(userEmail, bookName)
            result.onSuccess {
                onResult(true, it)
            }.onFailure {
                onResult(false, it.message)
            }
        }
    }

    fun deleteCustomerFavorite(userEmail: String, bookName: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = customerFavoriteRepository.deleteCustomerFavorite(userEmail, bookName)
            result.onSuccess {
                onResult(true, it)
            }.onFailure {
                onResult(false, it.message)
            }
        }
    }

    fun queryCustomerFavorite(userEmail: String, onResult: (Boolean, List<CustomerFavoriteResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = customerFavoriteRepository.queryCustomerFavorite(userEmail)
            result.onSuccess {
                onResult(true, it)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    fun queryCustomerFavoriteExist(userEmail: String, bookName: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = customerFavoriteRepository.queryCustomerFavoriteExist(userEmail, bookName)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }
}
