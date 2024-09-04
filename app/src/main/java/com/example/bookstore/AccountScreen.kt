package com.example.bookstore

import androidx.compose.foundation.Image
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Phần lời chào và tên người dùng
                Text(
//                    text = "Hello ${account.name}!",
                    text = "Hello Bao!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phần địa chỉ
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
                            text = "43 Bourke Street, Newbridge NSW 837 R...",
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Edit Address",
                        tint = Color.Gray
                    )
                }

                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                // Phần tài khoản
                Text(
                    text = "Account",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.Black
                )

                ListItem(
                    icon = {
                        Icon(Icons.Default.Lock, contentDescription = "Change Password", tint = mainColor)
                    },
                    text = { Text("Change Password") },
                    trailing = { Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Change Password") },
                    modifier = Modifier.clickable { /* Mở màn hình thay đổi mật khẩu */ }
                )

                ListItem(
                    icon = {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out", tint = mainColor)
                    },
                    text = { Text("Sign Out") },
                    trailing = { Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Sign Out") },
                    modifier = Modifier.clickable {
                        // Đăng xuất và quay lại LoginActivity
                        navController.popBackStack(route = "login", inclusive = true)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phần tác giả đã theo dõi
                Text(
                    text = "Following",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.Black
                )

                LazyColumn {
                    var listAuthor = getFollowedAuthors()
                    items(listAuthor) { author ->
                        AuthorItem(author = author, onFollowToggle = { /* Toggle Follow/Unfollow */ })
                    }
                }
            }
        }
    )
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
        )
    )
}

// Composable hiển thị thông tin một tác giả
@Composable
fun AuthorItem(author: Author, onFollowToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onFollowToggle() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hình đại diện tác giả
        Image(
            painter = painterResource(id = R.drawable.ic_account), // Thay thế bằng hình đại diện của bạn
            contentDescription = "Avatar",
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Thông tin tác giả
        Column(modifier = Modifier.weight(1f)) {
            Text(text = author.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(text = "${author.followers} Followers", color = Color.Gray, fontSize = 13.sp)
            Text(text = if (author.categories.size > 2) {
                "${author.categories[0]}, ${author.categories[1]}, ${author.categories.size - 2} more"
                } else {
                    buildString {
                        for (i in author.categories.indices) {
                            append(author.categories[i])
                            if (i < author.categories.size - 1) {
                                append(", ")
                            }
                        }
                    }
                },
            color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    AccountScreen(navController = rememberNavController())
}