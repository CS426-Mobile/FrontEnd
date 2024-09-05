// UserEntity.kt
package com.example.bookstore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val email: String,
    val password: String,
    val address: String? = null,
)
