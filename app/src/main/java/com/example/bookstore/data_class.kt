package com.example.bookstore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BookCard(
    title: String,
    author: String,
    rating: Float,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(144.dp)
            .padding(8.dp)
            .clickable { onClick() },  // Handle book click
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Book image placeholder
            Box(
                modifier = Modifier
                    .size(144.dp, 181.dp)
                    .background(Color.LightGray)
            ) {
                // Favorite button (heart icon)
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = rating.toString(),
                        style = MaterialTheme.typography.caption,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Book title
            Text(
                text = title,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )

            // Author name
            Text(
                text = author,
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

data class BookDetail(
    var title: String,
    var author: String,
    var rating: Float,
    var isFavorite: Boolean,
)

// Mẫu dữ liệu tác giả
data class Author(
    val name: String,
    val followers: String,
    val books: Int,
    val isFollowing: Boolean,
    val categories: List<String>,
    val about: String,
    val booksList: List<Book>
)

data class Account(
    val name: String,
    val address: String,
)