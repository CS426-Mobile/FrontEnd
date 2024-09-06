package com.example.bookstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.ui.theme.mainColor

@Composable
fun CartScreen(navController: NavHostController) {
    // Dữ liệu mẫu cho các sách trong giỏ hàng
    var cartItems = sampleCartItems()

    // Tính tổng số tiền cần thanh toán
    val totalPrice = cartItems.sumOf { it.price * it.quantity }
    val deliveryFee = 5

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Shopping Cart", navController = navController, isBack = true)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(bottom = 20.dp)
            ) {
                // LazyColumn cho danh sách các sách
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(item = item, onRemoveItem = { removedItem ->
                            cartItems = cartItems.filter { it.id != removedItem.id }
                        }, onQuantityChange = { updatedItem, newQuantity ->
                            cartItems = cartItems.map {
                                if (it.id == updatedItem.id) it.copy(quantity = newQuantity) else it
                            }
                        })
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
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Arrow")
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
                        text = "${cartItems.sumOf {it.quantity}} items",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text("Include taxes",color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
                }


                // Nút mua hàng
                Button(
                    onClick = { /* Xử lý nút Buy */ },
                    colors = ButtonDefaults.buttonColors(containerColor = mainColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(56.dp)
                ) {
                    Text("Buy", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

@Composable
fun CartItemCard(
    item: CartItem,
    onRemoveItem: (CartItem) -> Unit,
    onQuantityChange: (CartItem, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_shopping), // Thay bằng hình bìa sách thực tế
            contentDescription = "Book Cover",
            modifier = Modifier.size(54.dp, 81.dp)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            Text(item.title, fontWeight = FontWeight.Bold)
            Text("$${item.price}", color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                if (item.quantity > 1) onQuantityChange(item, item.quantity - 1)
                else onRemoveItem(item)
            }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease Quantity")
            }
            Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { onQuantityChange(item, item.quantity + 1) }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase Quantity")
            }
        }
    }
    Divider()
}

data class CartItem(
    val id: Int,
    val title: String,
    val price: Int,
    var quantity: Int
)

fun sampleCartItems(): List<CartItem> {
    return listOf(
        CartItem(1, "Battle of 1917", 12, 1),
        CartItem(2, "A Tale of Ben Hur", 20, 1),
        CartItem(3, "Valles Marineris", 17, 1)
    )
}



@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    CartScreen(navController = rememberNavController())
}