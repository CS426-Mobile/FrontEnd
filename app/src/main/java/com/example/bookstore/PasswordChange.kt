package com.example.bookstore

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.components.CustomTextField
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.ui.theme.mainColor


@Composable
fun ChangePasswordScreen(navController: NavHostController) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Change Password", navController = navController, isBack = true)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f).pointerInput(Unit) {
                        detectTapGestures(onTap = { focusManager.clearFocus() })
                    },
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Trường nhập mật khẩu cũ
                    CustomTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = "New Password",
                        isError = errorMessage.isNotEmpty(),
                        isPassword = true,
                        icon = R.drawable.ic_password
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Confirm Password TextField
                    CustomTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirm New Password",
                        isError = errorMessage.isNotEmpty(),
                        isPassword = true,
                        icon = R.drawable.ic_password
                    )
                }

                // Nút đổi mật khẩu
                Button(
                    onClick = {
                        if (newPassword == confirmPassword) {
                            // Xử lý logic đổi mật khẩu
                            navController.popBackStack() // Quay lại màn hình trước đó sau khi đổi mật khẩu thành công
                        } else {
                            errorMessage = "New password and confirm password do not match"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainColor,
                        contentColor = Color.White
                    )
                ) {
                    Text("Change Password")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    ChangePasswordScreen(navController = rememberNavController())
}