package com.example.bookstore.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.Screen
import com.example.bookstore.model.SimpleBookResponse
import com.example.bookstore.viewmodel.CustomerFavoriteViewModel
import com.example.bookstore.viewmodel.UserViewModel

@SuppressLint("DefaultLocale")
@Composable
fun BookCard(
    book: SimpleBookResponse,
    navController: NavHostController,
) {
    val customerFavoriteViewModel: CustomerFavoriteViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    var isFavorite by remember { mutableStateOf(false) }
    var userEmail by remember { mutableStateOf<String?>(null) }

    // Fetch the user's email once when the card is displayed
    LaunchedEffect(Unit) {
        userViewModel.getUserInfo { success, userInfo, _ ->
            if (success && userInfo != null) {
                userEmail = userInfo.user_email

                // Check if the book is in the user's favorites
                customerFavoriteViewModel.queryCustomerFavoriteExist(userEmail!!, book.book_name) { favoriteSuccess, _ ->
                    if (favoriteSuccess) {
                        isFavorite = true
                    }
                }
            }
        }
    }

    // Handle favorite toggle
    fun toggleFavorite() {
        userEmail?.let { email ->
            if (isFavorite) {
                // Remove from favorites
                customerFavoriteViewModel.deleteCustomerFavorite(email, book.book_name) { success, _ ->
                    if (success) {
                        isFavorite = false
                    }
                }
            } else {
                // Add to favorites
                customerFavoriteViewModel.insertCustomerFavorite(email, book.book_name) { success, _ ->
                    if (success) {
                        isFavorite = true
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .width(144.dp)
            .height(240.dp)
            .padding(8.dp)
            .background(color = Color.White)
            .clickable { navController.navigate(route = Screen.Book.passBookName(book.book_name)) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Book image placeholder
            Box(
                modifier = Modifier
                    .size(144.dp, 160.dp)
                    .background(color = Color.White)
            ) {
                // Book image
                Image(
                    painter = rememberAsyncImagePainter(model = book.book_image), // Load image from URL
                    contentDescription = "Book Cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp), // Set the image height
                    contentScale = ContentScale.Crop // Crop the image to fit
                )

                // Semi-transparent gray blur layer
                Box(
                    modifier = Modifier
                        .fillMaxSize()  // Fill the size of the image
                        .background(Color(0x4D000000))  // Semi-transparent gray color (30% opacity)
                )

                // Favorite icon
                IconButton(
                    onClick = { toggleFavorite() },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.White,
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
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(24.dp)
                    )
                    androidx.compose.material3.Text(
                        text = String.format("%.1f", book.average_rating),
                        style = MaterialTheme.typography.caption,
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Book title
            androidx.compose.material3.Text(
                text = book.book_name,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 4.dp),
                textAlign = TextAlign.Center
            )

            // Author name
            androidx.compose.material3.Text(
                text = book.author_name,
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
                fontSize = 11.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BookCardPlaceholder() {
    Card(
        modifier = Modifier
            .width(144.dp)
            .height(240.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Placeholder image
            Box(
                modifier = Modifier
                    .size(144.dp, 160.dp)
                    .background(color = Color.White)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Placeholder text
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
