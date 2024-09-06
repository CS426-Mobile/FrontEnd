package com.example.bookstore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bookstore.BookCard
import com.example.bookstore.network.SimpleBookResponse
import com.example.bookstore.viewmodel.BookViewModel

@Composable
fun RecommendedBooksSection(navController: NavHostController, bookViewModel: BookViewModel) {
    var books by remember { mutableStateOf<List<SimpleBookResponse>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch books from the ViewModel
    LaunchedEffect(Unit) {
        bookViewModel.get10Books { success, result ->
            if (success && result != null && result.isNotEmpty()) {
                books = result
            } else if (result.isNullOrEmpty()) {
                errorMessage = "No Books found"
            } else {
                errorMessage = "Failed to load books"
            }
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Section title
        Text(
            text = "Recommended",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.h6
        )

        when {
            isLoading -> {
                // Show loading indicator while fetching data
                CircularProgressIndicator(color = Color.Gray, modifier = Modifier.padding(16.dp))
            }
            errorMessage != null -> {
                // Show error message if data fetch fails
                Text(
                    text = errorMessage!!,
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            books != null && books!!.isNotEmpty() -> {
                // Show books in a LazyRow
                LazyRow(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(books!!) { book ->
                        var isFavorite by remember { mutableStateOf(false) } // Example to manage favorite state

                        // Each book card
                        BookCard(
                            title = book.book_name,
                            author = book.author_name,
                            rating = 4.5f, // Placeholder rating
                            isFavorite = isFavorite,
                            onFavoriteClick = {
                                isFavorite = !isFavorite
                                // Call API to add/remove from favorite (implementation later)
                            },
                            onClick = {
                                // Navigate to book detail screen
                                navController.navigate("book/${book.book_name}")
                            }
                        )
                    }
                }
            }
            else -> {
                // Show message if no books are found
                Text(
                    text = "No recommended books available at the moment.",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}