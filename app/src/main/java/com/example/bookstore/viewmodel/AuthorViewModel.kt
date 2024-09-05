// AuthorViewModel.kt
package com.example.bookstore.viewmodel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.BookStoreDatabase
import com.example.bookstore.model.UserEntity
import com.example.bookstore.network.Author
import com.example.bookstore.network.AuthorRequest
import com.example.bookstore.network.SimpleAuthorResponse
import com.example.bookstore.network.UserInfo
import com.example.bookstore.repository.AuthorRepository
import com.example.bookstore.repository.UserRepository
import kotlinx.coroutines.launch

class AuthorViewModel(private val authorRepository: AuthorRepository): ViewModel() {

    // Get author by name
    fun getAuthor(authorName: String, onResult: (Boolean, Author?) -> Unit) {
        viewModelScope.launch {
            val result = authorRepository.getAuthor(authorName)
            result.onSuccess {
                onResult(true, it)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    // Add a new author
    fun addAuthor(authorRequest: AuthorRequest, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = authorRepository.addAuthor(authorRequest)
            result.onSuccess {
                onResult(true, it)
            }.onFailure {
                onResult(false, it.message)
            }
        }
    }

    // Get all authors
    fun getAllAuthors(onResult: (Boolean, List<Author>?) -> Unit) {
        viewModelScope.launch {
            val result = authorRepository.getAllAuthors()
            result.onSuccess {
                onResult(true, it)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    // Get top 5 popular authors
    fun getTop5PopularAuthors(onResult: (Boolean, List<Author>?) -> Unit) {
        viewModelScope.launch {
            val result = authorRepository.getTop5PopularAuthors()
            result.onSuccess {
                onResult(true, it)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    // Get simple author info
    fun getSimpleAuthors(authorName: String, onResult: (Boolean, List<SimpleAuthorResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = authorRepository.getSimpleAuthors(authorName)
            result.onSuccess {
                onResult(true, it)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    // Get matching authors
    fun getMatchingAuthors(authorName: String, onResult: (Boolean, List<SimpleAuthorResponse>?) -> Unit) {
        viewModelScope.launch {
            val result = authorRepository.getMatchingAuthors(authorName)
            result.onSuccess {
                onResult(true, it)
            }.onFailure {
                onResult(false, null)
            }
        }
    }

    // Get all info of an author
    fun getAuthorInfo(authorName: String, onResult: (Boolean, Author?) -> Unit) {
        viewModelScope.launch {
            val result = authorRepository.getAuthorInfo(authorName)
            result.onSuccess {
                onResult(true, it)
            }.onFailure {
                onResult(false, null)
            }
        }
    }
}
