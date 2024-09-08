package com.example.bookstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun OrderDetail(navController: NavHostController, orderID: String?) {
    var orderDetail =  Order("4dsf2ge",sampleCartItems())

    // Tính tổng số tiền cần thanh toán
    val totalPrice = orderDetail.listBookOrder.sumOf {it.price}
    val deliveryFee = 5

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Order Detail", navController = navController, isBack = true)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(bottom = 20.dp)
            ) {
                Text(
//                    text = "Hello ${account.name}!",
                    text = "Order ID: ${orderDetail.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
                // LazyColumn cho danh sách các sách
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(orderDetail.listBookOrder) { book ->
                        BookCardHorizontal(book = book, navController = navController, onFavoriteClick = {
                            orderDetail.listBookOrder = orderDetail.listBookOrder.toMutableList().apply {
                                remove(book)
                            }
                        })
                        if (orderDetail.listBookOrder.indexOf(book) < orderDetail.listBookOrder.size - 1) {
                            Divider()
                        }
                    }
                }

                // Thông tin địa chỉ giao hàng
                Text(
                    text = "Delivery Address",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 13.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().clickable { /* Điều hướng đến màn hình địa chỉ */ }
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("43 Bourke Street, Newbridge NSW 837 Raffles...", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }

                // Thông tin phí giao hàng và tổng tiền
                Divider(modifier = Modifier.padding(vertical = 20.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Delivery Fee", color = Color.Gray)
                    Text("$${deliveryFee}")
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Price", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("$${totalPrice + deliveryFee}", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "${orderDetail.listBookOrder.size} items",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text("Include taxes",color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun OrderDetailPreview() {
    OrderDetail(navController = rememberNavController(), "aa")
}