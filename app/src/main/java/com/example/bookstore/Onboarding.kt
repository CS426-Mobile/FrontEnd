package com.example.bookstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import androidx.core.view.*
import com.example.bookstore.ui.theme.grey
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.ui.theme.textColor

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.d("FlightScreen", "Flight: ")
            OnboardingScreen {
                val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("chaptery_onboarding_complete", true)
                    apply()
                }
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}
//
//@OptIn(ExperimentalPagerApi::class)
//@Composable
//fun CustomPagerIndicator(
//    pagerState: PagerState,
//    modifier: Modifier = Modifier,
//    activeColor: Color = Color(0xFFFF6F00), // Màu cam cho trạng thái active
//    inactiveColor: Color = Color.White, // Màu trắng cho trạng thái inactive
//    borderColor: Color = Color(0xFFFF6F00), // Màu viền cam cho trạng thái inactive
//    indicatorSize: Dp = 8.dp,
//    spacing: Dp = 8.dp
//) {
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(spacing)
//    ) {
//        repeat(pagerState.pageCount) { page ->
//            Box(
//                modifier = Modifier
//                    .size(indicatorSize)
//                    .border(
//                        width = 1.dp,
//                        color = if (pagerState.currentPage == page) activeColor else borderColor, // Viền cam cho trạng thái inactive
//                        shape = CircleShape
//                    )
//                    .background(
//                        color = if (pagerState.currentPage == page) activeColor else inactiveColor,
//                        shape = CircleShape
//                    )
//            )
//        }
//    }
//}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = {
                scope.launch {
                    onFinish()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Skip", color = Color.Gray, fontSize = 16.sp)
        }
        HorizontalPager(
            count = 3,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> OnboardingPage1()
                1 -> OnboardingPage2()
                2 -> OnboardingPage3()
            }
        }

//        CustomPagerIndicator(
//            pagerState = pagerState,
//            modifier = Modifier.padding(16.dp)
//        )
        // Pager Indicator
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.padding(16.dp),
            indicatorWidth = 11.dp,
            indicatorHeight = 11.dp,
            activeColor = Color(0xFFFF6F00),
            inactiveColor = Color.LightGray
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                scope.launch {
                    if (pagerState.currentPage + 1 < 3) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        onFinish()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = mainColor,
                contentColor = Color.White
            ),
            modifier = Modifier
//                .padding(if (pagerState.currentPage + 1 < 3) 0.dp else 16.dp)
                .width(if (pagerState.currentPage + 1 < 3) 93.dp else 200.dp)
                .align(if (pagerState.currentPage + 1 < 3) Alignment.End else Alignment.CenterHorizontally)
                .height(48.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(text = if (pagerState.currentPage + 1 < 3) "Next" else "Get started",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White)
        }
    }
}

@Composable
fun OnboardingPage1() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding1), // Thay 'your_image' bằng ID hình ảnh của bạn
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(top = 16.dp) // Thêm khoảng cách ở trên hình ảnh nếu cần
        )

        Spacer(modifier = Modifier.height(40.dp)) // Khoảng cách giữa hình ảnh và dòng chữ đầu tiên

        // Dòng chữ tiêu đề
        Text(
            text = "Have you read today?",
            //style = MaterialTheme.typography.h6, // Thay đổi kiểu chữ theo MaterialTheme
            color = textColor, // Đặt màu chữ
            fontSize = 30.sp, // Đặt kích thước chữ
            fontWeight = FontWeight.Bold, // Đặt chữ đậm
            textAlign = TextAlign.Center // Căn giữa chữ
        )

        // Dòng chữ mô tả
        Text(
            text = "Read every day. The benefits are well charted.",
            color = Color(0xFF6D727A), // Đặt màu chữ
            fontSize = 18.sp, // Đặt kích thước chữ
            //style = MaterialTheme.typography.body2, // Thay đổi kiểu chữ theo MaterialTheme
            textAlign = TextAlign.Center, // Căn giữa chữ
            modifier = Modifier.padding(top = 16.dp, ) // Khoảng cách giữa 2 dòng chữ
        )
    }
}

@Composable
fun OnboardingPage2() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding2), // Thay 'your_image' bằng ID hình ảnh của bạn
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(top = 16.dp) // Thêm khoảng cách ở trên hình ảnh nếu cần
        )

        Spacer(modifier = Modifier.height(40.dp)) // Khoảng cách giữa hình ảnh và dòng chữ đầu tiên

        // Dòng chữ tiêu đề
        Text(
            text = "Find your favorite books",
            //style = MaterialTheme.typography.h6, // Thay đổi kiểu chữ theo MaterialTheme
            color = textColor, // Đặt màu chữ
            fontSize = 30.sp, // Đặt kích thước chữ
            fontWeight = FontWeight.Bold, // Đặt chữ đậm
            textAlign = TextAlign.Center // Căn giữa chữ
        )

        // Dòng chữ mô tả
        Text(
            text = "Access anytime, anywhere, any device.",
            color = Color(0xFF6D727A), // Đặt màu chữ
            fontSize = 18.sp, // Đặt kích thước chữ
            //style = MaterialTheme.typography.body2, // Thay đổi kiểu chữ theo MaterialTheme
            textAlign = TextAlign.Center, // Căn giữa chữ
            modifier = Modifier.padding(top = 16.dp, ) // Khoảng cách giữa 2 dòng chữ
        )
    }
}

@Composable
fun OnboardingPage3() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding3), // Thay 'your_image' bằng ID hình ảnh của bạn
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(top = 16.dp) // Thêm khoảng cách ở trên hình ảnh nếu cần
        )

        Spacer(modifier = Modifier.height(40.dp)) // Khoảng cách giữa hình ảnh và dòng chữ đầu tiên

        // Dòng chữ tiêu đề
        Text(
            text = "Unlimited Knowledge",
            //style = MaterialTheme.typography.h6, // Thay đổi kiểu chữ theo MaterialTheme
            color = textColor, // Đặt màu chữ
            fontSize = 30.sp, // Đặt kích thước chữ
            fontWeight = FontWeight.Bold, // Đặt chữ đậm
            textAlign = TextAlign.Center // Căn giữa chữ
        )

        // Dòng chữ mô tả
        Text(
            text = "Easily get an almost unlimited amount of books.",
            color = Color(0xFF6D727A), // Đặt màu chữ
            fontSize = 18.sp, // Đặt kích thước chữ
            //style = MaterialTheme.typography.body2, // Thay đổi kiểu chữ theo MaterialTheme
            textAlign = TextAlign.Center, // Căn giữa chữ
            modifier = Modifier.padding(top = 16.dp, ) // Khoảng cách giữa 2 dòng chữ
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(onFinish = {})
}