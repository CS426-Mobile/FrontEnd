package com.example.bookstore

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bookstore.components.AuthorsSection
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.components.FeaturedBooksSection
import com.example.bookstore.components.RecommendedBooksSection
import com.example.bookstore.network.SimpleBookResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.ui.theme.textColor
import com.example.bookstore.viewmodel.AuthorViewModel
import com.example.bookstore.viewmodel.BookViewModel
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedCategories by remember { mutableStateOf(listOf<String>()) }
    var offsetY by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    // Create AuthorViewModel instance
    val authorViewModel: AuthorViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    // State để theo dõi vị trí cuộn
    val listState = rememberLazyListState()

    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

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
            .padding(start = 16.dp, end = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        CustomTopAppBar(
            title = "Browse everything",
            isCart = true,
            navController = navController
        )

        SearchBar(
            query = searchQuery,
            onQueryChanged = { searchQuery = it },
        )

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
                    if (selectedCategories.isEmpty())
                        RecommendedBooksSection(navController = navController, bookViewModel = bookViewModel)
            }

            stickyHeader {
                Column(
                    modifier = Modifier.background(Color.White) // Ensure sticky header stays visible
                ) {
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
                }
            }

            item {
                if (selectedCategories.isEmpty())
                    AuthorsSection(navController = navController, authorViewModel = authorViewModel)
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
                FeaturedBooksSection(navController = navController, bookViewModel = bookViewModel)
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = query,
        onValueChange = { newQuery ->
            // Update the query with the new text
            onQueryChanged(newQuery)
        },
        label = {
            if (query.isEmpty()) {
                Text(
                    text = "Search",
                    color = when {
                        isFocused.value -> mainColor
                        else -> Color.Gray
                    }
                )
            }
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = when {
                    isFocused.value -> mainColor
                    else -> Color.Gray
                },
                modifier = Modifier.size(24.dp)
            )
        },
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = Color.Black
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = mainColor,
            unfocusedBorderColor = Color.Gray,
            backgroundColor = if (isFocused.value) Color.White else Color.Transparent
        ),
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .onFocusChanged { focusState -> isFocused.value = focusState.isFocused }
            .focusRequester(focusRequester)
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

data class Book(val title: String, val author: String)