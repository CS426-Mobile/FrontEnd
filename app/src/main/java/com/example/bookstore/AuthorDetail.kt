package com.example.bookstore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.components.BookCard
import com.example.bookstore.components.BookDetail
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.network.AuthorResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.AuthorViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun AuthorDetailScreen(navController: NavHostController, authorName: String) {
    val authorViewModel: AuthorViewModel = viewModel()

    // Local state to hold author data
    var author by remember { mutableStateOf<AuthorResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch author information
    LaunchedEffect(authorName) {
        authorViewModel.getAuthorInfo(authorName) { success, result ->
            if (success && result != null) {
                author = result
            } else {
                errorMessage = "Failed to load author information."
            }
            isLoading = false
        }
    }

    // Display content based on the fetched data
    if (isLoading) {
        // Loading state
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Gray)
        }
    } else if (errorMessage != null) {
        // Error state
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = errorMessage!!,
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    } else if (author != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            // Sticky header for the author image, name, followers, and books count
            stickyHeader {
                CustomTopAppBar(title = "Author Detail", isBack = true, navController = navController)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background)
                        .padding(bottom = 16.dp) // Add some padding to prevent overlap
                ) {
                    // Avatar and author name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_account), // Replace with author.avatar if available
                            contentDescription = "Author Avatar",
                            modifier = Modifier
                                .size(86.dp)
                                .clip(CircleShape)
                        )

                        Text(
                            text = author!!.author_name,
                            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                            color = mainColor // Orange color for author name
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Followers and books count
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${author!!.num_follower} Followers",
                            style = MaterialTheme.typography.body1,
                            color = Color.Gray
                        )
                        Text(
                            text = "873 Books", // Sample data for books
                            style = MaterialTheme.typography.body1,
                            color = Color.Gray
                        )

                        Button(
                            onClick = { /* Handle follow/unfollow action */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (author!!.num_follower > 0) mainColor else Color.Gray,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Follow", color = Color.White)
                        }
                    }
                }
            }

            // Rest of the content scrolls beneath the sticky header
            item {
                // Categories section (sample data)
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                )
                FlowRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Drama", "Mysteries", "Fantasy").forEach { category ->
                        Chip(category)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // About section
                Text(
                    text = "About",
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                )
                ExpandableText(
                    text = author!!.about,
                    minimizedMaxLines = 5 // Max lines when collapsed
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Books section (sample data)
            item {
                Text(
                    text = "Books",
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(listOf(
                        BookDetail("The Alchemist", "Paulo Coelho", 4.5f, true),
                        BookDetail("1984", "George Orwell", 4.6f, false),
                        BookDetail("Moby Dick", "Herman Melville", 4.1f, false)
                    )) { book ->
                        BookCard(
                            title = book.title,
                            author = book.author,
                            rating = book.rating,
                            isFavorite = book.isFavorite,
                            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/78/Image.jpg",
                            onFavoriteClick = {
                                // Toggle favorite status
                                book.isFavorite = !book.isFavorite
                            },
                            onClick = {
                                // Navigate to book detail screen
                                navController.navigate("bookDetail/${book.title}")
                            }
                        )
                    }
                }
            }
        }
    }
}

// Sample Chip composable for category display
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
fun AuthorDetailScreenPreview() {
    AuthorDetailScreen(navController = rememberNavController(), authorName = "Paulo Coelho")
}
