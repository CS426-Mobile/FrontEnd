package com.example.bookstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.bookstore.network.AuthorResponse
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


@Composable
fun BookCard(book: BookDetail,  onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(144.dp) // Kích thước thẻ sách
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bìa sách
            Box(
                modifier = Modifier
                    .size(144.dp, 181.dp)
                    .background(Color.LightGray) // Thay bằng hình ảnh bìa sách của bạn
            ) {
                // Nút yêu thích
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                ) {
                    Icon(
                        imageVector = if (book.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Add to Favorite",
                        tint = if (book.isFavorite) Color.Red else Color.White, // Đổi màu khi yêu thích

                        modifier = Modifier.size(20.dp)
                    )
                }

                // Số lượt rating trung bình
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = book.rating.toString(),
                        style = MaterialTheme.typography.caption,
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tiêu đề sách
            Text(
                text = book.title,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )

            // Tên tác giả
            Text(
                text = book.author,
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Composable hiển thị thông tin một tác giả
// onFollowToggle: chuyen sang author screen
// onButtonFollow: bam nut follow / unfollow
@Composable
fun AuthorItem(author: Author, onFollowToggle: () -> Unit, onButtonFollow: () -> Unit) {
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

@Composable
fun AuthorCircle(author: Author, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Image(
            painter = rememberImagePainter(R.drawable.ic_account),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = author.name,
            color = Color.Black,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp),
            textAlign = TextAlign.Center,
            maxLines = 1,  // Limit to one line
            overflow = TextOverflow.Ellipsis  // Truncate with "..."
        )
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

@Preview(showBackground = true)
@Composable
fun BookcardPreview(){
    BookCard(
        BookDetail(
        "ajdnfafdas",
        "nfkajsndfkajnfa",
        589F,
        true,
            14.0),
        onFavoriteClick = {

        }
    )
}
@Preview(showBackground = true)
@Composable
fun BookcardPreviewa(){
    BookCardHorizontal(
        BookDetail(
        "ajdnfafdas",
        "nfkajsndfkajnfa",
        589F,
        true,
            10.0),
        onFavoriteClick = {

        }
    )
}