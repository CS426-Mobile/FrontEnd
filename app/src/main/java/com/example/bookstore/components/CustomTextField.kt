package com.example.bookstore.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstore.R
import com.example.bookstore.ui.theme.errorColor
import com.example.bookstore.ui.theme.mainColor

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
                        tint = if (isError) errorColor else Color.Gray,
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
