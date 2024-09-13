package com.example.bookstore

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookstore.screen.AuthorDetailScreen
import com.example.bookstore.screen.BookDetailScreen
import com.example.bookstore.screen.CartScreen
import com.example.bookstore.screen.FavoriteScreen
import com.example.bookstore.screen.ListOrder
import com.example.bookstore.screen.OrderDetail
import com.example.bookstore.screen.PurchaseSuccessScreen
import com.example.bookstore.ui.theme.mainColor

val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Home()
        }
    }
}

@Composable
fun Home() {
    val navController = rememberNavController()
    Scaffold(
        //bottomBar = { BottomNavigationBar(navController = navController) }
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            if (currentDestination?.route in listOf(
                    BottomNavItem.Home.route,
                    BottomNavItem.Order.route,
                    BottomNavItem.Favorite.route,
                    BottomNavItem.Account.route,)) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHostContainer(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun NavHostContainer(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(
            route = Screen.Home.route,
            enterTransition = {null},
            exitTransition = {null},
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ) { HomeScreen(navController)}
        // Add AuthorDetailScreen with a dynamic author_name argument
        composable(
            route = Screen.Author.route,
            arguments = listOf(navArgument("author_name") { type = NavType.StringType }),
            enterTransition = { // Animation cho transition vào
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn()
            },
            exitTransition = { // Animation cho transition ra
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ) { backStackEntry ->
            // Extract the author_name argument from the back stack
            val authorName = backStackEntry.arguments?.getString("author_name")
            if (authorName != null) {
                AuthorDetailScreen(navController, authorName)
            }
        }

        composable(
            route = Screen.Order.route, // Định nghĩa route có argument
            arguments = listOf(navArgument("order_code") { type = NavType.StringType }),
            enterTransition = { // Animation cho transition vào
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn()
            },
            exitTransition = { // Animation cho transition ra
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }// Khai báo loại argument
        ) { backStackEntry ->
            val orderCode = backStackEntry.arguments?.getString("order_code") // Lấy giá trị argument
            if (orderCode != null)
                OrderDetail(navController, orderCode) // Truyền argument vào hàm
        }

        composable(
            route = Screen.Book.route, // Định nghĩa route có argument
            arguments = listOf(navArgument("book_name") { type = NavType.StringType }), // Khai báo loại argument
            enterTransition = { // Animation cho transition vào
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn()
            },
            exitTransition = { // Animation cho transition ra
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ) { backStackEntry ->
            val bookName = backStackEntry.arguments?.getString("book_name") // Lấy giá trị argument
            BookDetailScreen(navController, bookName) // Truyền argument vào hàm
        }

        composable(
            route = Screen.Cart.route,
            enterTransition = {null},
            exitTransition = {null},
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ) {
            CartScreen(navController)
        }
        composable(
            route = Screen.ListOrder.route,
            enterTransition = {null},
            exitTransition = {null},
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ) {
            ListOrder(navController)
        }
        composable(
            route = Screen.Favorite.route,
            enterTransition = {null},
            exitTransition = {null},
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ) {
            FavoriteScreen(navController)
        }
        composable(
            route = Screen.PasswordChange.route,
            enterTransition = { // Animation cho transition vào
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn()
            },
            exitTransition = { // Animation cho transition ra
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ) {
            ChangePasswordScreen(navController)
        }

        composable(
            route = Screen.Account.route,
            enterTransition = {null},
            exitTransition = {null},
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ) {
            AccountScreen(navController)
        }

        composable(
            route = Screen.ListAuthor.route,
            enterTransition = { // Animation cho transition vào
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn()
            },
            exitTransition = { // Animation cho transition ra
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ){
            ListAuthor(navController)
        }
        composable(
            route = Screen.Search.route,
            enterTransition = { // Animation cho transition vào
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn()
            },
            exitTransition = { // Animation cho transition ra
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ){
            SearchScreen(navController)
        }
        composable(
            route = Screen.Success.route,
            enterTransition = { // Animation cho transition vào
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn()
            },
            exitTransition = { // Animation cho transition ra
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
            }
        ){
            PurchaseSuccessScreen(navController)
        }
    }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        Log.d("Navigation", "Navigated to ${destination.route}")
        navController.currentBackStackEntry?.let { backStackEntry ->
            Log.d("Navigation", "Current back stack: ${backStackEntry.destination.route}")
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Order,
        BottomNavItem.Favorite,
        BottomNavItem.Account
    )
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                selectedContentColor = mainColor,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    Log.d("Navigation", "Navigating to ${item.route}")
                    navController.navigate(item.route) {
                        Log.d("Navigation", "Popping up to ${navController.graph.findStartDestination().route}")
                        popUpTo(navController.graph.findStartDestination().id) {
                            //saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(var title: String, var icon: Int, var route: String) {
    object Home : BottomNavItem("Home", R.drawable.ic_home, Screen.Home.route)
    object Order : BottomNavItem("Order", R.drawable.ic_cart, Screen.ListOrder.route)
    object Favorite : BottomNavItem("Favorite", R.drawable.ic_favo, Screen.Favorite.route)
    object Account : BottomNavItem("Account", R.drawable.ic_account, Screen.Account.route)
}