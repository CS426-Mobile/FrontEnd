// File: UserRepository.kt
package com.example.bookstore.repository

import com.example.bookstore.data.UserDao
import com.example.bookstore.model.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUser(email: String, password: String): UserEntity? {
        return userDao.getUser(email, password)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    // Method to update address
    suspend fun updateAddress(email: String, address: String) {
        userDao.updateAddress(email, address)
    }
}
