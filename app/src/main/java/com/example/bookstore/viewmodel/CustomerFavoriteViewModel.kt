// CustomerFavoriteViewModel.kt
package com.example.bookstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.network.CustomerFavoriteResponse
import com.example.bookstore.repository.CustomerFavoriteRepository
import kotlinx.coroutines.launch

class CustomerFavoriteViewModel(private val customerFavoriteRepository: CustomerFavoriteRepository) : ViewModel() {

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
}
