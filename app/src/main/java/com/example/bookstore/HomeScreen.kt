package com.example.bookstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.ui.theme.textColor

@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("All") }
    var isAuthorRowVisible by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            CustomTopAppBar(
                title = "Browse everything",
                onBackClick = { /* Handle back action */ },
                onCartClick = { /* Handle cart action */ }
            )
        }

        item {
            SearchBar()
        }

        if (isAuthorRowVisible) {
            item {
                RecommendedBooksSection()
            }
        }

        item {
            CategoriesSection(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    isAuthorRowVisible = false
                }
            )
        }

        if (isAuthorRowVisible) {
            item {
                AuthorsSection(navController)
            }
        }

        item {
            FeaturedBooksSection(selectedCategory)
        }
    }
}

@Composable
fun CustomTopAppBar(title: String, onBackClick: () -> Unit, onCartClick: () -> Unit) {
    TopAppBar(
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Tiêu đề ở giữa
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontSize = 24.sp,
                color = textColor
            )

            // Hàng chứa các icon ở hai bên
            Row(modifier = Modifier.fillMaxWidth()) {
                // Nút quay lại bên trái
                IconButton(
                    onClick = { onBackClick() },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back), // Thay bằng icon quay lại của bạn
                        contentDescription = "Back"
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Nút giỏ hàng bên phải
                IconButton(
                    onClick = { onCartClick() },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shopping), // Thay bằng icon giỏ hàng của bạn
                        contentDescription = "Cart"
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    TextField(
        value = "",
        onValueChange = { /* Handle text change */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        placeholder = { Text(text = "Search") },
//        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search"
            )
        },
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xFFF6F6F7), // Màu nền của ô tìm kiếm
            focusedIndicatorColor = Color.Transparent, // Xóa viền khi ô được chọn
            unfocusedIndicatorColor = Color.Transparent // Xóa viền khi ô không được chọn
        )
    )
}

@Composable
fun RecommendedBooksSection() {
    Text(text = "Recommended", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.h6)
    LazyRow {
        items(3) {
            // Hiển thị các sách được đề xuất
            Box(
                modifier = Modifier
                    .size(125.dp, 188.dp)
                    .background(Color.LightGray)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun CategoriesSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Text(text = "Categories", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.h6)
    LazyRow {
        items(listOf("All", "Romance", "Fiction", "Education", "Manga")) { category ->
            Text(
                text = category,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onCategorySelected(category) }
                    .background(if (category == selectedCategory) Color(0xFFFF6F00) else Color.Transparent)
                    .padding(16.dp),
                color = if (category == selectedCategory) Color.White else Color.Black
            )
        }
    }
}

@Composable
fun AuthorsSection(navController: NavHostController) {
    Text(text = "Popular Authors", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.h6)
    LazyRow {
        items(listOf("Gerard Fabiano", "Amber Julia", "Fernando Agaro")) { author ->
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Gray)
                    .padding(8.dp)
                    .clickable { navController.navigate("author/${author.replace(" ", "_")}") }
            ) {
                Text(text = author, color = Color.White)
            }
        }
    }
}

@Composable
fun FeaturedBooksSection(selectedCategory: String) {
    Text(text = "Featured Books", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.h6)
    LazyRow {
        items(4) {
            // Hiển thị các sách đặc trưng tùy theo category
            Box(
                modifier = Modifier
                    .size(125.dp, 188.dp)
                    .background(Color.LightGray)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}