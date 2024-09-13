package com.example.bookstore.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
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
import com.example.bookstore.components.AuthorHorizontalItem
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.model.AuthorResponse
import com.example.bookstore.viewmodel.AuthorViewModel

@Composable
fun ListAuthor(navController: NavHostController) {
    val authorViewModel: AuthorViewModel = viewModel()
    var searchQuery by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }
    var listAuthor by remember { mutableStateOf<List<AuthorResponse>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch authors from ViewModel
    LaunchedEffect(Unit) {
        authorViewModel.getAllAuthors { success, result ->
            if (success && result != null) {
                listAuthor = result
            } else {
                errorMessage = "Failed to load authors."
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Authors", navController = navController, isBack = true)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = Color.White)
            ) {
                SearchBarHolder(
                    navController = navController
                )

                when {
                    isLoading -> {
                        // Loading state
                        Box(modifier = Modifier.fillMaxSize().background(color = Color.White), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.Gray)
                        }
                    }
                    errorMessage != null -> {
                        // Error state
                        Text(
                            text = errorMessage!!,
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    listAuthor != null -> {
                        // Display the list of authors
                        LazyColumn (modifier = Modifier.padding(16.dp)) {
                            items(listAuthor!!) { author ->
                                AuthorHorizontalItem(
                                    author = author,
                                    navController = navController,
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
