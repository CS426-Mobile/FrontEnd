package com.example.bookstore

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.components.AuthorItem
import com.example.bookstore.components.BookCardHorizontal
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.network.AuthorResponse
import com.example.bookstore.ui.theme.mainColor

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    val allAuthors = remember { getFollowedAuthors() }  // Danh sách tác giả ngẫu nhiên
    val allBooks = remember { sampleFavoriteBooks() }  // Danh sách sách ngẫu nhiên

    val filteredAuthors = remember(searchText) {
        if (searchText.isEmpty()) allAuthors else allAuthors.filter { it.name.contains(searchText, true) }
    }

    var filteredBooks = remember(searchText) {
        if (searchText.isEmpty()) allBooks else allBooks.filter { it.title.contains(searchText, true) }
    }
    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Search", navController = navController, isCart = true, isBack = true)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ){
                SearchBar(
                    searchText = searchText,
                    onTextChange = { searchText = it },
                    onClearClick = { searchText = "" }
                )
                Text(
                    text = "Authors",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

//                LazyRow{
//                    items (filteredAuthors) {author ->
//                        AuthorItem(author = author, onClick = { /* Handle author click */ })
//                    }
//                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Books",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredBooks) { book ->
                        BookCardHorizontal(book = book, onFavoriteClick = {
                            filteredBooks = filteredBooks.toMutableList().apply {
                                remove(book)
                            }
                        })
                        if (filteredBooks.indexOf(book) < filteredBooks.size - 1) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: String,
    onTextChange: (String) -> Unit,
    onClearClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchText,
            onValueChange = onTextChange,
            placeholder = { Text("Search...") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = onClearClick) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { /* Xử lý sự kiện tìm kiếm */ },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.padding(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = mainColor,
                contentColor = Color.White
            )
        ) {
            Text("Find")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(navController = rememberNavController())
}