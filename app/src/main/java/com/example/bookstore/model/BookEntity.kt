// BookEntity.kt
package com.example.bookstore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
data class BookEntity(
    @PrimaryKey val book_name: String,
    val author_name: String,
    val book_image: String,
    val average_rating: Float,
    val price: Float? = 0f
)