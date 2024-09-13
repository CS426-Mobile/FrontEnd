package com.example.bookstore.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.Screen
import com.example.bookstore.model.AuthorResponse
import com.example.bookstore.model.CustomerFavoriteResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.CustomerFollowViewModel
import com.example.bookstore.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun BookCardHorizontal(
    book: CustomerFavoriteResponse,
    onFavoriteClick: () -> Unit,  // Callback to handle favorite logic
    navController: NavHostController,
    favoriteScreen: Int = 1
) {
    var isVisible by remember { mutableStateOf(true) }  // Track visibility for this specific book card
    var isDone by remember { mutableStateOf(false) }

    // Animate the visibility of the book card
    AnimatedVisibility(
        visible = isVisible,
        exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()  // Slide out and fade out
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp, start = 24.dp, end = 24.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = mainColor.copy(alpha = 0.8f),  // Blur shadow
                    spotColor = mainColor.copy(alpha = 1f)
                )
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .clickable { navController.navigate(route = Screen.Book.passBookName(book.book_name)) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book image
            Image(
                painter = rememberAsyncImagePainter(model = book.book_image),
                contentDescription = "Book Cover",
                modifier = Modifier.size(80.dp, 100.dp),
                contentScale = ContentScale.Crop  // Crop the image to fit
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Display book information
            Column(modifier = Modifier.weight(1f)) {
                Text(text = book.book_name, fontWeight = FontWeight.Bold)
                Text(text = book.author_name, color = Color.Gray)
                RatingBar(rating = book.average_rating)
            }

            Column {
                if (favoriteScreen <= 1) {
                    IconButton(
                        onClick = {
                            isVisible = false  // Trigger the disappearing animation
                        }
                    ) {
                        Icon(
                            imageVector = if (favoriteScreen == 1) (Icons.Default.Favorite) else (Icons.Default.Delete),
                            contentDescription = "Remove from Favorites",
                            tint = Color.Red
                        )
                    }
                }
                Text("$${book.price}", color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            if (favoriteScreen > 1)
                Spacer(modifier = Modifier.width(10.dp))
        }
    }

    // After the animation finishes, invoke the onFavoriteClick logic for deletion
    if (!isVisible && !isDone) {
        LaunchedEffect(Unit) {
            delay(600)  // Wait for the animation to finish
            onFavoriteClick()
            isDone = true// Trigger the deletion logic
        }
    }
}


// Composable hiển thị thông tin một tác giả
// onButtonFollow: bam nut follow / unfollow
@Composable
fun AuthorHorizontalItem(
    author: AuthorResponse,
    navController: NavHostController
) {
    val customerFollowViewModel: CustomerFollowViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    var isFollowing by remember { mutableStateOf<Boolean?>(null) }
    var userEmail by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var numFollower by remember { mutableStateOf(author.num_follower) }


    LaunchedEffect(Unit) {
        // Fetch user info to get email
        userViewModel.getUserInfo { success, userInfo, _ ->
            if (success && userInfo != null) {
                userEmail = userInfo.user_email

                // Check if user is following the author
                customerFollowViewModel.queryFollow(author.author_name, userEmail!!) { success, following ->
                    if (success) {
                        isFollowing = following
                    }
                    isLoading = false // Data fetching is complete
                }
            } else {
                isLoading = false // Data fetching is complete
            }
        }
    }

    // Handle follow/unfollow toggle
    fun toggleFollow() {
        userEmail?.let { email ->
            if (isFollowing == true) {
                // Unfollow the author
                customerFollowViewModel.toggleFollow(email, author.author_name) { success, _ ->
                    if (success) {
                        isFollowing = false
                        numFollower = numFollower - 1
                    }
                }
            } else {
                // Follow the author
                customerFollowViewModel.toggleFollow(email, author.author_name) { success, _ ->
                    if (success) {
                        isFollowing = true
                        numFollower = numFollower + 1
                    }
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)
            .shadow(
                elevation = 14.dp,
                //shape = RoundedCornerShape(16.dp),
                ambientColor = mainColor.copy(alpha = 0.8f),  // Blur shadow
                spotColor = mainColor.copy(alpha = 1f)
            )
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .clickable {
            navController.navigate(route = Screen.Author.passAuthorName(author.author_name))
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        // Author information or placeholder
        if (isLoading) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .fillMaxSize(),  // Make the box fill the available space
                contentAlignment = Alignment.Center // Center the content both vertically and horizontally
            ) {
                Text(
                    text = "Loading author...",
                    fontSize = 15.sp,
                    color = Color.Gray,
                )
            }

        } else {
            // Author image
            author.author_image?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        //.clip(CircleShape)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                Text(
                    text = author.author_name,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 15.sp
                )
                Text(
                    text = "${numFollower ?: 0} Followers",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Button(
                onClick = { toggleFollow() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFollowing == true) Color(0xFFB0B0B0) else mainColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.width(116.dp) // Set a fixed width to accommodate both "Follow" and "Following"
            ) {
                Text(
                    text = if (isFollowing == true) "Following" else "Follow",
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}