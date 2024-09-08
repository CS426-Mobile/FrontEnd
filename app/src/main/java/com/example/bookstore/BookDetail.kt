package com.example.bookstore

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.components.AuthorHorizontalItem
import com.example.bookstore.components.BookCard
import com.example.bookstore.components.BookDetail
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.components.ExpandableText
import com.example.bookstore.network.AuthorResponse
import com.example.bookstore.network.BookResponse
import com.example.bookstore.network.SimpleBookResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.AuthorViewModel
import com.example.bookstore.viewmodel.BookViewModel

@Composable
fun BookDetailScreen(navController: NavHostController, bookName: String?) {
    val bookViewModel: BookViewModel = viewModel()
    val authorViewModel: AuthorViewModel = viewModel()
    var book by remember { mutableStateOf<BookResponse?>(null) }
    var author by remember { mutableStateOf<AuthorResponse?>(null) }
    var relatedBooks by remember { mutableStateOf<List<SimpleBookResponse>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch book and author details
    LaunchedEffect(bookName) {
        if (bookName != null) {
            bookViewModel.getBookInfo(bookName) { success, result ->
                if (success && result != null) {
                    book = result
                    authorViewModel.getAuthorInfo(result.author_name) { authorSuccess, authorResult ->
                        if (authorSuccess && authorResult != null) {
                            author = authorResult
                        } else {
                            errorMessage = "Failed to load author information."
                        }
                    }
                    bookViewModel.getRelatedBooks(bookName) { relatedSuccess, relatedResult ->
                        if (relatedSuccess && relatedResult != null) {
                            relatedBooks = relatedResult
                        } else {
                            errorMessage = "Failed to load related books."
                        }
                    }
                } else {
                    errorMessage = "Failed to load book information."
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Book Detail", navController = navController, isBack = true)
        },
        content = { paddingValues ->
            if (errorMessage != null) {
                // Show error message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = errorMessage!!, color = Color.Gray, fontSize = 16.sp)
                }
            } else if (book != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    // Book Details Section
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = book!!.book_image),
                                contentDescription = "Book Cover",
                                modifier = Modifier.size(153.dp, 230.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp)
                            ) {
                                Text(
                                    text = book!!.book_name,
                                    style = MaterialTheme.typography.h6,
                                    fontWeight = FontWeight.Bold,
                                    color = mainColor,
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.Yellow)
                                    Text(
                                        text = String.format("%.1f", book!!.average_rating) + " (${book!!.num_5_star} ratings)",
                                        color = Color.Gray
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "$${book!!.price}",
                                    style = MaterialTheme.typography.h6,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }

                    // Author Section

                    // Remember to add calling API following or not
                    if (author != null) {
                        item {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            AuthorHorizontalItem(
                                authorName = author!!.author_name,
                                numFollower = author?.num_follower,
                                authorImage = author?.author_image,
                                following = true,
                                navController = navController,
                                onButtonFollow = {})
                        }
                    }

                    // Description Section
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                        )
                        ExpandableText(text = book!!.book_description)
                    }

                    // Product Information Section
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Product Information",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                        )
                        ProductInformationRow("Publication Date", book!!.public_date)
                        ProductInformationRow("Language", book!!.book_language)
                        ProductInformationRow("Weight", "${book!!.book_weight} oz")
                        ProductInformationRow("Pages", "${book!!.book_page}")
                    }

                    // Rating Section
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Customer Reviews",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                        )
                        RatingSection(ratings = listOf(book!!.num_5_star, book!!.num_4_star, book!!.num_3_star, book!!.num_2_star, book!!.num_1_star)) // Use your ratings data
                    }

                    // Related Books Section
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Related Books",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                        )
                        LazyRow {
                            items(relatedBooks ?: emptyList()) { relatedBook ->
                                BookCard(
                                    title = relatedBook.book_name,
                                    author = relatedBook.author_name,
                                    rating = relatedBook.average_rating,
                                    isFavorite = false, // Adjust as needed
                                    imageUrl = relatedBook.book_image,
                                    onFavoriteClick = { /* Handle favorite */ },
                                    navController = navController
                                )
                            }
                        }
                    }

                    // Add to Cart Button
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                // Add to cart logic
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

@Preview(showBackground = true)
@Composable
fun BookCardPreview() {
    var isFavorite by remember { mutableStateOf(false) }

    BookCard(
        title = "Jan Rombouts Een Renaissann",
        author = "Mercatorfonds",
        rating = 4.5f,
        isFavorite = isFavorite,
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/78/Image.jpg",
        onFavoriteClick = { isFavorite = !isFavorite }, // Thay đổi trạng thái khi nhấn vào
        navController = rememberNavController()
    )
}

