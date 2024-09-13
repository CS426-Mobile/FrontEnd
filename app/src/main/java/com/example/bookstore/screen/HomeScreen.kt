package com.example.bookstore.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bookstore.R
import com.example.bookstore.components.AuthorsSection
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.components.FeaturedBooksSection
import com.example.bookstore.components.RecommendedBooksSection
import com.example.bookstore.model.CategoryResponse
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.AuthorViewModel
import com.example.bookstore.viewmodel.BookViewModel
import com.example.bookstore.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var offsetY by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    // State để theo dõi vị trí cuộn
    val listState = rememberLazyListState()

    // Create AuthorViewModel instance
    val authorViewModel: AuthorViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    // State cho nút Filter và Sort
    var selectedCategories by remember { mutableStateOf(listOf<String>()) }
    var isFilterActive by remember { mutableStateOf(false) }
    var sortType by remember { mutableStateOf("none") }
    var isTopRating by remember { mutableStateOf(false)}
    var fromPrice by remember { mutableStateOf(0) }
    var toPrice by remember { mutableStateOf(100) }
    var rating by remember { mutableStateOf("all") }
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    var shouldRefetchData by remember { mutableStateOf(false) }

    // Track previous states to detect changes
    var prevSelectedCategories by remember { mutableStateOf(listOf<String>()) }
    var prevIsTopRating by remember { mutableStateOf(false) }
    var prevSortType by remember { mutableStateOf("none") }
    var prevFromPrice by remember { mutableStateOf(0) }
    var prevToPrice by remember { mutableStateOf(100) }
    var prevRating by remember { mutableStateOf("all") }

    // Only trigger re-fetch when actual changes occur
    LaunchedEffect(selectedCategories, isTopRating, sortType, fromPrice, toPrice, rating) {
        if (selectedCategories.isEmpty()) {
            isTopRating = false
            sortType = "none"
            fromPrice = 0
            toPrice = 100
            rating = "all"
        }
        if (selectedCategories != prevSelectedCategories ||
            isTopRating != prevIsTopRating ||
            sortType != prevSortType ||
            fromPrice != prevFromPrice ||
            toPrice != prevToPrice ||
            rating != prevRating
        ) {
            shouldRefetchData = true
            prevSelectedCategories = selectedCategories
            prevIsTopRating = isTopRating
            prevSortType = sortType
            prevFromPrice = fromPrice
            prevToPrice = toPrice
            prevRating = rating
        }
    }

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
            onFilterActive = {isFilterActive = it},
            rating = {rating = it},
            fromPrice = {fromPrice = it},
            toPrice = {toPrice = it}
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
                                    emptyList() // Unselect if already selected
                                } else {
                                    listOf(category) // Select the new category
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
                                        "none" -> "asce"
                                        "asce" -> "desc"
                                        "desc" -> "none"
                                        else -> "none" // Fallback case, if needed
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
                FeaturedBooksSection(
                    navController = navController,
                    bookViewModel = bookViewModel,
                    selectedCategories = selectedCategories.getOrElse(0) { "" }, // Avoid out of bounds
                    isTopRating = isTopRating,
                    sortType = sortType,
                    fromPrice = fromPrice,
                    toPrice = toPrice,
                    rating = rating,
                    shouldRefetch = shouldRefetchData, // Pass re-fetch trigger
                    onFetchComplete = { shouldRefetchData = false } // Reset re-fetch flag after fetching
                )
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

@SuppressLint("RememberReturnType")
@Composable
fun CategoriesSection(
    selectedCategories: List<String>,
    onCategorySelected: (String) -> Unit
) {
    val categoryViewModel: CategoryViewModel = viewModel()
    var categories by remember { mutableStateOf<List<CategoryResponse>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch categories from ViewModel when the composable is first displayed
    LaunchedEffect(Unit) {
        categoryViewModel.getAllCategories { success, result ->
            if (success && result != null) {
                categories = result
            } else {
                errorMessage = "Failed to load categories"
            }
            isLoading = false
        }
    }

    // Section title
    Text(
        text = "Categories",
        color = mainColor,
        modifier = Modifier.padding(horizontal = 24.dp),
        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
    )

    val lighterMainColor = mainColor.copy(alpha = 0.2f) // Lighter main color for unselected items

    // Show loading or error message
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(color = Color.White), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Gray)
        }
    } else if (errorMessage != null) {
        Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.padding(16.dp))
    } else {
        // Add the "All" category manually to the list
        val categoriesWithAll = listOf(CategoryResponse("All")) + (categories ?: emptyList())

        // Categories list with spacing
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp), // Padding between boxes
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp) // Overall padding for the section
        ) {
            items(categoriesWithAll) { category ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            if (category.category_name == "All") {
                                // If "All" is selected, unselect other categories and select "All"
                                onCategorySelected("All") // Use empty string for "All"
                            } else {
                                // If another category is selected, unselect "All"
                                onCategorySelected(category.category_name)
                            }
                        }
                        .background(
                            if (selectedCategories.contains(category.category_name)) mainColor else lighterMainColor
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp) // Padding inside the box
                ) {
                    Text(
                        text = category.category_name,
                        color = if (selectedCategories.contains(category.category_name)) Color.White else Color.Black,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FilterPreview(){
    FilterBar(
        isFilterActive = true,
        isTopRating = true,
        onRatingClick = {},
        sortType = "asce",
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
    sortType: String = "none",
    onSortClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), // Padding 16.dp ở 2 đầu
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút Filter (Sát bên trái)
        Button(
            onClick = {
                onFilterClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isFilterActive) Color(0xFFFFF0E0) else Color(0xFFF0F0F0),
                contentColor = if (isFilterActive) Color(0xFFFF6600) else Color.Gray
            ),
            border = BorderStroke(1.dp, if (isFilterActive) mainColor else Color.Gray), // Viền tùy chỉnh
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.elevation(0.dp), // Loại bỏ shadow
            modifier = Modifier.wrapContentSize()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter), // Thay bằng icon của bạn
                contentDescription = "Filter",
                tint = if (isFilterActive) mainColor else Color.Gray,
                modifier = Modifier.size(20.dp) // Điều chỉnh kích thước icon
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Đẩy nút Top Ratings vào giữa

        // Nút Top Ratings (Ở giữa)
        Button(
            onClick = {
                onRatingClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isTopRating) Color(0xFFFFF0E0) else Color(0xFFF0F0F0),
                contentColor = if (isTopRating) mainColor else Color.Gray
            ),
            border = BorderStroke(1.dp, if (isTopRating) mainColor else Color.Gray), // Viền tùy chỉnh
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.elevation(0.dp), // Loại bỏ shadow
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = "Top Ratings",
                style = MaterialTheme.typography.body2,
                fontSize = 13.sp,
                color = if (isTopRating) mainColor else Color.Gray
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Đẩy nút Price sát bên phải

        // Nút Price (Sát bên phải)
        Button(
            onClick = onSortClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (sortType == "none") Color(0xFFF0F0F0) else Color(0xFFFFF0E0),
                contentColor = if (sortType == "none") Color.Gray else mainColor
            ),
            border = BorderStroke(1.dp, if (sortType == "none") Color.Gray else mainColor), // Viền tùy chỉnh
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.elevation(0.dp), // Loại bỏ shadow
            modifier = Modifier.wrapContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center // Canh giữa nội dung trong Row
            ) {
                Text("Price", style = MaterialTheme.typography.body2, fontSize = 13.sp, maxLines = 1) // Sửa lỗi text bị cắt bằng maxLines
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = when (sortType) {
                        "asce" -> Icons.Default.KeyboardArrowUp // Icon khi sắp xếp tăng dần
                        "desc" -> Icons.Default.KeyboardArrowDown // Icon khi sắp xếp giảm dần
                        "none" -> Icons.Default.Add // Icon mặc định khi không có sắp xếp
                        else -> Icons.Default.Add // Icon mặc định khi không có sắp xếp
                    },
                    contentDescription = "Sort Order",
                    tint = if (sortType == "none") Color.Gray else mainColor,
                    modifier = Modifier.size(20.dp) // Điều chỉnh kích thước icon
                )
            }
        }
    }
}