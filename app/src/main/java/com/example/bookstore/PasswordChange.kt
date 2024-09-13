package com.example.bookstore

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bookstore.components.CustomTextField
import com.example.bookstore.components.CustomTopAppBar
import com.example.bookstore.ui.theme.blurOrange
import com.example.bookstore.ui.theme.errorColor
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.UserViewModel

@Composable
fun ChangePasswordScreen(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isFormValid by remember {
        mutableStateOf(false)
    } // Determine if form inputs are valid

    val focusManager = LocalFocusManager.current

    // Validate form fields
    LaunchedEffect(oldPassword, newPassword, confirmPassword) {
        isFormValid = oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()
    }

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
                    modifier = Modifier
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { focusManager.clearFocus() })
                        },
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Error message placeholder
                    if (errorMessage.isNotEmpty()) {
                        androidx.compose.material.Text(text = errorMessage, color = errorColor)
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Spacer(modifier = Modifier.height(24.dp)) // Placeholder for error message space
                    }
                    // Old Password TextField
                    CustomTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = "Old Password",
                        isError = errorMessage.isNotEmpty(),
                        isPassword = true,
                        icon = R.drawable.ic_password
                    )

                    // New Password TextField
                    CustomTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = "New Password",
                        isError = errorMessage.isNotEmpty(),
                        isPassword = true,
                        icon = R.drawable.ic_password
                    )

                    // Confirm New Password TextField
                    CustomTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirm New Password",
                        isError = errorMessage.isNotEmpty(),
                        isPassword = true,
                        icon = R.drawable.ic_password
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Change Password Button
                Button(
                    onClick = {
                        if (newPassword == confirmPassword) {
                            isLoading = true
                            errorMessage = ""
                            userViewModel.changePassword(
                                oldPassword = oldPassword,
                                newPassword = newPassword,
                                confirmPassword = confirmPassword
                            ) { success, message ->
                                isLoading = false
                                if (success) {
                                    // Notify the user about success or navigate back
                                    navController.popBackStack()
                                } else {
                                    errorMessage = message ?: "Failed to change password. Please try again."
                                }
                            }
                        } else {
                            errorMessage = "Passwords do not match."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isFormValid) mainColor else blurOrange,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = isFormValid && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Change Password",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    )
}
