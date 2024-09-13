package com.example.bookstore.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.R
import com.example.bookstore.ui.theme.mainColor

@Composable
fun PurchaseSuccessScreen(navController: NavHostController) {
    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Hộp chứa tiêu đề và biểu tượng
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Tiêu đề
                        Text(
                            text = "Success!",
//                            style = MaterialTheme.typography.h4,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 48.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Biểu tượng dấu tích
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check_success), // Thay thế bằng nguồn biểu tượng của bạn
                            contentDescription = "Success Icon",

                            modifier = Modifier.size(128.dp),
                            tint = mainColor // Màu tím đậm
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Văn bản cảm ơn
                        Text(
                            text = "Thank you for shopping",
//                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Văn bản mô tả
                        Text(
                            text = "Your items have been placed and is on its way to being processed",
//                            style = MaterialTheme.typography.body2,
                            textAlign = TextAlign.Center,
                            fontSize = 17.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Nút "Track Order"
                Button(
                    onClick = {
                        navController.navigate(Screen.ListOrder.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(text = "Track Order", color = Color.White, fontSize = 17.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nút "Back to Shop"
                Button(
                    onClick = {
                        navController.navigate(Screen.Home.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp)
                        .border(
                            2.dp,
                            mainColor,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Back to Shop",
                        color = Color.Black,
                        fontSize = 17.sp
//                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PurchaseSuccessScreenPreview() {
    PurchaseSuccessScreen(navController = rememberNavController())
}