// ApiService.kt
import com.example.bookstore.network.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // User APIs
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

    // Author APIs
    // Get author info by name
    @GET("/author/")
    suspend fun getAuthor(
        @Query("author_name") authorName: String
    ): Response<AuthorResponse>

    // Add a new author
    @PUT("/author/")
    suspend fun addAuthor(
        @Body authorRequest: AuthorRequest
    ): Response<RegisterResponse>

    // Get all authors
    @GET("/all_authors/")
    suspend fun getAllAuthors(): Response<List<AuthorResponse>>

    // Get top 5 popular authors
    @GET("/author/popular_5/")
    suspend fun getTop5PopularAuthors(): Response<List<AuthorResponse>>

    // Get simple author info
    @GET("/author/simple/")
    suspend fun getSimpleAuthors(
        @Query("author_name") authorName: String
    ): Response<List<SimpleAuthorResponse>>

    // Get matching authors by string
    @GET("/author/match_string/")
    suspend fun getMatchingAuthors(
        @Query("author_name") authorName: String
    ): Response<List<SimpleAuthorResponse>>

    // Get all info of an author
    @GET("/author/info/")
    suspend fun getAuthorInfo(
        @Query("author_name") authorName: String
    ): Response<AuthorResponse>
}
