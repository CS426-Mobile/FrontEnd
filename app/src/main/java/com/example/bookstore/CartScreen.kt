//package com.example.bookstore
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.example.bookstore.components.BookCardHorizontal
//import com.example.bookstore.components.BookDetail
//import com.example.bookstore.components.CustomTopAppBar
//import com.example.bookstore.ui.theme.mainColor
//
//@Composable
//fun CartScreen(navController: NavHostController) {
//    // Dữ liệu mẫu cho các sách trong giỏ hàng
//    var cartItems = sampleCartItems()
//
//    // Tính tổng số tiền cần thanh toán
//    val totalPrice = cartItems.sumOf { it.price }
//    val deliveryFee = 5
//
//    Scaffold(
//        topBar = {
//            CustomTopAppBar(title = "Shopping Cart", navController = navController, isBack = true)
//        },
//        content = { paddingValues ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .padding(horizontal = 16.dp, vertical = 8.dp)
//                    .padding(bottom = 20.dp)
//            ) {
//                // LazyColumn cho danh sách các sách
//                LazyColumn(
//                    modifier = Modifier.weight(1f),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    items(cartItems) { book ->
//                        BookCardHorizontal(
//                            book = book, navController = navController,
//                            onFavoriteClick = {
//                            cartItems = cartItems.toMutableList().apply {
//                                remove(book)
//                            }
//                        })
//                        if (cartItems.indexOf(book) < cartItems.size - 1) {
//                            HorizontalDivider()
//                        }
//                    }
//                }
//
//                // Thông tin địa chỉ giao hàng
//                Text(
//                    text = "Delivery Address",
//                    modifier = Modifier.padding(vertical = 8.dp),
//                    fontSize = 13.sp
//                )
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.fillMaxWidth()
//                        .clickable { /* Điều hướng đến màn hình địa chỉ */ }
//                ) {
//                    Column(modifier = Modifier.weight(1f)) {
//                        Text(
//                            "43 Bourke Street, Newbridge NSW 837 Raffles...",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 15.sp
//                        )
//                    }
//                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Arrow")
//                }
//
//                // Thông tin phí giao hàng và tổng tiền
//                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))
//                Row(
//                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text("Delivery Fee", color = Color.Gray)
//                    Text("$${deliveryFee}")
//                }
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text("Total Price", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//                    Text(
//                        "$${totalPrice + deliveryFee}",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 17.sp
//                    )
//                }
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        text = "${cartItems.size} items",
//                        color = Color.Gray,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    )
//                    Text(
//                        "Include taxes",
//                        color = Color.Gray,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    )
//                }
//
//
//                // Nút mua hàng
//                Button(
//                    onClick = { /* Xử lý nút Buy */ },
//                    colors = ButtonDefaults.buttonColors(containerColor = mainColor),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.dp)
//                        .height(56.dp)
//                ) {
//                    Text("Buy", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                }
//            }
//        }
//    )
//}
//
//fun sampleCartItems(): List<BookDetail> {
//    return listOf(
//        BookDetail("The Secret About Us", "Elizabeth McKean", 4.2f, true),
//        BookDetail("The Happiness", "Kate Kirkwood", 4.7f, true),
//        BookDetail("Peace in His Life", "Armando Newman", 3.9f, true)
//    )
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun CartScreenPreview() {
//    CartScreen(navController = rememberNavController())
//}