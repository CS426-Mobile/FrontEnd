package com.example.bookstore.repository

import com.example.bookstore.data.UserDao
import com.example.bookstore.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUser(email: String, password: String): User? {
        return userDao.getUser(email, password)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}
