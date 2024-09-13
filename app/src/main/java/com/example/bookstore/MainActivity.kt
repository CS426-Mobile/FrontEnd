// MainActivity.kt
package com.example.bookstore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets.Type.statusBars
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.screen.LoginActivity
import com.example.bookstore.screen.OnboardingActivity
import com.example.bookstore.ui.theme.mainColor
import com.example.bookstore.viewmodel.UserViewModel

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Install the splash screen
        val splashScreen = installSplashScreen()

        // Ensure the splash screen is kept on until the loading state is finished
        var isLoading = true
        splashScreen.setKeepOnScreenCondition { isLoading }

        setContent {
            // Set mainColor as the background for the whole app during loading
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = mainColor
                ) {
                    val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
                    val onboardingComplete = sharedPref.getBoolean("chaptery_onboarding_complete", false)
                    val rememberedUserEmail = sharedPref.getString("remembered_user_email", null)
                    val rememberedUserPassword = sharedPref.getString("remembered_user_password", null)
                    val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

                    LaunchedEffect(Unit) {
                        if (rememberedUserEmail != null && rememberedUserPassword != null) {
                            // Automatically log in the remembered user
                            userViewModel.loginUser(rememberedUserEmail, rememberedUserPassword) { success, message ->
                                if (success) {
                                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                                    finish()
                                } else {
                                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                    finish()
                                }
                            }
                        } else if (!onboardingComplete) {
                            startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
                            finish()
                        } else {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                        isLoading = false
                    }

                    // Display a loading indicator with a mainColor background while loading
                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
