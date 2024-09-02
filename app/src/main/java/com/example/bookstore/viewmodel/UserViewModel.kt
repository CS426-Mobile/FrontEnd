package com.example.bookstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.AppDatabase
import com.example.bookstore.model.User
import com.example.bookstore.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    fun getUser(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUser(email, password)
            onResult(user)
        }
    }

    fun getUserByEmail(email: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            onResult(user)
        }
    }
}
