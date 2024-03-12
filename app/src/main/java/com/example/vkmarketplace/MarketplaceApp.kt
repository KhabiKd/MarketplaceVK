package com.example.vkmarketplace

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vkmarketplace.ui.screens.ProductInfo
import com.example.vkmarketplace.ui.screens.ProductListScreen

@Composable
fun MarketplaceApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "productListScreen",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = "productListScreen") {
            ProductListScreen(
                onProductClick = { title, description ->
                    navController.navigate("productInfo/${title}/${description}")
                }
            )
        }
        
        composable(
            route = "productInfo/{title}/{description}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ProductInfo(title = backStackEntry.arguments?.getString("title") ?: "", description = backStackEntry.arguments?.getString("description") ?: "")
        }
    }
}