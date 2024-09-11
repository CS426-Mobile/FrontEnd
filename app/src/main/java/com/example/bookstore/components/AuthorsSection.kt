package com.example.bookstore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bookstore.Screen
import com.example.bookstore.model.AuthorResponse
import com.example.bookstore.ui.theme.mainColor
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
                errorMessage = null
            } else if (result.isNullOrEmpty()) {
                errorMessage = "No authors found"
            } else {
                errorMessage = "Failed to load authors"
            }
            isLoading = false
        }
    }

    // UI Layout with header and authors list
    Column(modifier = Modifier.fillMaxWidth().padding(top = 0.dp, bottom = 8.dp)) {
        // Row for "Popular Authors" title and "See all" button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Authors",
                color = mainColor,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "See all > ",
                color = mainColor,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { navController.navigate(Screen.ListAuthor.route) }
            )
        }

        when {
            isLoading -> {
                // Show placeholder while loading
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(5) { // Show 5 placeholder items
                        AuthorItemPlaceholder()
                    }
                }
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
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(authors!!) { author ->
                        AuthorItem(
                            author = author,
                            navController = navController
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
fun AuthorItemPlaceholder() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
    ) {
        // Placeholder image with a diameter of 100.dp
        Box(
            modifier = Modifier
                .size(90.dp)  // Set the size to 100.dp diameter
                .clip(CircleShape)
                .background(Color.Gray)  // Placeholder color
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Placeholder text constrained to the width of the image
        Box(
            modifier = Modifier
                .widthIn(max = 90.dp)  // Constrain the width to match the image diameter
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = "Loading...",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,  // Limit to one line
                overflow = TextOverflow.Ellipsis  // Truncate with "..."
            )
        }
    }
}






