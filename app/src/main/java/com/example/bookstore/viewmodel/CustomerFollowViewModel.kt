// CustomerFollowViewModel.kt
package com.example.bookstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.CustomerFollowResponse
import com.example.bookstore.repository.CustomerFollowRepository
import kotlinx.coroutines.launch

class CustomerFollowViewModel(application: Application) : AndroidViewModel(application) {
    private val customerFollowRepository: CustomerFollowRepository

    init {
        customerFollowRepository = CustomerFollowRepository()
    }

    // Query authors a user follows
    fun queryCustomerFollow(userEmail: String, onResult: (Boolean, List<CustomerFollowResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = customerFollowRepository.queryCustomerFollow(userEmail)
            result.onSuccess { authors ->
                onResult(true, authors)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    // Toggle follow/unfollow for an author
    fun toggleFollow(userEmail: String, authorName: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = customerFollowRepository.toggleFollow(userEmail, authorName)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure {
                onResult(false, it.message)
            }
        }
    }

    // Get the number of followers for an author
    fun getNumFollowersForAuthor(authorName: String, onResult: (Boolean, Int?) -> Unit) {
        viewModelScope.launch {
            val result = customerFollowRepository.getNumFollowersForAuthor(authorName)
            result.onSuccess { numFollowers ->
                onResult(true, numFollowers)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    // Query follow status of an author by user
    fun queryFollow(authorName: String, userEmail: String, onResult: (Boolean, Boolean?) -> Unit) {
        viewModelScope.launch {
            val result = customerFollowRepository.queryFollow(authorName, userEmail)
            result.onSuccess { isFollowing ->
                onResult(true, isFollowing)
            }.onFailure {
                onResult(false, null)
            }
        }
    }
}
