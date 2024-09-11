package com.example.bookstore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
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
import com.example.bookstore.components.AuthorHorizontalItem
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.model.AuthorResponse
import com.example.bookstore.model.CustomerFollowResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.CustomerFollowViewModel
import com.example.bookstore.viewmodel.UserViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountScreen(navController: NavHostController) {
    val customerFollowViewModel: CustomerFollowViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    var userEmail by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var followedAuthors by remember { mutableStateOf<List<CustomerFollowResponse>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch user info and the list of authors the user follows
    LaunchedEffect(Unit) {
        userViewModel.getUserInfo { success, userInfo, _ ->
            if (success && userInfo != null) {
                userEmail = userInfo.user_email

                // Fetch the list of followed authors
                customerFollowViewModel.queryCustomerFollow(userEmail!!) { followSuccess, authors ->
                    if (followSuccess && authors != null) {
                        followedAuthors = authors
                    } else {
                        errorMessage = "Failed to load followed authors."
                    }
                    isLoading = false
                }
            } else {
                errorMessage = "Failed to load user info."
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Account", navController = navController, isCart = true)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(start = 16.dp, end = 16.dp)
            ) {

                if (isLoading) {
                    // Show loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Gray)
                    }
                } else if (errorMessage != null) {
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
                } else {
                    LazyColumn(
                        modifier = Modifier.background(Color.White)
                    ) {
                        // Address section
                        item {
                            Text(
                                text = "Address",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { /* Open address screen */ }
                            ) {
                                Text(
                                    text = "43 Bourke Street, Newbridge NSW 837 R...",
                                    color = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Account section
                        item {
                            Text(
                                text = "Account",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )

                            ListItem(
                                icon = {
                                    Icon(
                                        Icons.Default.Lock,
                                        contentDescription = "Change Password",
                                        tint = mainColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                },
                                text = { Text("Change Password") },
                                trailing = {
                                    Icon(
                                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = "Change Password",
                                        modifier = Modifier.size(28.dp)
                                    )
                                },
                                modifier = Modifier.clickable { /* Open change password screen */}
                            )

                            ListItem(
                                icon = {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Sign Out",
                                        tint = mainColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                },
                                text = { Text("Sign Out") },
                                trailing = {
                                    Icon(
                                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = "Sign Out",
                                        modifier = Modifier.size(28.dp)
                                    )
                                },
                                modifier = Modifier.clickable {
                                    navController.popBackStack(route = "login", inclusive = true)
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Following section
                            Text(
                                text = "Following",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                        }

                        // List of followed authors
                        followedAuthors?.let { authors ->
                            items(authors) { author ->
                                AuthorHorizontalItem(
                                    author = AuthorResponse(
                                        author_name = author.author_name,
                                        num_follower = author.num_follower,
                                        author_image = author.author_image,
                                        about = ""
                                    ),
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

