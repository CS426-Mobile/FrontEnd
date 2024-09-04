package com.example.bookstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.test.HomeActivity
import com.example.bookstore.viewmodel.UserViewModel

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val onboardingComplete = sharedPref.getBoolean("chaptery_onboarding_complete", false)

        // Check if user is remembered
        val rememberedUserEmail = sharedPref.getString("remembered_user_email", null)

        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        if (rememberedUserEmail != null) {
            Log.d("remembered_user_email: ", rememberedUserEmail)
        }
        else
            Log.d("remembered_user_email: ", "null")
        if (rememberedUserEmail != null) {
            // Automatically log in the remembered user
            userViewModel.getUserByEmail(rememberedUserEmail) { user ->
                if (user != null) {
                    // User found, navigate to HomeActivity
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    // Invalid credentials, fallback to login
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } else if (!onboardingComplete) {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
