package com.example.bookstore

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.bookstore.ui.theme.mainColor

class HomeActivity : AppCompatActivity() {
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
                    BottomNavItem.Cart.route,
                    BottomNavItem.Favorite.route,
                    BottomNavItem.Notification.route,
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
    NavHost(navController = navController, startDestination = BottomNavItem.Home.route, modifier = modifier) {
//        composable(BottomNavItem.Home.route) { HomeScreen(onNaviGateToTransport = { navController.navigate("transportBooking") })}

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
        BottomNavItem.Cart,
        BottomNavItem.Favorite,
        BottomNavItem.Notification,
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
                label = { Text(text = item.title) },
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
    object Home : BottomNavItem("Home", R.drawable.ic_home, "home")
    object Cart : BottomNavItem("Cart", R.drawable.ic_cart, "booking")
    object Favorite : BottomNavItem("Favorite", R.drawable.ic_favo, "booking")
    object Notification : BottomNavItem("Notification", R.drawable.ic_noti, "notification")
    object Account : BottomNavItem("Account", R.drawable.ic_account, "account")
}