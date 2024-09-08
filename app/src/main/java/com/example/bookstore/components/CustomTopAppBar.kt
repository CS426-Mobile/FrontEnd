package com.example.bookstore.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bookstore.R
import com.example.bookstore.Screen
import com.example.bookstore.canGoBack
import com.example.bookstore.ui.theme.textColor

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
                        // onClick = { navController.navigateUp() }
                        onClick = {
                            if (navController.canGoBack) {
                                navController.popBackStack()
                            }
                        },
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