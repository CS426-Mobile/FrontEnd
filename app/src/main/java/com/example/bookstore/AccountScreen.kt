package com.example.bookstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.components.Author
import com.example.bookstore.components.AuthorHorizontalItem
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.ui.theme.mainColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountScreen(navController: NavHostController) {
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
                    .padding(16.dp)
            ) {
                // Phần lời chào và tên người dùng
                Text(
                    text = "Hello Bao!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn (
                    modifier = Modifier.background(Color.White)
                ) {

                // Phần địa chỉ
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
                                .clickable { /* Mở màn hình địa chỉ */ }
                        ) {
                            Column {
                                Text(
                                    text = "Office",
                                    color = Color.Gray
                                )
                                Text(
                                    //                            text = account.address,
                                    text = "43 Bourke Street, Newbridge NSW 837 R...ndfjandkfjanskdfnkdasnfka",
                                    color = Color.Black
                                )
                            }
                        }

                        HorizontalDivider()

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    // Phần tài khoản
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
                                    tint = mainColor
                                )
                            },
                            text = { Text("Change Password") },
                            trailing = {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Change Password"
                                )
                            },
                            modifier = Modifier.clickable { /* Mở màn hình thay đổi mật khẩu */ }
                        )

                        ListItem(
                            icon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Sign Out",
                                    tint = mainColor
                                )
                            },
                            text = { Text("Sign Out") },
                            trailing = {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Sign Out"
                                )
                            },
                            modifier = Modifier.clickable {
                                // Đăng xuất và quay lại LoginActivity
                                navController.popBackStack(route = "login", inclusive = true)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Following",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                    }

                    // Phần tác giả đã theo dõi

//                    var listAuthor = getFollowedAuthors()
//                    items(listAuthor) { author ->
//                            AuthorHorizontalItem(
//                                author = author,
//                                navController = navController,
//                                onButtonFollow = { /*author.isFollowing = !author.isFollowing*/ }
//                            )
//                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    AccountScreen(navController = rememberNavController())
}

// Hàm lấy danh sách các tác giả đã theo dõi (Dữ liệu mẫu)
fun getFollowedAuthors(): List<Author> {
    return listOf(
        Author(
            name = "Averie Stecanella",
            followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
            books = 873,
            isFollowing = false,
            categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
            about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
            booksList = listOf(
                Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
                Book("Believe in Eternal Dark", "Laura Jones"),
                Book("The Secret About Us", "Elizabeth McKean"),
                Book("Love in the Time of War", "Michael Foster"),
                Book("Shadows of the Past", "Alice Munro")
            )
        ),
        Author(
            name = "Averie Stecanella",
            followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
            books = 873,
            isFollowing = false,
            categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
            about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
            booksList = listOf(
                Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
                Book("Believe in Eternal Dark", "Laura Jones"),
                Book("The Secret About Us", "Elizabeth McKean"),
                Book("Love in the Time of War", "Michael Foster"),
                Book("Shadows of the Past", "Alice Munro")
            )
        ),
        Author(
            name = "Averie Stecanella",
            followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
            books = 873,
            isFollowing = false,
            categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
            about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
            booksList = listOf(
                Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
                Book("Believe in Eternal Dark", "Laura Jones"),
                Book("The Secret About Us", "Elizabeth McKean"),
                Book("Love in the Time of War", "Michael Foster"),
                Book("Shadows of the Past", "Alice Munro")
            )
        ),
        Author(
            name = "Averie Stecanella",
            followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
            books = 873,
            isFollowing = false,
            categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
            about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
            booksList = listOf(
                Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
                Book("Believe in Eternal Dark", "Laura Jones"),
                Book("The Secret About Us", "Elizabeth McKean"),
                Book("Love in the Time of War", "Michael Foster"),
                Book("Shadows of the Past", "Alice Munro")
            )
        ),
        Author(
            name = "Averie Stecanella",
            followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
            books = 873,
            isFollowing = false,
            categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
            about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
            booksList = listOf(
                Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
                Book("Believe in Eternal Dark", "Laura Jones"),
                Book("The Secret About Us", "Elizabeth McKean"),
                Book("Love in the Time of War", "Michael Foster"),
                Book("Shadows of the Past", "Alice Munro")
            )
        ),
        Author(
            name = "Averie Stecanella",
            followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
            books = 873,
            isFollowing = false,
            categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
            about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
            booksList = listOf(
                Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
                Book("Believe in Eternal Dark", "Laura Jones"),
                Book("The Secret About Us", "Elizabeth McKean"),
                Book("Love in the Time of War", "Michael Foster"),
                Book("Shadows of the Past", "Alice Munro")
            )
        ),
        Author(
            name = "Averie Stecanella",
            followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
            books = 873,
            isFollowing = false,
            categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
            about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
            booksList = listOf(
                Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
                Book("Believe in Eternal Dark", "Laura Jones"),
                Book("The Secret About Us", "Elizabeth McKean"),
                Book("Love in the Time of War", "Michael Foster"),
                Book("Shadows of the Past", "Alice Munro")
            )
        ),
        Author(
            name = "Averie Stecanella",
            followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
            books = 873,
            isFollowing = false,
            categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
            about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
            booksList = listOf(
                Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
                Book("Believe in Eternal Dark", "Laura Jones"),
                Book("The Secret About Us", "Elizabeth McKean"),
                Book("Love in the Time of War", "Michael Foster"),
                Book("Shadows of the Past", "Alice Munro")
            )
        ),
        Author(
            name = "Averie Stecanella",
            followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
            books = 873,
            isFollowing = false,
            categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
            about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
            booksList = listOf(
                Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
                Book("Believe in Eternal Dark", "Laura Jones"),
                Book("The Secret About Us", "Elizabeth McKean"),
                Book("Love in the Time of War", "Michael Foster"),
                Book("Shadows of the Past", "Alice Munro")
            )
        ),
        Author(
            name = "Averie Stecanella",
            followers = "8.2M", // Có thể format kiểu String để phù hợp hơn với hiển thị
            books = 873,
            isFollowing = false,
            categories = listOf("Drama", "Mysteries & Thrillers", "Biographies", "Romance", "Fantasy", "Horror"),
            about = "Averie is an American writer, best known for her romance novels. She is the bestselling author alive and the fourth bestselling fiction author of all time, with over 800 million copies sold. She has written 179 books, in her own time and style, which have gained a massive readership and critical acclaim.",
            booksList = listOf(
                Book("Jan Rombouts Een Renaisan", "Mercatorfonds"),
                Book("Believe in Eternal Dark", "Laura Jones"),
                Book("The Secret About Us", "Elizabeth McKean"),
                Book("Love in the Time of War", "Michael Foster"),
                Book("Shadows of the Past", "Alice Munro")
            )
        )
    )
}