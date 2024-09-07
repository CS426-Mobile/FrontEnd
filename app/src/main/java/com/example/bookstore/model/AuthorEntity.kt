// AuthorEntity.kt
package com.example.bookstore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "author_table")
data class AuthorEntity(
    @PrimaryKey val name: String,
    val localImageRes: Int,
    val numberOfBooks: Int,
    val categories: String
)
