package com.example.bookstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.components.RatingBar
import com.example.bookstore.model.OrderDetailResponse
import com.example.bookstore.model.OrderResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.OrderViewModel
import com.example.bookstore.viewmodel.UserViewModel

@Composable
fun OrderDetail(navController: NavHostController, orderCode: String) {
    val userViewModel: UserViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()

    var userEmail by remember { mutableStateOf<String?>(null) }
    var order by remember { mutableStateOf<OrderResponse?>(null) }
    var orderItems by remember { mutableStateOf<List<OrderDetailResponse>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val deliveryFee = 5f

    // Fetch user email and then query the orders for this user
    LaunchedEffect(Unit) {
        userViewModel.getUserInfo { success, userInfo, _ ->
            if (success && userInfo != null) {
                userEmail = userInfo.user_email

                // Query all orders for the user
                orderViewModel.queryCustomerOrder(userEmail!!) { orderSuccess, orders ->
                    if (orderSuccess && orders != null) {
                        // Find the order by orderCode
                        order = orders.find { it.order_code == orderCode }

                        // If the order is found, fetch its details
                        if (order != null) {
                            orderViewModel.queryOrderDetail(orderCode) { detailSuccess, orderDetails ->
                                if (detailSuccess && orderDetails != null) {
                                    orderItems = orderDetails
                                } else {
                                    errorMessage = "Failed to load order details."
                                }
                                isLoading = false
                            }
                        } else {
                            errorMessage = "Order not found."
                            isLoading = false
                        }
                    } else {
                        errorMessage = "Failed to load orders."
                        isLoading = false
                    }
                }
            } else {
                errorMessage = "Failed to load user info."
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Order Detail", navController = navController, isBack = true)
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)  // Set white background
            ) {
                when {
                    isLoading -> {
                        // Show loading indicator
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.Gray)
                        }
                    }

                    errorMessage != null -> {
                        // Show error message
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage!!,
                                color = Color.Gray,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    order != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Order ID: ${order!!.order_code}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // LazyColumn for order items
                            if (orderItems != null && orderItems!!.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.weight(1f)
                                        .background(Color.White)
                                ) {
                                    items(orderItems!!) { item ->
                                        OrderDetailCard(
                                            book = item,
                                            navController = navController
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No items in this order.",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Delivery Address section
                            Text(
                                text = "Delivery Address",
                                modifier = Modifier.padding(horizontal = 16.dp),
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "43 Bourke Street, Newbridge NSW 837 Raffles...",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            // Delivery Fee and Total Price section
                            HorizontalDivider(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Delivery Fee", color = Color.Gray, fontSize = 13.sp)
                                Text("$${deliveryFee}")
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Price", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(
                                    "$${order!!.total_price + deliveryFee}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${order!!.num_books} items",
                                    color = Color.Gray,
                                )
                                Text(
                                    text = "Include taxes",
                                    color = Color.Gray,
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun OrderDetailCard(
    book: OrderDetailResponse,
    navController: NavHostController,
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
            androidx.compose.material.Text(text = book.book_name, fontWeight = FontWeight.Bold)
            androidx.compose.material.Text(text = book.author_name, color = Color.Gray)
            RatingBar(rating = book.average_rating)
        }

        Text("$${book.price}", color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}