package com.example.bookstore

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.components.CustomTextField
import com.example.bookstore.data.AppDatabase
import com.example.bookstore.model.User
import com.example.bookstore.repository.UserRepository
import com.example.bookstore.ui.theme.blurOrange
import com.example.bookstore.ui.theme.errorColor
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.UserViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    // Initialize UserViewModel using viewModel() function
//                    val userViewModel: UserViewModel = viewModel()
                    val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

                    LoginNavHost(userViewModel)
                }
            }
        }
    }
}

@Composable
fun LoginNavHost(userViewModel: UserViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "sign_in") {
        composable("sign_in") { SignInScreen(navController, userViewModel) }
        composable("sign_up") { SignUpScreen(navController, userViewModel) }
    }
}

@Composable
fun SignInScreen(navController: NavController, userViewModel: UserViewModel) {
//    val userViewModel: UserViewModel = viewModel()

    SignInUI(
        onSignInSuccess = {
//             Navigate to HomeActivity or any other screen on successful sign-in
            navController.context.startActivity(Intent(navController.context, OnboardingActivity::class.java))
        },
        onSignUpClick = {
            navController.navigate("sign_up")
        },
        userViewModel = userViewModel
    )
}

@Composable
fun SignUpScreen(navController: NavController, userViewModel: UserViewModel) {
//    val userViewModel: UserViewModel = viewModel()

    SignUpUI(
        onSignUpSuccess = {
            navController.navigate("sign_in") {
                popUpTo("sign_up") { inclusive = true }
            }
        },
        userViewModel = userViewModel
    )
}

@Composable
fun SignInUI(onSignInSuccess: () -> Unit, onSignUpClick: () -> Unit, userViewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    val isFormValid = email.isNotBlank() && password.isNotBlank()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Feather Image
            Image(
                painter = painterResource(id = R.drawable.feather),
                contentDescription = "Feather",
                modifier = Modifier
                    .padding(bottom = 0.dp)
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Welcome Texts
            Text(text = "Welcome Back!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = "Login to your existing account", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            // Error message placeholder
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = errorColor)
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Email TextField
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email or Phone number",
                icon = R.drawable.ic_email
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Password TextField
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isError = errorMessage.isNotEmpty(),
                isPassword = true,
                icon = R.drawable.ic_password
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Remember Me Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Custom Circular Checkbox
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { rememberMe = !rememberMe }
                        .background(
                            color = if (rememberMe) Color.White else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = if (rememberMe) mainColor else Color.Gray,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (rememberMe) {
                        Canvas(modifier = Modifier.size(10.dp)) {
                            drawCircle(mainColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Remember me",
                    fontSize = 16.sp,
                    color = if (rememberMe) mainColor else Color.Gray,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Button
            Button(
                onClick = {
                    if (isFormValid) {
                        userViewModel.getUser(email, password) { user ->
                            if (user != null) {
                                onSignInSuccess()
                            } else {
                                errorMessage = "Incorrect password or account does not exist"
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isFormValid) mainColor else blurOrange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Sign in",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Text
            Row {
                Text(
                    text = "Don’t have an account?",
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign up",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignUpClick() },
                    color = mainColor
                )
            }
        }
    }
}

@Composable
fun SignUpUI(onSignUpSuccess: () -> Unit, userViewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val isFormValid = email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Feather Image
        Image(
            painter = painterResource(id = R.drawable.feather),
            contentDescription = "Feather",
            modifier = Modifier
                .padding(bottom = 0.dp)
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Welcome Texts
        Text(text = "Create New Account", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = "Register new account", color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        // Error message placeholder
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = errorColor)
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Spacer(modifier = Modifier.height(24.dp)) // Placeholder for error message space
        }

        // Email TextField
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email or Phone number",
            icon = R.drawable.ic_email
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Password TextField
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true,
            icon = R.drawable.ic_password
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password TextField
        CustomTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm Password",
            isPassword = true,
            icon = R.drawable.ic_password
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Sign Up Button
        Button(
            onClick = {
                if (isFormValid) {
                    if (password == confirmPassword) {
                        userViewModel.insertUser(User(0, email, password))
                        onSignUpSuccess()
                    } else {
                        errorMessage = "Passwords do not match"
                    }
                } else {
                    errorMessage = "All fields are required"
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isFormValid) mainColor else blurOrange,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Sign up",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}