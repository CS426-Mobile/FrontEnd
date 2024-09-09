package com.example.bookstore.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.Screen
import com.example.bookstore.model.AuthorResponse

@Composable
fun AuthorItem(author: AuthorResponse, navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { navController.navigate(route = Screen.Author.passAuthorName(author.author_name)) }
            .padding(8.dp)
    ) {
        // Circular image with a diameter of 100.dp
        Image(
            painter = rememberAsyncImagePainter(author.author_image),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)  // Set the size to 100.dp diameter
                .clip(CircleShape)  // Clip the image into a circle
                .background(Color.Gray),  // Optional background color
            contentScale = ContentScale.Crop  // Crop the image to fit the circle
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Text constrained to the width of the image
        Box(
            modifier = Modifier
                .widthIn(max = 100.dp)  // Constrain the width to match the image diameter
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = author.author_name,
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,  // Limit to one line
                overflow = TextOverflow.Ellipsis  // Truncate with "..."
            )
        }
    }
}