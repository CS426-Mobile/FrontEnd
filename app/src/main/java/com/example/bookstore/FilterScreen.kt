package com.example.bookstore

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.ui.theme.textColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    isSheetOpen: Boolean = false,
    onSheetOpenChange: (Boolean) -> Unit = {}
) {
    var priceFrom by remember { mutableStateOf("0") }
    var priceTo by remember { mutableStateOf("50") }
    var selectedStarRating by remember { mutableStateOf(1) }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                onSheetOpenChange(false)
            },
//        sheetState = rememberModalBottomSheetState(),
        ){
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Black,
                        elevation = 0.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Tiêu đề ở giữa
                            Text(
                                text = "Filter",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.Black // Thay thế bằng màu `textColor` nếu bạn đã định nghĩa trước
                            )

                            // Hàng chứa các icon ở hai bên
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Spacer(modifier = Modifier.weight(1f))

                                // Nút "Reset" bên phải
                                TextButton(
                                    onClick = {
                                        // Xử lý nút reset
                                    }
                                ) {
                                    Text(
                                        text = "Reset",
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                },
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(8.dp),
                    ) {
                        // Price Range Section
                        Text(
                            text = "Price Range",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        PriceRangeSlider()

                        // Star Rating Section
                        Text(
                            text = "Star Rating",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .padding(4.dp), // Đặt padding bên ngoài và bên trong
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (i in 1..5) {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 2.dp) // Khoảng cách giữa các ô
                                        .weight(1f) // Chia đều không gian cho mỗi ô
                                        .clip(RoundedCornerShape(24.dp))
                                        .size(48.dp)
                                        .border(
                                            1.dp,
                                            Color(0xFFC8C7CC),
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                        .background(
                                            if (selectedStarRating == i) mainColor else Color.Transparent, // Đổi màu nền khi ô được chọn
                                            shape = RoundedCornerShape(24.dp)
                                        )
                                        .clickable { selectedStarRating = i }, // Thay đổi trạng thái khi nhấn
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "$i",
                                            color = if (selectedStarRating == i) Color.White else Color.Black,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.width(4.dp)) // Khoảng cách giữa số và icon
                                        Icon(
                                            imageVector = Icons.Default.Star, // Thay thế bằng icon ngôi sao của bạn
                                            contentDescription = "Star",
                                            tint = if (selectedStarRating == i) Color.White else Color(0xFFFFC107)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        // Done Button
                        Button(
                            onClick = { /* Handle Done Click */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor  = mainColor,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text("Done", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Spacer(modifier = Modifier.size(24.dp))
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FilterScreenPreview() {
    FilterScreen()
}

@Composable
fun PriceRangeSlider() {
    // Trạng thái để lưu giá trị từ và đến của RangeSlider
    var sliderPosition by remember { mutableStateOf(1f..200f) }
    val sliderRange = 1f..200f

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hàng hiển thị thông tin giá trị của slider
        EditablePriceRange()

        // Hàng chứa thanh RangeSlider
        RangeSlider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = sliderRange,
            onValueChangeFinished = {
                // Xử lý khi người dùng hoàn thành thay đổi
            },
            colors = SliderDefaults.colors(
                thumbColor = mainColor, // Màu của điểm mốc
                activeTrackColor = Color(0xFFFF6F00), // Màu của thanh trượt khi đã chọn
                inactiveTrackColor = Color(0xFFEFEFF4) // Màu của thanh trượt khi chưa chọn
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditablePriceRange() {
    var fromValue by remember { mutableStateOf("2") } // Giá trị "From"
    var toValue by remember { mutableStateOf("50") } // Giá trị "To"
    var isFromFocused by remember { mutableStateOf(false) } // Trạng thái focus của ô "From"
    var isToFocused by remember { mutableStateOf(false) } // Trạng thái focus của ô "To"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start, // Không trải dài hết hàng
        modifier = Modifier
            .wrapContentWidth()
    ) {
        Text(text = "From", color = Color.Black, modifier = Modifier.padding(end = 8.dp)) // Thêm khoảng cách với ô

        // Ô nhập liệu "From"
        OutlinedTextField(
            value = fromValue,
            onValueChange = { fromValue = it },
            modifier = Modifier
                .width(100.dp) // Giảm chiều rộng của ô
                .border(
                    width = 1.dp,
                    color = if (isFromFocused) Color(0xFFFF6F00) else Color.Gray,
                    shape = RoundedCornerShape(24.dp)
                )
                .onFocusChanged { isFromFocused = it.isFocused },
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            trailingIcon = {
                Text(text = "$", color = Color.Black)
            },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center) // Canh giữa giá trị
        )

//        Spacer(modifier = Modifier.width(16.dp)) // Khoảng cách giữa các ô

        Text(text = "to", color = Color.Black, modifier = Modifier.padding(horizontal = 8.dp)) // Thêm khoảng cách với các ô

        // Ô nhập liệu "To"
        OutlinedTextField(
            value = toValue,
            onValueChange = { toValue = it },
            modifier = Modifier
                .width(100.dp) // Giảm chiều rộng của ô
                .border(
                    width = 1.dp,
                    color = if (isToFocused) Color(0xFFFF6F00) else Color.Gray,
                    shape = RoundedCornerShape(24.dp)
                )
                .onFocusChanged { isToFocused = it.isFocused },
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            trailingIcon = {
                Text(text = "$", color = Color.Black)
            },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center) // Canh giữa giá trị
        )
    }
}



