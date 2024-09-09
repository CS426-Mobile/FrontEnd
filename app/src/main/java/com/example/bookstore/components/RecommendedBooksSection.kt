package com.example.bookstore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bookstore.network.SimpleBookResponse
import com.example.bookstore.viewmodel.BookViewModel

@Composable
fun RecommendedBooksSection(navController: NavHostController, bookViewModel: BookViewModel) {
    var books by remember { mutableStateOf<List<SimpleBookResponse>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDataFetched by remember { mutableStateOf(false) } // New state to track whether data was fetched

    // Fetch books from the ViewModel only once
    LaunchedEffect(isDataFetched) {
        if (!isDataFetched) {  // Ensure the API call is only made once
            bookViewModel.get10Books { success, result ->
                if (success && result != null && result.isNotEmpty()) {
                    books = result
                    errorMessage = null
                } else if (result.isNullOrEmpty()) {
                    errorMessage = "No Books found"
                } else {
                    errorMessage = "Failed to load books"
                }
                isDataFetched = true  // Mark as fetched
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        // Section title
        Text(
            text = "Recommended Books",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
        )

        when {
            !isDataFetched -> {
                // Show placeholder while loading
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(3) { // Show 3 placeholder items
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
                // Show books in a LazyRow
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(books!!) { book ->
                        var isFavorite by remember { mutableStateOf(false) } // Manage favorite state locally
                        BookCard(
                            book = book,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
