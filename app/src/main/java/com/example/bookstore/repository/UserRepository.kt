// UserRepository.kt
package com.example.bookstore.repository

import android.util.Log
import com.example.bookstore.model.ChangePasswordRequest
import com.example.bookstore.model.LoginRequest
import com.example.bookstore.model.RegisterRequest
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.model.UpdateAddressRequest
import com.example.bookstore.model.UpdateAddressWithEmailRequest
import com.example.bookstore.model.UserInfo
import org.json.JSONObject

class UserRepository() {



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
                    // Handle non-2xx responses by parsing error body
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (errorBody != null) {
                        try {
                            // Parse the error body to extract the message field
                            JSONObject(errorBody).getString("message")
                        } catch (e: Exception) {
                            "Login failed with code: ${response.code()}."
                        }
                    } else {
                        "Login failed with code: ${response.code()}."
                    }
                    Result.failure(Exception(errorMessage))
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
                    Result.success(registerResponse.message)
                } else {
                    Result.failure(Exception(registerResponse?.message ?: "Registration failed with code${response.code()}."))
                }
            } else {
                // Handle non-2xx responses by parsing error body
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        // Parse the error body to extract the message field
                        JSONObject(errorBody).getString("message")
                    } catch (e: Exception) {
                        "Registration failed with code: ${response.code()}."
                    }
                } else {
                    "Registration failed with code: ${response.code()}."
                }
                Result.failure(Exception(errorMessage))
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
                // Handle non-2xx responses by parsing error body
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        // Parse the error body to extract the message field
                        JSONObject(errorBody).getString("message")
                    } catch (e: Exception) {
                        "Login failed with code: ${response.code()}."
                    }
                } else {
                    "Login failed with code: ${response.code()}."
                }
                Result.failure(Exception(errorMessage))
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