// BookStoreDao.kt
package com.example.bookstore.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookstore.model.AuthorEntity
import com.example.bookstore.model.BookEntity
import com.example.bookstore.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookStoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthor(author: AuthorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM author_table")
    fun getAllAuthors(): Flow<List<AuthorEntity>>

    @Query("SELECT * FROM book_table")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM category_table")
    fun getAllCategories(): Flow<List<CategoryEntity>>
}
