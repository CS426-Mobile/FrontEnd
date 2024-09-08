package com.example.bookstore.components

import com.example.bookstore.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstore.Book
import com.example.bookstore.RatingBar
import com.example.bookstore.ui.theme.mainColor

@Composable
fun BookCardHorizontal(book: BookDetail, onFavoriteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hiển thị bìa sách
        Image(
            painter = painterResource(id = R.drawable.ic_cart), // Thay thế bằng nguồn bìa sách của bạn
            contentDescription = "Book Cover",
            modifier = Modifier.size(54.dp, 81.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Hiển thị thông tin sách
        Column(modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Text(text = book.title, fontWeight = FontWeight.Bold)
            androidx.compose.material3.Text(text = book.author, color = Color.Gray)
            RatingBar(rating = book.rating)
        }

        // Biểu tượng trái tim để quản lý trạng thái yêu thích va gia tien
        Column(

        ){
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remove from Favorites",
                    tint = Color.Red
                )
            }
            Text("$ ${book.price}", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// Composable hiển thị thông tin một tác giả
// onFollowToggle: chuyen sang author screen
// onButtonFollow: bam nut follow / unfollow
@Composable
fun AuthorHorizontalItem(author: Author, onFollowToggle: () -> Unit, onButtonFollow: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onFollowToggle() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hình đại diện tác giả
        Image(
            painter = painterResource(id = R.drawable.ic_account), // Thay thế bằng hình đại diện của bạn
            contentDescription = "Avatar",
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Thông tin tác giả
        Column(modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Text(
                text = author.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            androidx.compose.material3.Text(
                text = "${author.followers} Followers",
                color = Color.Gray,
                fontSize = 13.sp
            )
            androidx.compose.material3.Text(text = if (author.categories.size > 2) {
                "${author.categories[0]}, ${author.categories[1]}, ${author.categories.size - 2} more"
            } else {
                buildString {
                    for (i in author.categories.indices) {
                        append(author.categories[i])
                        if (i < author.categories.size - 1) {
                            append(", ")
                        }
                    }
                }
            },
                color = Color.Gray)
        }

        Button(
            onClick = onButtonFollow,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (author.isFollowing) Color.Gray else mainColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            androidx.compose.material.Text(
                if (author.isFollowing) "Following" else "Follow",
                color = if (author.isFollowing) Color.Gray else Color.White
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