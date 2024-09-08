package com.example.bookstore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.bookstore.network.SimpleBookResponse
import com.example.bookstore.viewmodel.BookViewModel

@Composable
fun FeaturedBooksSection(navController: NavHostController, bookViewModel: BookViewModel) {
    var books by remember { mutableStateOf<List<SimpleBookResponse>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDataFetched by remember { mutableStateOf(false) }  // Track if data was fetched

    // Fetch books data only once
    LaunchedEffect(isDataFetched) {
        if (!isDataFetched) {  // Ensure the API call is made only once
            bookViewModel.get20Books { success, result ->
                if (success && result != null) {
                    books = result
                    errorMessage = null
                } else {
                    errorMessage = "Failed to load featured books"
                }
                isDataFetched = true  // Mark data as fetched
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Section title
        Text(
            text = "Featured Books",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        when {
            !isDataFetched -> {
                // Show placeholder while loading
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(2) { // Show 10 placeholder items
                        BookCardPlaceholder()
                    }
                }
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
                // Display books in a row of 2 books per row
                books!!.chunked(2).forEach { bookRow ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (book in bookRow) {
                            var isFavorite by remember { mutableStateOf(false) }  // Handle favorite state
                            BookCard(
                                title = book.book_name,
                                author = book.author_name,
                                rating = book.average_rating,
                                isFavorite = isFavorite,
                                imageUrl = book.book_image,
                                onFavoriteClick = {
                                    isFavorite = !isFavorite
                                    // Call API to add/remove favorite (implementation later)
                                },
                                onClick = {
                                    // Navigate to book detail screen
                                    navController.navigate("book/${book.book_name}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

