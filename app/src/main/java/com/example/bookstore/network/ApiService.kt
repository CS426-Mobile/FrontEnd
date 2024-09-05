// ApiService.kt
import com.example.bookstore.network.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("/register/")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("/login/")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/logout/")
    suspend fun logoutUser(): Response<LogoutResponse>

    @GET("/user/get_user_info_from_cookies/")
    suspend fun getUserInfo(): Response<UserInfoResponse>

    @POST("/user/change_password/")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<Map<String, String>>

    @POST("/user/update_address_with_email/")
    suspend fun updateAddressByEmail(@Body request: UpdateAddressRequest): Response<Map<String, String>>

    @GET("/user/get_address/{user_email}/")
    suspend fun getAddressByEmail(@Path("user_email") userEmail: String): Response<Map<String, String>>
}
