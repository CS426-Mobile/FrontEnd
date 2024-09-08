package com.example.bookstore

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.ui.theme.mainColor
import java.sql.Time

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun BookDetailScreen(navController: NavHostController, bookName: String?) {
    val books = listOf(
        BookDetail(
            title = "The Alchemist",
            author = "Paulo Coelho",
            rating = 4.5f,
            isFavorite = true,
        ),
        BookDetail(
            title = "To Kill a Mockingbird",
            author = "Harper Lee",
            rating = 4.8f,
            isFavorite = false,
        ),
        BookDetail(
            title = "1984",
            author = "George Orwell",
            rating = 4.6f,
            isFavorite = false,
        ),
        BookDetail(
            title = "Pride and Prejudice",
            author = "Jane Austen",
            rating = 4.5f,
            isFavorite = false,
        ),
        BookDetail(
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            rating = 4.3f,
            isFavorite = true,
        ),
        BookDetail(
            title = "Moby Dick",
            author = "Herman Melville",
            rating = 4.1f,
            isFavorite = false,
        )
    )

    val scrollState = rememberLazyListState()

    Scaffold (
        topBar = {
            CustomTopAppBar(title = "Book", navController = navController, isBack = true)
        },
        content = { paddingValues ->
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cart), // Thay bằng hình bìa sách của bạn
                            contentDescription = "Book Cover",
                            modifier = Modifier.size(153.dp, 230.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Battle of 1917",
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.Yellow)
                                Text(
                                    text = String.format(
                                        "%.1f",
                                        calculateAverageRating(listOf(123, 45, 30, 15, 5))
                                    ) + " (${listOf(123, 45, 30, 15, 5).sum()} ratings)", color = Color.Gray
                                )
                            }
                            FlowRow(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Chip(text = "Drama")
                                Chip(text = "Romance")
                                Chip(text = "Mysteries")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("In Stock", color = Color.Green, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "$9.99",
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        IconButton(onClick = { /* Thêm vào yêu thích */ }) {
                            Icon(Icons.Default.FavoriteBorder, contentDescription = "Add to Favorite")
                        }
                    }
                }

                // Phần thứ hai: Tác giả và thông tin

                item{
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    AuthorItem(author = Author(
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
                    ), navController = navController, onButtonFollow = {})
                }

                // Phần thứ ba: Mô tả sách
                var textDescription =
                    "From athe glamorous San Francisco social scene of the 1920s, through war and the social changes of the ’60s  to the rise of Silicon Valley today, this extraordinary novel takes us on a family odyssey that is both heartbreaking and exhilarating, showing how the roots of misfortune and strength can trace through generations."
                item {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Description", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    ExpandableText(textDescription)
                }

                // Phần thứ tư: Thông tin chi tiết sách
                item {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Product Information", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    ProductInformationRow("Publication Date", "May 1, 2016")
                    ProductInformationRow("Language", "English")
                    ProductInformationRow("Weight", "15.7 ounces")
                    ProductInformationRow("Pages", "374")
                }

                // Phần thứ năm: Đánh giá khách hàng
                item {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Customer Reviews", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    RatingSection(ratings = listOf(123, 45, 30, 15, 5))
                }

                // Phần cuối: Các sách liên quan
                item {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Related Books", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    LazyRow {
                        items(books) { book ->
                            BookCard(
                                BookDetail(
                                    title = book.title,
                                    author = book.author,
                                    rating = book.rating,
                                    isFavorite = book.isFavorite
                                ),
                                navController = navController,
                                onFavoriteClick = {}
                            )
                        }
                    }
                }

                // Nút thêm vào giỏ hàng
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Them vo gio hang
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = mainColor)
                    ) {
                        Text("Add to Cart", color = Color.White)
                    }
                }
            }
        }
    )
}

@Composable
fun ProductInformationRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold)
    }
}


@Composable
fun ProductInfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text(text = value, modifier = Modifier.weight(1f), color = Color.Gray)
    }
}


@Preview(showBackground = true)
@Composable
fun BookDetailScreenPreview() {
    BookDetailScreen(navController = rememberNavController(), "adaa")
}

@Composable
fun calculateAverageRating(ratings: List<Int>): Float {
    val totalRatings = ratings.sum()
    val averageRating = if (totalRatings > 0) {
        ratings.mapIndexed { index, count -> (5 - index) * count }.sum() / totalRatings.toFloat()
    } else {
        0f
    }
    return averageRating
}

@Composable
fun RatingSection(ratings: List<Int>) {
    val totalRatings = ratings.sum() // Tổng số lượt đánh giá
    val averageRating = calculateAverageRating(ratings = ratings)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Phần hiển thị số sao đánh giá trung bình bên trái
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%.1f", averageRating),
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )
            Text(text = "out of 5", color = Color.Gray)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Phần hiển thị các hàng đánh giá bên phải
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 5 downTo 1) {
                RatingRow(stars = i, count = ratings[i - 1])
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RatingRow(stars: Int, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Hiển thị các ngôi sao đánh giá
        Row {
            repeat(5-stars){
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = " ",
                    tint = Color.Transparent,
                    modifier = Modifier.size(16.dp)
                )
            }
            repeat(stars) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Hiển thị thanh đánh giá
        LinearProgressIndicator(
            progress = count / 100f, // Tỉ lệ % của số lượt đánh giá
            modifier = Modifier
                .weight(1f)
                .height(8.dp),
            color = mainColor, // Màu của thanh đánh giá
            backgroundColor = Color(0xFFF0F0F0)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Hiển thị số lượt đánh giá
        Text(
            text = count.toString(),
            color = Color.Gray
        )
    }
}

@Composable
fun ExpandableText(
    text: String,
    minimizedMaxLines: Int = 4 // Số dòng tối đa khi thu gọn
) {
    var isExpanded by remember { mutableStateOf(false) } // Trạng thái mở rộng hoặc thu gọn

    Column(modifier = Modifier.padding(8.dp)) {
        // Văn bản hiển thị
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines, // Hiển thị toàn bộ hoặc giới hạn
            style = MaterialTheme.typography.body2,
            overflow = TextOverflow.Ellipsis // Cắt đoạn văn khi vượt quá số dòng
        )

        // Chỉ hiển thị nút "Read more" hoặc "Show less" khi văn bản vượt quá số dòng tối đa
        if (text.length > 200) { // Điều chỉnh độ dài theo ý muốn
            Text(
                text = if (isExpanded) "Show less" else "Read more",
                color = Color.Gray,
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(top = 4.dp),
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookCardPreview() {
    var isFavorite by remember { mutableStateOf(false) }

    BookCard(
        BookDetail(
        title = "Jan Rombouts Een Renaissann",
        author = "Mercatorfonds",
        rating = 4.5f,
        isFavorite = isFavorite),
        navController = rememberNavController(),
        onFavoriteClick = { isFavorite = !isFavorite } // Thay đổi trạng thái khi nhấn vào
    )
}


