// ApiService.kt
import com.example.bookstore.network.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("/register/")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("/login/")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("/user/get_user_info_from_cookies/")
    suspend fun getUserInfo(): Response<UserInfoResponse>

    @POST("/user/update_address_with_email/")
    suspend fun updateAddressByEmail(@Body request: UpdateAddressRequest): Response<Map<String, String>>

    @GET("/user/get_address/{user_email}/")
    suspend fun getAddressByEmail(@Path("user_email") userEmail: String): Response<Map<String, String>>

    @POST("/update_address/")
    suspend fun updateAddress(@Body updateAddressRequest: UpdateAddressRequest): Response<UpdateAddressResponse>

    @POST("/update_address_with_email/")
    suspend fun updateAddressWithEmail(@Body updateAddressWithEmailRequest: UpdateAddressWithEmailRequest): Response<UpdateAddressResponse>

    @GET("/get_address/{user_email}/")
    suspend fun getAddress(@Path("user_email") userEmail: String): Response<GetAddressResponse>

    @POST("/change_password/")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse>

    @POST("/logout/")
    suspend fun logoutUser(): Response<LogoutResponse>
}
