package com.example.bookstore

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Space
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.components.AuthorsSection
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.components.FeaturedBooksSection
import com.example.bookstore.components.RecommendedBooksSection
import com.example.bookstore.ui.theme.mainColor
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
    // State để theo dõi vị trí cuộn
    val listState = rememberLazyListState()

    // Create AuthorViewModel instance
    val authorViewModel: AuthorViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    // State để theo dõi vị trí cuộn

    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // State cho nút Filter và Sort
    var isFilterActive by remember { mutableStateOf(false) }
    var sortType by remember { mutableStateOf(SortType.NONE) }
    var isTopRating by remember { mutableStateOf(false)}

    var isSheetOpen by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CustomTopAppBar(
            title = "Browse everything",
            isCart = true,
            navController = navController
        )

        SearchBarHolder(navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        FilterScreen(
            isSheetOpen = isSheetOpen,
            onSheetOpenChange = { isSheetOpen = it },
            onFilterActive = {isFilterActive = it}
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
                    if (selectedCategories.isEmpty()) {
                        RecommendedBooksSection(navController = navController, bookViewModel = bookViewModel)
                        AuthorsSection(navController = navController, authorViewModel = authorViewModel)
                    }
            }

            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White) // Đặt nền vững chắc
                        .zIndex(1f)
                        .pointerInput(Unit) {}
                ) {
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
                                isTopRating = isTopRating,
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
                                },
                                onRatingClick = {
                                    isTopRating = !isTopRating
                                }
                            )
                        }
                    }
                }
            }

            // Featured Books Section
            item {
                FeaturedBooksSection(navController = navController, bookViewModel = bookViewModel)
            }
        }
    }
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
fun SearchBarHolder(navController: NavHostController) {
    Spacer(modifier = Modifier.height(4.dp))
    // Outer Box to handle clicks and give the illusion of a search bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)  // Set a height similar to the standard OutlinedTextField
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFFF0F0F0)) // Light gray background like a search bar
            .clickable {
                // Navigate to the search screen when clicked
                navController.navigate(Screen.Search.route)
            }
            .padding(horizontal = 16.dp),  // Padding inside the search bar
        contentAlignment = Alignment.CenterStart
    ) {
        // Row inside the Box to align the search icon and text
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Search icon
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )

            // Spacer to create space between the icon and text
            Spacer(modifier = Modifier.width(8.dp))

            // Placeholder text, similar to OutlinedTextField's label
            Text(
                text = "Search",
                color = Color.Gray,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun CategoriesSection(
    selectedCategories: List<String>,
    onCategorySelected: (String) -> Unit
) {
    // Section title
    Text(
        text = "Categories",
        color = mainColor,
        modifier = Modifier.padding(horizontal = 24.dp),
        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
    )

    val lighterMainColor = mainColor.copy(alpha = 0.2f) // Lighter main color for unselected items

    // Categories list with spacing
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp), // Padding between boxes
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp) // Overall padding for the section
    ) {
        items(listOf("All", "Romance", "Fiction", "Education", "Manga")) { category ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onCategorySelected(category) }
                    .background(if (selectedCategories.contains(category)) mainColor else lighterMainColor)
                    .padding(horizontal = 12.dp, vertical = 8.dp) // Padding inside the box
            ) {
                Text(
                    text = category,
                    color = if (selectedCategories.contains(category)) Color.White else Color.Black,
                )
            }
        }
    }

    // Add white space below the categories section
}

@Preview(showBackground = true)
@Composable
fun FilterPreview(){
    FilterBar(
        isFilterActive = true,
        isTopRating = true,
        onRatingClick = {},
        sortType = SortType.ASCENDING,
        onSortClick = {},
        onFilterClick = {}
    )
}

@Composable
fun FilterBar(
    isFilterActive: Boolean = false,
    onFilterClick: () -> Unit,
    isTopRating: Boolean = false,
    onRatingClick: () -> Unit,
    sortType: SortType = SortType.NONE,
    onSortClick: () -> Unit
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
            modifier = Modifier.wrapContentSize().weight(1f) // Đảm bảo nút vừa với nội dung
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter), // Thay bằng icon của bạn
                contentDescription = "Filter",
                tint = if (isFilterActive) mainColor else Color.Gray,
                modifier = Modifier.size(16.dp) // Điều chỉnh kích thước icon
            )
        }

        // Nút Top Ratings
        Button(
            onClick = {
                onRatingClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isTopRating) Color(0xFFFFF0E0) else Color(0xFFF0F0F0),
                contentColor = if (isTopRating) mainColor else Color.Gray
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.wrapContentSize().weight(2f)
        ) {
            Text("Top Ratings", fontSize = 12.sp,
                color = if (isTopRating) mainColor else Color.Gray)
        }

        // Nút Price
        Button(
            onClick = onSortClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (sortType == SortType.NONE) Color(0xFFF0F0F0) else Color(0xFFFFF0E0),
                contentColor = if (sortType == SortType.NONE) Color.Gray else mainColor
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.wrapContentSize().weight(2f)
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
                    tint = if (sortType == SortType.NONE) Color.Gray else mainColor,
                    modifier = Modifier.size(16.dp) // Điều chỉnh kích thước icon
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}


// Định nghĩa các loại sắp xếp
enum class SortType {
    NONE,
    ASCENDING,
    DESCENDING
}

data class Book(val title: String, val author: String)