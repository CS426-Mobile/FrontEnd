package com.example.bookstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.ui.theme.mainColor

@Composable
fun ListOrder(navController: NavHostController) {
    // Dữ liệu mẫu cho các sách trong giỏ hàng
    var orders  = sampleOrders()

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Order", navController = navController)
        },
        content = { paddingValues ->
            if (orders.isEmpty()) {
                // Hiển thị thông báo mờ khi không có đơn hàng
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No orders found.",
//                        style = MaterialTheme.typography.h6,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Hiển thị danh sách đơn hàng theo dạng lưới
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Số cột là 2
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp), // Khoảng cách giữa các hàng
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách giữa các cột
                ) {
                    items(orders) { order ->
                        OrderCard(order = order)
                    }
                }
            }
        }
    )
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Order ID: ${order.id}", fontWeight = FontWeight.Bold)
            Text(text = "Date: ${order.date}", color = Color.Gray)
            Text(text = "Total: $${order.totalPrice}", fontWeight = FontWeight.Medium)
            Text(text = "Books: ${order.totalBooks}", color = Color.Gray)
        }
    }
}

data class Order(
    val id: String,
    val date: String,
    val totalPrice: Int,
    val totalBooks: Int
)

fun sampleOrders(): List<Order> {
    return listOf(
        Order("12345", "2024-09-05", 54, 3),
        Order("12346", "2024-09-06", 75, 5),
        Order("12347", "2024-09-07", 30, 2),
        Order("12348", "2024-09-08", 60, 4),
        Order("12349", "2024-09-09", 40, 1)
    )
}


@Preview(showBackground = true)
@Composable
fun ListOrderPreview() {
    ListOrder(navController = rememberNavController())
}