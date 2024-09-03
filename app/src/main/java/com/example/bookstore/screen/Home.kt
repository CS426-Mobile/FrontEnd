package com.example.bookstore.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstore.R
import com.example.bookstore.model.AuthorEntity
import com.example.bookstore.model.BookEntity
import com.example.bookstore.model.CategoryEntity
import com.example.bookstore.viewmodel.HomeViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val recommendedBooks by viewModel.recommendedBooks.observeAsState(emptyList())
    val bestSellerBooks by viewModel.bestSellerBooks.observeAsState(emptyList())
    val popularAuthors by viewModel.popularAuthors.observeAsState(emptyList())
    val featuredBooks by viewModel.featuredBooks.observeAsState(emptyList())
    val categories by viewModel.categories.observeAsState(emptyList())

    Scaffold(
        topBar = { TopBar() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SearchBar()
            BookSection("Recommended", recommendedBooks)
            BookSection("Best Seller", bestSellerBooks)
            CategorySection(categories)
            AuthorSection(popularAuthors)
            BookSection("Featured Books", featuredBooks)
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = "Browse everything") },
        actions = {
            IconButton(onClick = { /* Navigate to Cart Screen */ }) {
                Icon(painter = painterResource(id = R.drawable.ic_shopping), contentDescription = "Cart")
            }
        }
    )
}

@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }

    BasicTextField(
        value = searchText,
        onValueChange = { searchText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textStyle = TextStyle(fontSize = 18.sp),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(MaterialTheme.colors.surface, shape = MaterialTheme.shapes.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = "Search")
                Box(modifier = Modifier.weight(1f)) {
                    innerTextField()
                }
                if (searchText.isEmpty()) {
                    Text(text = "Search", style = TextStyle(fontSize = 18.sp))
                }
            }
        }
    )
}

@Composable
fun BookSection(title: String, books: List<BookEntity>) {
    Column {
        Text(text = title, style = MaterialTheme.typography.h6, modifier = Modifier.padding(vertical = 8.dp))
        LazyRow {
            items(books.size) { index ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = books[index].localImageRes),
                        contentDescription = books[index].name,
                        modifier = Modifier
                            .size(125.dp, 188.dp)
                            .clickable { /* Navigate to Book Detail */ },
                        contentScale = ContentScale.Crop
                    )
                    Text(text = books[index].name, style = TextStyle(fontSize = 14.sp))
                    Text(text = books[index].authorName, style = TextStyle(fontSize = 12.sp))
                }
            }
        }
    }
}

@Composable
fun CategorySection(categories: List<CategoryEntity>) {
    Column {
        Text(text = "Categories", style = MaterialTheme.typography.h6, modifier = Modifier.padding(vertical = 8.dp))
        LazyRow {
            items(categories.size) { index ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = categories[index].localImageRes),
                        contentDescription = categories[index].name,
                        modifier = Modifier
                            .size(64.dp)
                            .clickable { /* Navigate to Category Detail */ },
                        contentScale = ContentScale.Crop
                    )
                    Text(text = categories[index].name, style = TextStyle(fontSize = 14.sp))
                }
            }
        }
    }
}

@Composable
fun AuthorSection(authors: List<AuthorEntity>) {
    Column {
        Text(text = "Popular Authors", style = MaterialTheme.typography.h6, modifier = Modifier.padding(vertical = 8.dp))
        LazyRow {
            items(authors.size) { index ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = authors[index].localImageRes),
                        contentDescription = authors[index].name,
                        modifier = Modifier
                            .size(64.dp)
                            .clickable { /* Navigate to Author Detail */ },
                        contentScale = ContentScale.Crop
                    )
                    Text(text = authors[index].name, style = TextStyle(fontSize = 14.sp))
                }
            }
        }
    }
}
