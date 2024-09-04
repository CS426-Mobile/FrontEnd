package com.example.bookstore.viewmodel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.Composable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.BookStoreDatabase
import com.example.bookstore.model.UserEntity
import com.example.bookstore.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    init {
        val userDao = BookStoreDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
    }

    fun insertUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    fun getUser(email: String, password: String, onResult: (UserEntity?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUser(email, password)
            onResult(user)
        }
    }

    fun getUserByEmail(email: String, onResult: (UserEntity?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            onResult(user)
        }
    }

    // New method to update the address
    fun updateAddress(email: String, address: String) {
        viewModelScope.launch {
            userRepository.updateAddress(email, address)
        }
    }

    // New method to handle logout
    fun logoutUser() {
        viewModelScope.launch {
            // Clear the remembered user from local storage
            val sharedPref = getApplication<Application>().getSharedPreferences("app_preferences", MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("remembered_user_email")
                apply()
            }
        }
    }

    /* UI Code for Log out
    @Composable
    fun AccountScreen(userViewModel: UserViewModel) {
        // Assuming there's a button to trigger logout
        Button(onClick = {
            userViewModel.logoutUser()
            // Navigate to the login screen
            val context = LocalContext.current
            context.startActivity(Intent(context, LoginActivity::class.java))
            (context as? Activity)?.finish()
        }) {
            Text("Log Out")
        }
    }
     */
}
