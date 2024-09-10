// ApiService.kt
import com.example.bookstore.model.AuthorCategoriesResponse
import com.example.bookstore.model.AuthorRequest
import com.example.bookstore.model.AuthorResponse
import com.example.bookstore.model.BookCategoryResponse
import com.example.bookstore.model.BookCountResponse
import com.example.bookstore.model.BookResponse
import com.example.bookstore.model.ChangePasswordRequest
import com.example.bookstore.model.ChangePasswordResponse
import com.example.bookstore.model.CustomerCartRequest
import com.example.bookstore.model.CustomerCartResponse
import com.example.bookstore.model.CustomerFavoriteRequest
import com.example.bookstore.model.CustomerFavoriteResponse
import com.example.bookstore.model.CustomerFollowRequest
import com.example.bookstore.model.CustomerFollowResponse
import com.example.bookstore.model.GetAddressResponse
import com.example.bookstore.model.LoginRequest
import com.example.bookstore.model.LoginResponse
import com.example.bookstore.model.LogoutResponse
import com.example.bookstore.model.NumFollowerResponse
import com.example.bookstore.model.QueryFollowResponse
import com.example.bookstore.model.RegisterRequest
import com.example.bookstore.model.RegisterResponse
import com.example.bookstore.model.SimpleAuthorResponse
import com.example.bookstore.model.SimpleBookResponse
import com.example.bookstore.model.TotalPriceResponse
import com.example.bookstore.model.UpdateAddressRequest
import com.example.bookstore.model.UpdateAddressResponse
import com.example.bookstore.model.UpdateAddressWithEmailRequest
import com.example.bookstore.model.UpdateCartResponse
import com.example.bookstore.model.UpdateFavoriteResponse
import com.example.bookstore.model.UserInfoResponse
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

    @POST("/user/update_address/")
    suspend fun updateAddress(@Body updateAddressRequest: UpdateAddressRequest): Response<UpdateAddressResponse>

    @POST("/user/update_address_with_email/")
    suspend fun updateAddressWithEmail(@Body updateAddressWithEmailRequest: UpdateAddressWithEmailRequest): Response<UpdateAddressResponse>

    @GET("/user/address/{user_email}/")
    suspend fun getAddress(@Path("user_email") userEmail: String): Response<GetAddressResponse>

    @POST("/user/change_password/")
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

    // Get books by matching string in the book name
    @GET("/books/matching_string/")
    suspend fun getBooksByMatchingString(
        @Query("category_name") categoryName: String = "",
        @Query("book_input") bookInput: String = "",
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

    // Get books by author
    @GET("/books/author/{author_name}/")
    suspend fun getBooksByAuthor(@Path("author_name") authorName: String): Response<List<SimpleBookResponse>>

    // Get categories of books by an author
    @GET("/books/author/{author_name}/categories/")
    suspend fun getAuthorCategories(@Path("author_name") authorName: String): Response<AuthorCategoriesResponse>

    // Get related books by book name
    @GET("/books/related/{book_name}/")
    suspend fun getRelatedBooks(@Path("book_name") bookName: String): Response<List<SimpleBookResponse>>

    // Customer Favorite APIs
    // Insert a customer favorite
    @POST("/favorite/insert/")
    suspend fun insertCustomerFavorite(@Body request: CustomerFavoriteRequest): Response<UpdateFavoriteResponse>

    // Delete a customer favorite
    @HTTP(method = "DELETE", path = "/favorite/delete/", hasBody = true)
    suspend fun deleteCustomerFavorite(@Body request: CustomerFavoriteRequest): Response<UpdateFavoriteResponse>

    // Query customer favorites by user email
    @GET("/favorite/query/")
    suspend fun queryCustomerFavorite(@Query("user_email") userEmail: String): Response<List<CustomerFavoriteResponse>>

    // Query if a customer favorite exists
    @GET("/favorite/query_exist/")
    suspend fun queryCustomerFavoriteExist(
        @Query("user_email") userEmail: String,
        @Query("book_name") bookName: String
    ): Response<UpdateFavoriteResponse>

    // Customer Cart APIs
    // Insert a book into the customer cart
    @POST("/cart/insert/")
    suspend fun insertCustomerCartBook(@Body request: CustomerCartRequest): Response<UpdateCartResponse>

    // Delete a book from the customer cart
    @HTTP(method = "DELETE", path = "/cart/delete/", hasBody = true)
    suspend fun deleteCustomerCartBook(@Body request: CustomerCartRequest): Response<UpdateCartResponse>

    // Increase the number of books in the cart
    @POST("/cart/increase/")
    suspend fun increaseNumBooks(@Body request: CustomerCartRequest): Response<UpdateCartResponse>

    // Decrease the number of books in the cart
    @POST("/cart/decrease/")
    suspend fun decreaseNumBooks(@Body request: CustomerCartRequest): Response<UpdateCartResponse>

    // Calculate the total price of the cart
    @GET("/cart/total_price/")
    suspend fun calculateTotalPrice(@Query("user_email") userEmail: String): Response<TotalPriceResponse>

    // Query all customer cart books for a user
    @GET("/cart/query/")
    suspend fun queryCustomerCartBooks(@Query("user_email") userEmail: String): Response<List<CustomerCartResponse>>

    // Query authors that a user follows
    @GET("/followers/author/query/")
    suspend fun queryCustomerFollow(@Query("user_email") userEmail: String): Response<List<CustomerFollowResponse>>

    // Toggle follow/unfollow status of an author
    @POST("/followers/toggle/")
    suspend fun toggleFollow(@Body request: CustomerFollowRequest): Response<RegisterResponse>

    // Get the number of followers for an author
    @GET("/followers/author/count/")
    suspend fun getNumFollowersForAuthor(@Query("author_name") authorName: String): Response<NumFollowerResponse>

    // Query follow status of an author by user
    @GET("/followers/query/")
    suspend fun queryFollow(
        @Query("author_name") authorName: String,
        @Query("user_email") userEmail: String
    ): Response<QueryFollowResponse>
}
