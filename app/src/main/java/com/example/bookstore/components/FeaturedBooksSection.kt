package com.example.bookstore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.bookstore.model.BookCategoryResponse
import com.example.bookstore.model.SimpleBookResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.BookViewModel

@Composable
fun FeaturedBooksSection(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    selectedCategories: String,
    isTopRating: Boolean,
    sortType: String,
    fromPrice: Int = 0,
    toPrice: Int = 100,
    rating: String = "all",
    shouldRefetch: Boolean,
    onFetchComplete: () -> Unit
) {
    var books by remember { mutableStateOf<List<BookCategoryResponse>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDataFetched by remember { mutableStateOf(false) }

    LaunchedEffect(shouldRefetch) {
        if ((selectedCategories == "All" && !isTopRating && sortType == "none" && fromPrice == 0 && toPrice == 100 && rating == "all") || !isDataFetched)
        { // Fetch data if needed or first time
            bookViewModel.get20Books { success, result ->
                if (success && result != null) {
                    books = result
                    errorMessage = null
                } else {
                    errorMessage = "Failed to load featured books"
                }
                isDataFetched = true
                onFetchComplete() // Reset the refetch flag after data is fetched
            }
        }
        else if (shouldRefetch) { // Fetch data if needed or first time
            bookViewModel.getBooksByCategory(
                categoryName = if (selectedCategories == "All") "" else selectedCategories,
                ratingOptional = rating,
                priceOptional = "yes",
                priceMin = fromPrice.toDouble(),
                priceMax = toPrice.toDouble(),
                ratingSort = if (isTopRating) "desc" else "none",
                priceSort = sortType
            ) { success, result ->
                if (success && result != null) {
                    books = result
                    errorMessage = null
                } else {
                    errorMessage = "Failed to load featured books"
                }
                isDataFetched = true
                onFetchComplete() // Reset the refetch flag after data is fetched
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Section title
        Text(
            text = "Featured Books",
            color = mainColor,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        when {
            !isDataFetched -> {
                // Show placeholder while loading
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 8.dp),
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
                            .padding(horizontal = 30.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (book in bookRow) {
                            BookCard(
                                book = SimpleBookResponse(book.book_name, book.author_name, book.book_image, book.average_rating),
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

