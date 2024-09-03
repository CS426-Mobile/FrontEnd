// BookStoreRepository.kt
package com.example.bookstore.repository

import com.example.bookstore.data.BookStoreDao
import com.example.bookstore.model.AuthorEntity
import com.example.bookstore.model.BookEntity
import com.example.bookstore.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

class BookStoreRepository(private val bookStoreDao: BookStoreDao) {

    val allAuthors: Flow<List<AuthorEntity>> = bookStoreDao.getAllAuthors()
    val allBooks: Flow<List<BookEntity>> = bookStoreDao.getAllBooks()
    val allCategories: Flow<List<CategoryEntity>> = bookStoreDao.getAllCategories()

    suspend fun insertAuthor(author: AuthorEntity) {
        bookStoreDao.insertAuthor(author)
    }

    suspend fun insertBook(book: BookEntity) {
        bookStoreDao.insertBook(book)
    }

    suspend fun insertCategory(category: CategoryEntity) {
        bookStoreDao.insertCategory(category)
    }
}
