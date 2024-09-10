// CategoryViewModel.kt
package com.example.bookstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.CategoryResponse
import com.example.bookstore.repository.BookRepository
import com.example.bookstore.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val categoryRepository: CategoryRepository

    init {
        categoryRepository = CategoryRepository()
    }

    // Fetch all categories
    fun getAllCategories(onResult: (Boolean, List<CategoryResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = categoryRepository.getAllCategories()
            result.onSuccess { categories ->
                onResult(true, categories)
            }.onFailure {
                onResult(false, null)
            }
        }
    }
}
