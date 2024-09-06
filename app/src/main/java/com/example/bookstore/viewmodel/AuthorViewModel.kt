// AuthorViewModel.kt
package com.example.bookstore.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.network.AuthorRequest
import com.example.bookstore.network.AuthorResponse
import com.example.bookstore.network.SimpleAuthorResponse
import com.example.bookstore.repository.AuthorRepository
import kotlinx.coroutines.launch

class AuthorViewModel(application: Application) : AndroidViewModel(application) {

    private val authorRepository: AuthorRepository

    init {
        authorRepository = AuthorRepository()
    }
    // Get author by name
    fun getAuthor(authorName: String, onResult: (Boolean, AuthorResponse?) -> Unit) {
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
    fun getAllAuthors(onResult: (Boolean, List<AuthorResponse>?) -> Unit) {
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
    fun getTop5PopularAuthors(onResult: (Boolean, List<AuthorResponse>?) -> Unit) {
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
    fun getAuthorInfo(authorName: String, onResult: (Boolean, AuthorResponse?) -> Unit) {
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
