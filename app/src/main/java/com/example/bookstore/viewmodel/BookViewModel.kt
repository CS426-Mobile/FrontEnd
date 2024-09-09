// BookViewModel.kt
package com.example.bookstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.BookCategoryResponse
import com.example.bookstore.model.BookResponse
import com.example.bookstore.model.SimpleBookResponse
import com.example.bookstore.repository.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepository: BookRepository

    init {
        bookRepository = BookRepository()
    }

    fun get10Books(onResult: (Boolean, List<SimpleBookResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = bookRepository.get10Books()
            result.onSuccess { books ->
                onResult(true, books)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    fun get20Books(onResult: (Boolean, List<SimpleBookResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = bookRepository.get20Books()
            result.onSuccess { books ->
                onResult(true, books)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    fun getBooksByCategory(
        categoryName: String,
        ratingOptional: String = "all",
        priceOptional: String = "no",
        priceMin: Double = 0.0,
        priceMax: Double = 99999999.0,
        ratingSort: String = "none",
        priceSort: String = "none",
        onResult: (Boolean, List<BookCategoryResponse>?) -> Unit
    ) {
        viewModelScope.launch {
            val result = bookRepository.getBooksByCategory(categoryName, ratingOptional, priceOptional, priceMin, priceMax, ratingSort, priceSort)
            result.onSuccess { books ->
                onResult(true, books)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    // Get books by matching string
    fun getBooksByMatchingString(
        categoryName: String,
        bookInput: String,
        ratingOptional: String = "all",
        priceOptional: String = "no",
        priceMin: Double = 0.0,
        priceMax: Double = 99999999.0,
        ratingSort: String = "none",
        priceSort: String = "none",
        onResult: (Boolean, List<BookCategoryResponse>?) -> Unit
    ) {
        viewModelScope.launch {
            val result = bookRepository.getBooksByMatchingString(
                categoryName, bookInput, ratingOptional, priceOptional, priceMin, priceMax, ratingSort, priceSort
            )
            result.onSuccess { books ->
                onResult(true, books)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    fun getBookInfo(bookName: String, onResult: (Boolean, BookResponse?) -> Unit) {
        viewModelScope.launch {
            val result = bookRepository.getBookInfo(bookName)
            result.onSuccess { book ->
                onResult(true, book)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    fun getNumBooksByAuthor(authorName: String, onResult: (Boolean, Int?) -> Unit) {
        viewModelScope.launch {
            val result = bookRepository.getNumBooksByAuthor(authorName)
            result.onSuccess { numBooks ->
                onResult(true, numBooks)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    fun getAuthorCategories(authorName: String, onResult: (Boolean, List<String>?) -> Unit) {
        viewModelScope.launch {
            val result = bookRepository.getAuthorCategories(authorName)
            result.onSuccess { categories ->
                onResult(true, categories)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    // Get books by author
    fun getBooksByAuthor(authorName: String, onResult: (Boolean, List<SimpleBookResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = bookRepository.getBooksByAuthor(authorName)
            result.onSuccess { books ->
                onResult(true, books)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    fun getRelatedBooks(bookName: String, onResult: (Boolean, List<SimpleBookResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = bookRepository.getRelatedBooks(bookName)
            result.onSuccess { books ->
                onResult(true, books)
            }.onFailure {
                onResult(false, null)
            }
        }
    }
}
