// UserRepository.kt
package com.example.bookstore.repository

import android.util.Log
import com.example.bookstore.data.UserDao
import com.example.bookstore.model.UserEntity
import com.example.bookstore.network.ChangePasswordRequest
import com.example.bookstore.network.LoginRequest
import com.example.bookstore.network.RegisterRequest
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.network.UpdateAddressRequest
import com.example.bookstore.network.UpdateAddressWithEmailRequest
import com.example.bookstore.network.UserInfo
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UserRepository(private val userDao: UserDao) {

    // Room Database operations
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUser(email: String, password: String): UserEntity? {
        return userDao.getUser(email, password)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    // API operations with error messages
    suspend fun loginUser(email: String, password: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.loginUser(LoginRequest(email, password))
            when {
                response.isSuccessful -> {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.message == "Login successful") {
                        Result.success(loginResponse.message)
                    } else {
                        // Handle unexpected response body
                        Result.failure(Exception(loginResponse?.message ?:"Unexpected error"))
                    }
                }
                else -> {
                    // Handle other response codes
                    Result.failure(Exception(response.body()?.message ?: "Login failed with code: ${response.code()}."))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun registerUser(email: String, password: String, confirmPassword: String): Result<String> {
        return try {
            Log.d("RegisterUser", "Attempting to register user with email: $email")
            val response = RetrofitInstance.api.registerUser(RegisterRequest(email, password, confirmPassword))
            if (response.isSuccessful) {
                val registerResponse = response.body()
                if (registerResponse != null && registerResponse.message == "Account was created successfully") {
                    // Save user to Room database if registration is successful
                    insertUser(UserEntity(email = email, password = password))
                    Result.success(registerResponse.message)
                } else {
                    Result.failure(Exception(registerResponse?.message ?: "Registration failed with code${response.code()}."))
                }
            } else {
                Result.failure(Exception(response.body()?.message ?: "Registration failed with code ${response.code()}."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun getUserInfo(): Result<UserInfo> {
        return try {
            val response = RetrofitInstance.api.getUserInfo()
            if (response.isSuccessful) {
                val userInfo = response.body()?.user
                Result.success(userInfo ?: UserInfo("", ""))
            } else {
                Result.failure(Exception("Failed to retrieve user info with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.changePassword(ChangePasswordRequest(oldPassword, newPassword, confirmPassword))
            if (response.isSuccessful) {
                val changePasswordResponse = response.body()
                Result.success(changePasswordResponse?.message ?: "Password changed successfully")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Password change failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun updateAddress(address: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.updateAddress(UpdateAddressRequest(address))
            if (response.isSuccessful) {
                val updateAddressResponse = response.body()
                Result.success(updateAddressResponse?.message ?: "Address updated successfully")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Address update failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun updateAddressWithEmail(email: String, address: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.updateAddressWithEmail(UpdateAddressWithEmailRequest(email, address))
            if (response.isSuccessful) {
                val updateAddressResponse = response.body()
                Result.success(updateAddressResponse?.message ?: "Address updated successfully")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Address update failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun getAddress(email: String): Result<String> {
        return try {
            val response = RetrofitInstance.api.getAddress(email)
            if (response.isSuccessful) {
                val getAddressResponse = response.body()
                Result.success(getAddressResponse?.address ?: "Address not found")
            } else {
                Result.failure(Exception(response.body()?.address ?: "Get address failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }

    suspend fun logoutUser(): Result<String> {
        return try {
            val response = RetrofitInstance.api.logoutUser()
            if (response.isSuccessful) {
                val logoutResponse = response.body()
                Result.success(logoutResponse?.message ?: "Logout successful")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Logout failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or HTTP error: ${e.message}"))
        }
    }
}