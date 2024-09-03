package com.example.bookstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.ui.theme.textColor
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import com.example.bookstore.ui.theme.mainColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedCategories by remember { mutableStateOf(listOf<String>()) }
    var offsetY by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    val books = listOf(
        Book("1917", "Richard Moors"),
        Book("Perfume", "Amber Julia"),
        Book("Travel to Japan", "John Underwood")
        // Thêm sách khác nếu có
    )

    // State để theo dõi vị trí cuộn
    val listState = rememberLazyListState()

    // State cho nút Filter và Sort
    var isFilterActive by remember { mutableStateOf(false) }
    var sortType by remember { mutableStateOf(SortType.NONE) }

    // Animation for Recommended Books height
    val recommendedBooksHeight by animateDpAsState(
        targetValue = if (offsetY > -100) 200.dp else 0.dp
    )

    var isSheetOpen by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp)
    ) {
        CustomTopAppBar(
            title = "Browse everything",
            isCart = true,
            navController = navController
        )

        SearchBar()

        FilterScreen(
            isSheetOpen = isSheetOpen,
            onSheetOpenChange = { isSheetOpen = it }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(
                    rememberNestedScrollConnection(
                        onScroll = { delta ->
                            scope.launch {
                                offsetY = (offsetY + delta).coerceIn(-200f, 0f)
                            }
                        }
                    )
                ),
            state = listState // Áp dụng state để theo dõi vị trí cuộn
        ) {
            // Recommended Books Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (selectedCategories.isEmpty()) recommendedBooksHeight else 0.dp)
                        .background(Color.White)
                        .zIndex(1f)
                ) {
                    if (selectedCategories.isEmpty())
                        RecommendedBooksSection()
                }
            }

            stickyHeader {
                Column {
                    CategoriesSection(
                        selectedCategories = selectedCategories,
                        onCategorySelected = { category ->
                            selectedCategories = if (selectedCategories.contains(category)) {
                                selectedCategories - category
                            } else {
                                selectedCategories + category
                            }
                        }
                    )

                    // Thay thế FilterSection bằng FilterBar
                    if (selectedCategories.isNotEmpty()) {
                        FilterBar(
                            isFilterActive = isFilterActive,
                            navController = navController,
                            sortType = sortType,
                            onSortClick = {
                                sortType = when (sortType) {
                                    SortType.NONE -> SortType.ASCENDING
                                    SortType.ASCENDING -> SortType.DESCENDING
                                    SortType.DESCENDING -> SortType.NONE
                                }
                            },
                            onFilterClick = {
                                isSheetOpen = true
                            }
                        )
                    }
                    else
                        AuthorsSection(navController = navController)
                }
            }



            // Featured Books Section
            item {
//                FilterBar(
//                    isFilterActive = isFilterActive,
//                    navController = navController,
//                    sortType = sortType,
//                    onSortClick = {
//                        sortType = when (sortType) {
//                            SortType.NONE -> SortType.ASCENDING
//                            SortType.ASCENDING -> SortType.DESCENDING
//                            SortType.DESCENDING -> SortType.NONE
//                        }
//                    }
//                )
                FeaturedBooksSection(books)
            }
        }
    }
}



@Composable
fun CustomTopAppBar(title: String, isBack: Boolean = false, isCart: Boolean = false, navController: NavHostController) {
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
                if (isBack) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back), // Thay bằng icon quay lại của bạn
                            contentDescription = "Back"
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Nút giỏ hàng bên phải
                if (isCart) {
                    IconButton(
                        onClick = { navController.navigate(Screen.Cart.route) },
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
}

@Composable
fun SearchBar() {
    TextField(
        value = "",
        onValueChange = { /* Handle text change */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
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
fun rememberNestedScrollConnection(onScroll: (Float) -> Unit): NestedScrollConnection {
    return remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                onScroll(available.y)
                return Offset.Zero
            }
        }
    }
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
    selectedCategories: List<String>,
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
                    .background(if (selectedCategories.contains(category)) mainColor else Color.Transparent)
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(24.dp)),
                color = if (selectedCategories.contains(category)) Color.White else Color.Black
            )
        }
    }
}

@Composable
fun FilterBar(
    isFilterActive: Boolean = false,
    navController: NavHostController,
    sortType: SortType = SortType.NONE,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Thêm khoảng cách giữa các nút
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Filter
        Button(
            onClick = {
                onFilterClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isFilterActive) Color(0xFFFFF0E0) else Color(0xFFF0F0F0),
                contentColor = if (isFilterActive) Color(0xFFFF6600) else Color.Gray
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.wrapContentSize() // Đảm bảo nút vừa với nội dung
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter), // Thay bằng icon của bạn
                contentDescription = "Filter",
                tint = if (isFilterActive) Color(0xFFFF6600) else Color.Gray,
                modifier = Modifier.size(16.dp) // Điều chỉnh kích thước icon
            )
        }

        // Nút Top Sales
        Button(
            onClick = { /* Xử lý khi nút Top Sales được bấm */ },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF0F0F0),
                contentColor = Color.Gray
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.wrapContentSize()
        ) {
            Text("Top Sales", fontSize = 12.sp) // Điều chỉnh font size cho dễ nhìn hơn
        }

        // Nút Top Ratings
        Button(
            onClick = { /* Xử lý khi nút Top Ratings được bấm */ },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF0F0F0),
                contentColor = Color.Gray
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.wrapContentSize()
        ) {
            Text("Top Ratings", fontSize = 12.sp)
        }

        // Nút Price
        Button(
            onClick = onSortClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF0F0F0),
                contentColor = if (sortType == SortType.NONE) Color.Gray else Color(0xFFFF6600)
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.wrapContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center // Canh giữa nội dung trong Row
            ) {
                Text("Price", fontSize = 12.sp, maxLines = 1) // Sửa lỗi text bị cắt bằng maxLines
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = when (sortType) {
                        SortType.ASCENDING -> Icons.Default.KeyboardArrowUp // Icon khi sắp xếp tăng dần
                        SortType.DESCENDING -> Icons.Default.KeyboardArrowDown // Icon khi sắp xếp giảm dần
                        SortType.NONE -> Icons.Default.Add // Icon mặc định khi không có sắp xếp
                    },
                    contentDescription = "Sort Order",
                    tint = if (sortType == SortType.NONE) Color.Gray else Color(0xFFFF6600),
                    modifier = Modifier.size(16.dp) // Điều chỉnh kích thước icon
                )
            }
        }
    }
}


// Định nghĩa các loại sắp xếp
enum class SortType {
    NONE,
    ASCENDING,
    DESCENDING
}

@Composable
fun AuthorsSection(navController: NavHostController) {
    Row(){
        Text(text = "Popular Authors", modifier = Modifier
            .padding(16.dp)
            .weight(3.4f), style = MaterialTheme.typography.h6)
        Button(
            onClick = { navController.navigate("author") },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            )
        ){
            Text(text = "See all >", color = Color.Gray, fontSize = 11.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
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
fun FeaturedBooksSection(books: List<Book>) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Featured Books",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Bọc LazyColumn với Box và giới hạn chiều cao
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn {
                    items(books.chunked(2)) { bookRow ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (book in bookRow) {
                                BookCard(book = book)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookCard(book: Book) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Hình ảnh sách giả lập
            Box(
                modifier = Modifier
                    .height(188.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            ) {
                // Nội dung khác như hình ảnh thật, nếu có
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = book.title, style = MaterialTheme.typography.subtitle1)
            Text(text = book.author, style = MaterialTheme.typography.caption)
        }
    }
}

data class Book(val title: String, val author: String)


@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}