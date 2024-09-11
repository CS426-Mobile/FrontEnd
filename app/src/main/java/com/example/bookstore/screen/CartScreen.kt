package com.example.bookstore.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bookstore.Screen
import com.example.bookstore.components.BookCardHorizontal
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.model.CustomerCartResponse
import com.example.bookstore.model.CustomerFavoriteResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.CustomerCartViewModel
import com.example.bookstore.viewmodel.OrderViewModel
import com.example.bookstore.viewmodel.UserViewModel

@Composable
fun CartScreen(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()
    val customerCartViewModel: CustomerCartViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()

    var userEmail by remember { mutableStateOf<String?>(null) }
    var cartItems by remember { mutableStateOf<List<CustomerCartResponse>?>(null) }
    var totalPrice by remember { mutableStateOf(0f) }
    var numItems by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val deliveryFee = 5f

    // Fetch user info (email), cart items, and total price
    LaunchedEffect(Unit) {
        userViewModel.getUserInfo { success, userInfo, error ->
            if (success && userInfo != null) {
                userEmail = userInfo.user_email

                // Fetch cart items after getting the user's email
                customerCartViewModel.queryCustomerCartBooks(userEmail!!) { cartSuccess, cartResult ->
                    if (cartSuccess && cartResult != null) {
                        cartItems = cartResult
                        numItems = cartItems!!.size
                    } else {
                        errorMessage = "Failed to load cart items."
                    }

                    // Fetch total price after cart items are loaded
                    customerCartViewModel.calculateTotalPrice(userEmail!!) { priceSuccess, priceResult ->
                        if (priceSuccess && priceResult != null) {
                            totalPrice = priceResult
                        } else {
                            errorMessage = "Failed to load total price."
                        }
                        isLoading = false
                    }
                }
            } else {
                errorMessage = error ?: "Failed to fetch user info."
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Shopping Cart", navController = navController, isBack = true)
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
                    cartItems.isNullOrEmpty() -> {
                        // Show message if no cart items are available
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Your cart is empty.",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else -> {
                        // Display the cart items and the total price
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp)
                        ) {
                            // LazyColumn for cart items
                            LazyColumn(
                                modifier = Modifier.weight(1f)
                                    .background(Color.White)
                            ) {
                                items(cartItems!!) { book ->
                                    BookCardHorizontal(
                                        book = CustomerFavoriteResponse(book.book_name, book.author_name, book.book_image, book.average_rating, book.price), navController = navController,
                                        onFavoriteClick = {
                                            customerCartViewModel.deleteCustomerCartBook(userEmail!!, book.book_name) { success, _ ->
                                                if (success) {
                                                    // Update total price dynamically
                                                    customerCartViewModel.calculateTotalPrice(userEmail!!) { priceSuccess, priceResult ->
                                                        if (priceSuccess && priceResult != null) {
                                                            totalPrice = priceResult
                                                            numItems = numItems - 1
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        favoriteScreen = 0
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
                                    .clickable { /* Navigate to address screen */ }
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "43 Bourke Street, Newbridge NSW 837 Raffles...",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Arrow")
                            }

                            // Delivery Fee and Total Price section
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
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
                                    "$${totalPrice + deliveryFee}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${numItems} items",
                                    color = Color.Gray,
                                )
                                Text(
                                    text = "Include taxes",
                                    color = Color.Gray,
                                )
                            }

                            Button(
                                onClick = {
                                    // Insert customer order
                                    if (userEmail != null) {
                                        orderViewModel.insertCustomerOrder(userEmail!!) { orderSuccess, message ->
                                            if (orderSuccess) {
                                                navController.navigate(Screen.Success.route) // Navigate to success screen
                                            }
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = mainColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .height(56.dp)
                            ) {
                                Text("Buy", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    )
}