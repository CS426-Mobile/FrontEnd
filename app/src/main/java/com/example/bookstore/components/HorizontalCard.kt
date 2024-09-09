package com.example.bookstore.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.Book
import com.example.bookstore.RatingBar
import com.example.bookstore.Screen
import com.example.bookstore.model.CustomerFavoriteResponse
import com.example.bookstore.ui.theme.mainColor

@Composable
fun BookCardHorizontal(book: CustomerFavoriteResponse, navController: NavHostController, onFavoriteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
            .clickable {
                navController.navigate(route = Screen.Book.passBookName(book.book_name))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Book image
        Image(
            painter = rememberAsyncImagePainter(model = book.book_image), // Load image from URL
            contentDescription = "Book Cover",
            modifier = Modifier.size(54.dp, 81.dp),
            contentScale = ContentScale.Crop // Crop the image to fit
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Hiển thị thông tin sách
        Column(modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Text(text = book.book_name, fontWeight = FontWeight.Bold)
            androidx.compose.material3.Text(text = book.author_name, color = Color.Gray)
            RatingBar(rating = book.average_rating ?: 0f)
        }

        Column(
        ){
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remove from Favorites",
                    tint = Color.Red
                )
            }
            Text("$ ${book.price ?: 0f}", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// Composable hiển thị thông tin một tác giả
// onButtonFollow: bam nut follow / unfollow
@Composable
fun AuthorHorizontalItem(authorName: String, numFollower: Int?, authorImage: String?, following: Boolean = true, navController: NavHostController, onButtonFollow: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(route = Screen.Author.passAuthorName(authorName))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hình đại diện tác giả
        if (authorImage != null) {
            Image(
                painter = rememberAsyncImagePainter(authorImage),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Thông tin tác giả
        Column(modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Text(
                text = authorName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            androidx.compose.material3.Text(
                text = "${numFollower ?: 0} Followers",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        Button(
            onClick = onButtonFollow,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (following) Color.Gray else mainColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                if (following) "Following" else "Follow",
                color = if (following) Color.Black else Color.White
            )
        }
    }
}

data class BookDetail(
    var title: String,
    var author: String,
    var rating: Float,
    var isFavorite: Boolean,
    var price: Double = 0.0
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

data class Order(
    val id: String,
    var listBookOrder: List<BookDetail>
)

data class CartItem(
    val id: Int,
    val title: String,
    val price: Int,
    var quantity: Int
)