// HomeViewModel.kt
package com.example.bookstore.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.bookstore.R
import com.example.bookstore.data.*
import com.example.bookstore.model.AuthorEntity
import com.example.bookstore.model.BookEntity
import com.example.bookstore.model.CategoryEntity
import com.example.bookstore.repository.BookStoreRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BookStoreRepository

    val recommendedBooks: LiveData<List<BookEntity>>
    val bestSellerBooks: LiveData<List<BookEntity>>
    val popularAuthors: LiveData<List<AuthorEntity>>
    val featuredBooks: LiveData<List<BookEntity>>
    val categories: LiveData<List<CategoryEntity>>

    init {
        val bookStoreDao = BookStoreDatabase.getDatabase(application).bookStoreDao()
        repository = BookStoreRepository(bookStoreDao)

        recommendedBooks = repository.allBooks.asLiveData()
        bestSellerBooks = repository.allBooks.asLiveData()
        popularAuthors = repository.allAuthors.asLiveData()
        featuredBooks = repository.allBooks.asLiveData()
        categories = repository.allCategories.asLiveData()

        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch {
            // Insert dummy data into the database
            val author1 = AuthorEntity(1, "Gerard Fabiano", R.drawable.author1, 5, "Fiction, Romance")
            val author2 = AuthorEntity(2, "Amber Julia", R.drawable.author2, 3, "Romance")
            val author3 = AuthorEntity(3, "Fernando Agaro", R.drawable.author3, 4, "Education, Fiction")

            val book1 = BookEntity(1, "1917", "Gerard Fabiano", R.drawable.book1, "Fiction")
            val book2 = BookEntity(2, "Perfume", "Amber Julia", R.drawable.book2, "Romance")
            val book3 = BookEntity(3, "Travel to Japan", "Fernando Agaro", R.drawable.book3, "Education")

            val category1 = CategoryEntity(1, "All", R.drawable.category_all)
            val category2 = CategoryEntity(2, "Romance", R.drawable.category_romance)
            val category3 = CategoryEntity(3, "Fiction", R.drawable.category_fiction)
            val category4 = CategoryEntity(4, "Education", R.drawable.category_education)

            repository.insertAuthor(author1)
            repository.insertAuthor(author2)
            repository.insertAuthor(author3)

            repository.insertBook(book1)
            repository.insertBook(book2)
            repository.insertBook(book3)

            repository.insertCategory(category1)
            repository.insertCategory(category2)
            repository.insertCategory(category3)
            repository.insertCategory(category4)
        }
    }
}
