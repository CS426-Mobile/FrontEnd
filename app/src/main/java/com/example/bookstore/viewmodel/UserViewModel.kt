package com.example.bookstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.BookStoreDatabase
import com.example.bookstore.model.UserEntity
import com.example.bookstore.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    init {
        val userDao = BookStoreDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
    }

    fun insertUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    fun getUser(email: String, password: String, onResult: (UserEntity?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUser(email, password)
            onResult(user)
        }
    }

    fun getUserByEmail(email: String, onResult: (UserEntity?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            onResult(user)
        }
    }
}
