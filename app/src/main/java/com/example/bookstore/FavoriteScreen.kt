package com.example.bookstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun FavoriteScreen(navController: NavHostController) {
    // Dữ liệu mẫu cho danh sách sách ưa thích
    var favoriteBooks = sampleFavoriteBooks()

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Favorites", navController = navController, isCart = true)
        },
        content = { paddingValues ->
            if (favoriteBooks.isEmpty()) {
                // Hiển thị thông báo khi không có sách ưa thích
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No favorite books.",
//                        style = MaterialTheme.typography.h6,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Hiển thị danh sách sách ưa thích
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(favoriteBooks) { book ->
                        BookCardHorizontal(book = book, onFavoriteClick = {
                            favoriteBooks = favoriteBooks.toMutableList().apply {
                                remove(book)
                            }
                        })
                        if (favoriteBooks.indexOf(book) < favoriteBooks.size - 1) {
                            Divider()
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun RatingBar(rating: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (i <= rating) Color(0xFFFFC107) // Vàng cho sao được tô
                else if (i - rating < 1) Color(0xFFFFC107).copy(alpha = 0.5f) // Màu vàng nhạt cho sao tô một phần
                else Color.Gray
            )
        }
        Text(text = " ${rating.toInt()}") // Hiển thị số lượng đánh giá
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteScreenPreview() {
    FavoriteScreen(navController = rememberNavController())
}

fun sampleFavoriteBooks(): List<BookDetail> {
    return listOf(
        BookDetail("Abstract Art in The World", "Armando Newman", 4.5f, true),
        BookDetail("Leila’s Story", "Marcy Arkinson", 4.0f, true),
        BookDetail("Where is The Queen", "Scott Brian", 3.5f, true),
        BookDetail("The Secret About Us", "Elizabeth McKean", 4.2f, true),
        BookDetail("The Happiness", "Kate Kirkwood", 4.7f, true),
        BookDetail("Peace in His Life", "Armando Newman", 3.9f, true)
    )
}
