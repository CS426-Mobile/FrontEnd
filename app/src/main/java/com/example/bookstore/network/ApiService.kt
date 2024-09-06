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

    // Book APIs
    // Get 10 books
    @GET("/books/10/")
    suspend fun get10Books(): Response<List<SimpleBookResponse>>

    // Get 20 books
    @GET("/books/20/")
    suspend fun get20Books(): Response<List<SimpleBookResponse>>

    // Get books by category with optional filters
    @GET("/books/category/")
    suspend fun getBooksByCategory(
        @Query("category_name") categoryName: String,
        @Query("rating_optional") ratingOptional: String = "all",
        @Query("price_optional") priceOptional: String = "no",
        @Query("price_min") priceMin: Double = 0.0,
        @Query("price_max") priceMax: Double = 99999999.0,
        @Query("rating_sort") ratingSort: String = "none",
        @Query("price_sort") priceSort: String = "none"
    ): Response<List<BookCategoryResponse>>

    // Get book information by name
    @GET("/book/{book_name}/")
    suspend fun getBookInfo(@Path("book_name") bookName: String): Response<BookResponse>

    // Get number of books by author
    @GET("/books/author/{author_name}/count/")
    suspend fun getNumBooksByAuthor(@Path("author_name") authorName: String): Response<BookCountResponse>

    // Get categories of books by an author
    @GET("/books/author/{author_name}/categories/")
    suspend fun getAuthorCategories(@Path("author_name") authorName: String): Response<AuthorCategoriesResponse>

    // Get related books by book name
    @GET("/books/related/{book_name}/")
    suspend fun getRelatedBooks(@Path("book_name") bookName: String): Response<List<SimpleBookResponse>>
}
