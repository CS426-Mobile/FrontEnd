// BookEntity.kt
package com.example.bookstore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val authorName: String,
    val localImageRes: Int,
    val categories: String
)
