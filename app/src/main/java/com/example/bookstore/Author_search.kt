package com.example.bookstore

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.ui.theme.textColor
import kotlinx.coroutines.launch



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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@SuppressLint("RememberReturnType", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthorScreen(navController: NavHostController) {

    // Dữ liệu mẫu cho Author và Book
    val sampleAuthor = Author(
        name = "Averie Stecanella",
        followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
        books = 873,
        isFollowing = false,
        categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
        about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
        booksList = listOf(
            Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
            Book("Believe in Eternal Dark", "Laura Jones"),
            Book("The Secret About Us", "Elizabeth McKean"),
            Book("Love in the Time of War", "Michael Foster"),
            Book("Shadows of the Past", "Alice Munro")
        )
    )
    var author = sampleAuthor

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        stickyHeader {
            CustomTopAppBar(title = "Author", isBack = true, navController = navController)
            // Avatar và tên của tác giả
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_account), // Thay bằng ảnh avatar của bạn
                    contentDescription = "Author Avatar",
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = author.name,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                    color = mainColor // Màu cam cho tên tác giả
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Thông tin số lượng người following, số lượng sách và nút Follow
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
//                Column {
                Text(
                    text = "${author.followers} Followers",
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray
                )
                Text(
                    text = "${author.books} Books",
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray
                )
//                }

                Button(
                    onClick = {
                        author = author.copy(isFollowing = !author.isFollowing)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (author.isFollowing) Color.Gray else mainColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(if (author.isFollowing) "Following" else "Follow", color = if (author.isFollowing) Color.Gray else Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Categories tham gia
            Text(
                text = "Categories",
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
            )
            FlowRow(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                author.categories.forEach { category ->
                    Chip(category)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "About",
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = author.about,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Books
            Text(
                text = "Books",
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(author.booksList) { book ->
                    BookCard(book = book)
                }
            }
        }
    }
}

@Composable
fun Chip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF0F0F0))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = text, color = Color.Black)
    }
}


@Preview(showBackground = true)
@Composable
fun AuthorScreenPreview() {
    AuthorScreen(navController = rememberNavController())
}
