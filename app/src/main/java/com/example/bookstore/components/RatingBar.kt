package com.example.bookstore.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun RatingBar(rating: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (i <= rating) Color(0xFFFFC107) // Vàng cho sao được tô
                else if (i - rating < 1) Color(0xFFFFC107).copy(alpha = 0.5f) // Màu vàng nhạt cho sao tô một phần
                else Color.Gray
            )
        }
    }
}