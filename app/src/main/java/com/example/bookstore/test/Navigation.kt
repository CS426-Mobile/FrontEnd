//package com.example.bookstore.test
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.material.BottomNavigation
//import androidx.compose.material.BottomNavigationItem
//import androidx.compose.material.Icon
//import androidx.compose.material.Scaffold
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.res.painterResource
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.bookstore.R
//import com.example.bookstore.test.AccountScreen
//import com.example.bookstore.test.CartScreen
//import com.example.bookstore.test.FavoriteScreen
//import com.example.bookstore.HomeScreen
//import com.example.bookstore.ui.theme.BookstoreTheme
//
//class HomeActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            BookstoreTheme {
//                // Set up Navigation Host
//                val navController = rememberNavController()
//                HomeNavHost(navController = navController)
//            }
//        }
//    }
//}
//
//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Composable
//fun HomeNavHost(navController: NavHostController) {
//    // BottomNavigation Bar setup with NavHost for managing navigation between different screens
//    val items = listOf(
//        Screen.Home,
//        Screen.Favorite, // Placeholder for future screen
//        Screen.Cart, // Placeholder for future screen
//        Screen.Account // Placeholder for future screen
//    )
//
//    Scaffold(
//        bottomBar = {
//            BottomNavigationBar(
//                items = items,
//                navController = navController,
//                onItemClick = { navController.navigate(it.route) }
//            )
//        }
//    ) {
//        NavHost(navController = navController, startDestination = Screen.Home.route) {
//            composable(Screen.Home.route) { HomeScreen() }
//            composable(Screen.Favorite.route) { FavoriteScreen() } // Placeholder for the Favorite screen
//            composable(Screen.Cart.route) { CartScreen() } // Placeholder for the Cart screen
//            composable(Screen.Account.route) { AccountScreen() } // Placeholder for the Account screen
//        }
//    }
//}
//
//@Composable
//fun BottomNavigationBar(
//    items: List<Screen>,
//    navController: NavHostController,
//    onItemClick: (Screen) -> Unit
//) {
//    BottomNavigation {
//        val currentRoute = currentRoute(navController)
//        items.forEach { screen ->
//            BottomNavigationItem(
//                icon = { Icon(painter = painterResource(id = screen.icon), contentDescription = screen.label) },
//                label = { Text(screen.label) },
//                selected = currentRoute == screen.route,
//                onClick = { onItemClick(screen) }
//            )
//        }
//    }
//}
//
//@Composable
//fun currentRoute(navController: NavHostController): String? {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    return navBackStackEntry?.destination?.route
//}
//
//// Define different screens as sealed class for navigation
//sealed class Screen(val route: String, val label: String, val icon: Int) {
//    object Home : Screen("home", "Home", R.drawable.ic_home)
//    object Favorite : Screen("favorites", "Favorite", R.drawable.ic_favo)
//    object Cart : Screen("cart", "Cart", R.drawable.ic_cart)
//    object Account : Screen("account", "Account", R.drawable.ic_account)
//}