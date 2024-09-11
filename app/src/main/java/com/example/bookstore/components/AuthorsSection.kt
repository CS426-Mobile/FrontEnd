package com.example.bookstore.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.bookstore.Screen
import com.example.bookstore.network.AuthorResponse
import com.example.bookstore.viewmodel.AuthorViewModel

@Composable
fun AuthorsSection(navController: NavHostController, authorViewModel: AuthorViewModel) {
    var authors by remember { mutableStateOf<List<AuthorResponse>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch authors data
    LaunchedEffect(Unit) {
        authorViewModel.getTop5PopularAuthors { success, result ->
            if (success && result != null && result.isNotEmpty()) {
                authors = result
            } else if (result.isNullOrEmpty()) {
                errorMessage = "No authors found"
            } else {
                errorMessage = "Failed to load authors"
            }
            isLoading = false
        }
    }

    // UI Layout with header and authors list
    Column(modifier = Modifier.fillMaxWidth()) {
        // Row for "Popular Authors" title and "See all" button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Authors",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "See all",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable { navController.navigate(Screen.ListAuthor.route) }
            )
        }

        // Main content section
        when {
            isLoading -> {
                // Display loading animation
                CircularProgressIndicator(color = Color.Gray, modifier = Modifier.padding(16.dp))
            }
            errorMessage != null -> {
                // Show error message
                Text(
                    text = errorMessage!!,
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            authors != null && authors!!.isNotEmpty() -> {
                // Show LazyRow with authors if data is available
                LazyRow(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(authors!!) { author ->
                        AuthorItem(
                            author = author,
                            onClick = { navController.navigate("author/${author.author_name}") }
                        )
                    }
                }
            }
            else -> {
                // Fallback message if no authors are found
                Text(
                    text = "No authors available at the moment.",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AuthorItem(author: AuthorResponse, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Image(
            painter = rememberImagePainter(author.author_image),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = author.author_name,
            color = Color.Black,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp),
            textAlign = TextAlign.Center,
            maxLines = 1,  // Limit to one line
            overflow = TextOverflow.Ellipsis  // Truncate with "..."
        )
    }
}




