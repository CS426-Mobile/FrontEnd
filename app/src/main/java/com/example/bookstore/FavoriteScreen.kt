package com.example.bookstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bookstore.components.BookCardHorizontal
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.model.CustomerFavoriteResponse
import com.example.bookstore.viewmodel.CustomerFavoriteViewModel
import com.example.bookstore.viewmodel.UserViewModel

@Composable
fun FavoriteScreen(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()
    val customerFavoriteViewModel: CustomerFavoriteViewModel = viewModel()
    var userEmail by remember { mutableStateOf<String?>(null) }
    var favoriteBooks by remember { mutableStateOf<List<CustomerFavoriteResponse>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch user info (email) and favorite books
    LaunchedEffect(Unit) {
        userViewModel.getUserInfo { success, userInfo, error ->
            if (success && userInfo != null) {
                userEmail = userInfo.user_email

                // Fetch favorite books after getting the user's email
                customerFavoriteViewModel.queryCustomerFavorite(userEmail!!) { success, result ->
                    if (success && result != null) {
                        favoriteBooks = result
                    } else {
                        errorMessage = "Failed to load favorite books."
                    }
                    isLoading = false
                }
            } else {
                errorMessage = error ?: "Failed to fetch user info."
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Favorites", navController = navController, isCart = true)
        },
        content = { paddingValues ->
            when {
                isLoading -> {
                    // Show loading indicator
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
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
                            .background(Color.White)
                            .padding(paddingValues),
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
                favoriteBooks.isNullOrEmpty() -> {
                    // Show message if no favorite books are available
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No favorite books.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    // Display list of favorite books
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(paddingValues),
                    ) {
                        items(favoriteBooks!!) { book ->
                            BookCardHorizontal(
                                book = book,
                                navController = navController,
                                onFavoriteClick = {
                                    // Remove the clicked book from the favorite list and delete from API
                                    customerFavoriteViewModel.deleteCustomerFavorite(userEmail!!, book.book_name) { success, _ ->
                                        if (success) {

                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}