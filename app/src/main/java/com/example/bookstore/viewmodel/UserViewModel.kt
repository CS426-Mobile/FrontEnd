// UserViewModel.kt
package com.example.bookstore.viewmodel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.UserInfo
import com.example.bookstore.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    init {
        userRepository = UserRepository()
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.loginUser(email, password)
            result.onSuccess {
                onResult(true, null) // Login success
            }.onFailure {
                onResult(false, it.message) // Error message
            }
        }
    }

    // Register user function
    fun registerUser(email: String, password: String, password2: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.registerUser(email, password, password2)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }

    fun getUserInfo(onResult: (Boolean, UserInfo?, String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.getUserInfo()
            result.onSuccess { userInfo ->
                onResult(true, userInfo, null) // Success with user info
            }.onFailure { exception ->
                onResult(false, null, exception.message) // Error with message
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences("app_preferences", MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("remembered_user_email")
                remove("remembered_user_password")
                apply()
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.changePassword(oldPassword, newPassword, confirmPassword)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }

    fun updateAddress(address: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.updateAddress(address)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }

    fun updateAddressWithEmail(email: String, address: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.updateAddressWithEmail(email, address)
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }

    fun getAddress(email: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.getAddress(email)
            result.onSuccess { address ->
                onResult(true, address)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }

    fun logoutUser(onResult: (Boolean, String?) -> Unit) {
        val sharedPref = getApplication<Application>().getSharedPreferences("app_preferences", MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("remembered_user_email")
            remove("remembered_user_password")
            apply()
        }
        viewModelScope.launch {
            val result = userRepository.logoutUser()
            result.onSuccess { message ->
                onResult(true, message)
            }.onFailure { exception ->
                onResult(false, exception.message)
            }
        }
    }
}
