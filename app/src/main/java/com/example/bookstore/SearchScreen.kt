package com.example.bookstore

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bookstore.components.AuthorItem
import com.example.bookstore.components.BookCardHorizontal
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.model.AuthorResponse
import com.example.bookstore.model.BookCategoryResponse
import com.example.bookstore.model.CustomerFavoriteResponse
import com.example.bookstore.model.SimpleAuthorResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.AuthorViewModel
import com.example.bookstore.viewmodel.BookViewModel

@SuppressLint("RememberReturnType")
@Composable
fun SearchScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var authors by remember { mutableStateOf<List<SimpleAuthorResponse>?>(null) }
    var books by remember { mutableStateOf<List<BookCategoryResponse>?>(null) }

    val authorViewModel: AuthorViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()
    val focusManager = LocalFocusManager.current

    // Focus the search bar only when the screen loads for the first time
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Fetch data dynamically as the user types in the search bar
    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()) {
            // Fetch matching authors
            authorViewModel.getMatchingAuthors(searchText) { success, result ->
                if (success) authors = result
            }
            // Fetch matching books
            bookViewModel.getBooksByMatchingString(bookInput = searchText) { success, result ->
                if (success) books = result
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Search", navController = navController, isCart = true, isBack = true)
        },
        content = { paddingValues ->
            // Parent column with tap gesture detection to lose focus when tapping outside the search bar
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { focusManager.clearFocus() })
                    }
            ) {
                // Search Bar
                SearchBar(
                    query = searchText,
                    onQueryChanged = { searchText = it },
                    focusRequester = focusRequester,
                    isFocused = isFocused,
                    onSearchFocusChanged = { isFocused = it } // Handle focus change
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Authors Section
                if (!authors.isNullOrEmpty()) {
                    Text(
                        text = "Authors",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        items(authors!!) { author ->
                            AuthorItem(author = AuthorResponse(author.author_name, 0, "", author.author_image), navController)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Books Section
                if (!books.isNullOrEmpty()) {
                    Text(
                        text = "Books",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(books!!) { book ->
                            BookCardHorizontal(
                                book = CustomerFavoriteResponse(
                                    book.book_name,
                                    book.author_name,
                                    book.book_image,
                                    book.average_rating,
                                    book.price
                                ),
                                navController = navController,
                                onFavoriteClick = {},
                                favoriteScreen = 2
                            )
                        }
                    }
                }

                if (searchText.isEmpty()) {
                    // Placeholder message when search text is empty
                    Text(
                        text = "What are you looking for?",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    )
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    focusRequester: FocusRequester,
    isFocused: Boolean,
    onSearchFocusChanged: (Boolean) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = { newQuery -> onQueryChanged(newQuery) },
        label = {
            Text(
                text = "Search",
                color = if (isFocused) mainColor else Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = if (isFocused) mainColor else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        },
        textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = mainColor,
            unfocusedBorderColor = Color.Gray,
            backgroundColor = if (isFocused) Color.White else Color.Transparent
        ),
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState -> onSearchFocusChanged(focusState.isFocused) }
    )
}
