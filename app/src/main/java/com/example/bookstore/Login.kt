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
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookstore.ui.theme.blurOrange
import com.example.bookstore.ui.theme.errorColor
import com.example.bookstore.ui.theme.mainColor

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    LoginNavHost()
                }
            }
        }
    }
}

@Composable
fun LoginNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "sign_in") {
        composable("sign_in") { SignInScreen(navController) }
        composable("sign_up") { SignUpScreen(navController) }
    }
}

@Composable
fun SignInScreen(navController: NavController) {
    SignInUI(
        onSignInSuccess = {
//            navController.context.startActivity(Intent(navController.context, HomeActivity::class.java))
        },
        onSignUpClick = {
            navController.navigate("sign_up")
        }
    )
}

@Composable
fun SignUpScreen(navController: NavController) {
    SignUpUI(
        onSignUpSuccess = {
            navController.navigate("sign_in") {
                popUpTo("sign_up") { inclusive = true }
            }
        }
    )
}

@Composable
fun SignInUI(onSignInSuccess: () -> Unit, onSignUpClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    val isFormValid = email.isNotBlank() && password.isNotBlank()
    val focusManager = LocalFocusManager.current

    // Wrapper Box to detect taps outside of the text fields
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
                        if (email == "example@gmail.com" && password == "password") {
                            onSignInSuccess()
                        } else {
                            errorMessage = "Incorrect password or account does not exist"
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
fun SignUpUI(onSignUpSuccess: () -> Unit) {
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
            painter = painterResource(id = R.drawable.feather), // replace with actual feather image
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
            isError = errorMessage.isNotEmpty(),
            isPassword = true,
            icon = R.drawable.ic_password
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password TextField
        CustomTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm Password",
            isError = errorMessage.isNotEmpty(),
            isPassword = true,
            icon = R.drawable.ic_password
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Button
        Button(
            onClick = {
                if (isFormValid) {
                    if (password == confirmPassword) {
                        onSignUpSuccess()
                    } else {
                        errorMessage = "Passwords do not match"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isFormValid) mainColor else blurOrange, // Set the color based on form validity
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp), // Rounded corners for the button
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Sign up",
                fontSize = 18.sp, // Increase font size for the button text
                fontWeight = FontWeight.Bold, // Make the text bold
                color = Color.White // Ensure the text color is white
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sign In Text
        Row {
            Text(
                text = "Already have an account?",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sign in",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onSignUpSuccess() },
                color = mainColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    isPassword: Boolean = false,
    icon: Int? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = when {
                    isError -> errorColor
                    isFocused.value -> mainColor
                    else -> Color.Gray
                }
            )
        },
        isError = isError,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                        ),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = when {
                            isError -> errorColor
                            else -> Color.Gray
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        leadingIcon = {
            icon?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = when {
                        isError -> errorColor
                        isFocused.value -> mainColor
                        else -> Color.Gray
                    },
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = if (isError) errorColor else Color.Black
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isError) errorColor else mainColor,
            unfocusedBorderColor = if (isError) errorColor else Color.Gray,
            backgroundColor = if (isFocused.value) Color.White else Color.Transparent
        ),
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState -> isFocused.value = focusState.isFocused }
            .focusRequester(focusRequester)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview1() {
    MaterialTheme {
        SignInUI(onSignInSuccess = {}, onSignUpClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    MaterialTheme {
        SignUpUI(onSignUpSuccess = {})
    }
}
