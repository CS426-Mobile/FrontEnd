package com.example.bookstore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.model.OrderResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.OrderViewModel
import com.example.bookstore.viewmodel.UserViewModel

@Composable
fun ListOrder(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()

    var userEmail by remember { mutableStateOf<String?>(null) }
    var orders by remember { mutableStateOf<List<OrderResponse>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch user info and then the list of orders
    LaunchedEffect(Unit) {
        userViewModel.getUserInfo { success, userInfo, _ ->
            if (success && userInfo != null) {
                userEmail = userInfo.user_email

                // Fetch the list of orders for this user
                orderViewModel.queryCustomerOrder(userEmail!!) { orderSuccess, orderResult ->
                    if (orderSuccess && orderResult != null) {
                        orders = orderResult
                    } else {
                        errorMessage = "Failed to load orders."
                    }
                    isLoading = false
                }
            } else {
                errorMessage = "Failed to fetch user info."
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Order", navController = navController, isCart = true)
        },
        content = { paddingValues ->
            when {
                isLoading -> {
                    // Show loading indicator
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Gray)
                    }
                }

                errorMessage != null -> {
                    // Show error message
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                orders.isNullOrEmpty() -> {
                    // Show message when there are no orders
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No orders found.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    // Show list of orders in a grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1), // 1 column (for full-width)
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(orders!!) { order ->
                            OrderCard(order = order, navController = navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun OrderCard(order: OrderResponse, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = mainColor.copy(alpha = 1f),  // Blur shadow
                spotColor = mainColor.copy(alpha = 1f)
            )
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate(route = Screen.Order.passOrder(order.order_code))
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.White)
                .padding(start = 16.dp),  // Add padding at the start
            verticalArrangement = Arrangement.Center,  // Center the content vertically
            horizontalAlignment = Alignment.Start      // Align the content at the start horizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Add space between text elements
            Text(text = "Order ID: ${order.order_code}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp)) // Add space between text elements
            Text(text = "Total: $${order.total_price}", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp)) // Add space between text elements
            Text(text = "Number of items: ${order.num_books}", color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp)) // Add space between text elements
        }

    }
}
