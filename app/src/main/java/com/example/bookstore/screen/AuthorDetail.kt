package com.example.bookstore.screen

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.components.BookCard
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.components.ExpandableText
import com.example.bookstore.model.AuthorResponse
import com.example.bookstore.model.SimpleBookResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.AuthorViewModel
import com.example.bookstore.viewmodel.BookViewModel
import com.example.bookstore.viewmodel.CustomerFollowViewModel
import com.example.bookstore.viewmodel.UserViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun AuthorDetailScreen(navController: NavHostController, authorName: String) {
    val authorViewModel: AuthorViewModel = viewModel()
    val customerFollowViewModel: CustomerFollowViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    // Local state to hold author data, books, categories, and follow status
    var author by remember { mutableStateOf<AuthorResponse?>(null) }
    var books by remember { mutableStateOf<List<SimpleBookResponse>?>(null) }
    var numBooks by remember { mutableStateOf<Int?>(null) }
    var categories by remember { mutableStateOf<List<String>?>(null) }
    var isFollowing by remember { mutableStateOf<Boolean?>(null) }
    var userEmail by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var numFollower by remember { mutableStateOf(0) }

    // Fetch author information, number of books, categories, and follow status
    LaunchedEffect(authorName) {
        // Fetch user info to get the user's email
        userViewModel.getUserInfo { success, userInfo, _ ->
            if (success && userInfo != null) {
                userEmail = userInfo.user_email

                // Fetch author info
                authorViewModel.getAuthorInfo(authorName) { success, result ->
                    if (success && result != null) {
                        author = result
                        numFollower = author!!.num_follower

                        // Fetch follow status
                        customerFollowViewModel.queryFollow(authorName, userEmail!!) { followSuccess, following ->
                            if (followSuccess) {
                                isFollowing = following
                            }

                            // Fetch number of books by this author
                            bookViewModel.getNumBooksByAuthor(authorName) { bookSuccess, numBooksResult ->
                                if (bookSuccess && numBooksResult != null) {
                                    numBooks = numBooksResult
                                }

                                // Fetch author categories
                                bookViewModel.getAuthorCategories(authorName) { categorySuccess, categoriesResult ->
                                    if (categorySuccess && categoriesResult != null) {
                                        categories = categoriesResult
                                    }

                                    // Fetch books by this author
                                    bookViewModel.getBooksByAuthor(authorName) { booksSuccess, booksResult ->
                                        if (booksSuccess && booksResult != null) {
                                            books = booksResult
                                        }
                                        isLoading = false
                                    }
                                }
                            }
                        }
                    } else {
                        errorMessage = "Failed to load author information."
                        isLoading = false
                    }
                }
            }
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
        Scaffold(
            topBar = {
                CustomTopAppBar(title = "Author Detail", isBack = true, navController = navController)
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {

                    // Avatar và tên của tác giả
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(author!!.author_image), // Fetch the image from the URL
                            contentDescription = "Author Avatar",
                            modifier = Modifier
                                .size(100.dp)  // Set the size to 100.dp diameter
                                .clip(CircleShape)  // Clip the image into a circle
                                .background(Color.Gray),  // Optional background color
                            contentScale = ContentScale.Crop  // Crop the image to fit the circle
                        )
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = author!!.author_name,
                                style = MaterialTheme.typography.h5.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 28.sp // Adjust the line height (spacing between lines)
                                ),
                                color = mainColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${numFollower} Followers",
                            style = MaterialTheme.typography.body1,
                            color = Color.Gray
                        )
                        Text(
                            text = "${numBooks ?: 0} ${if (numBooks == 1) "Book" else "Books"}", // Show "Book" or "Books" based on numBooks
                            style = MaterialTheme.typography.body1,
                            color = Color.Gray
                        )

                        // Follow Button
                        Button(
                            onClick = {
                                userEmail?.let { email ->
                                    if (isFollowing == true) {
                                        // Unfollow the author
                                        customerFollowViewModel.toggleFollow(email, author!!.author_name) { success, _ ->
                                            if (success) {
                                                isFollowing = false
                                                numFollower = numFollower - 1
                                            }
                                        }
                                    } else {
                                        // Follow the author
                                        customerFollowViewModel.toggleFollow(email, author!!.author_name) { success, _ ->
                                            if (success) {
                                                isFollowing = true
                                                numFollower = numFollower + 1
                                            }
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFollowing == true) Color.Gray else mainColor,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.width(116.dp) // Set width for consistency
                        ) {
                            Text(
                                text = if (isFollowing == true) "Following" else "Follow",
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                    ) {

                        item {
                            Text(
                                text = "About",
                                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                            )
                            ExpandableText(
                                text = author!!.about,
                                minimizedMaxLines = 4 // Số dòng tối đa khi thu gọn
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            Text(
                                text = "Books",
                                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        // Books section from API
                        if (books != null && books!!.isNotEmpty()) {
                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    items(books!!) { book ->
                                        BookCard(
                                            book = book,
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        } else {
                            // Show fallback message if no books are found
                            item {
                                Text(
                                    text = "No books available by this author.",
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        item {
                            Text(
                                text = "Categories",
                                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                            )
                            FlowRow(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                categories?.forEach { category ->
                                    Chip(category)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        )
    }
}

// Sample Chip composable for category display
@Composable
fun Chip(text: String) {
    val lighterMainColor = mainColor.copy(alpha = 0.2f) // Adjust the alpha for a lighter color
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(lighterMainColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = text, color = Color.Black)
    }
}
