package com.example.kobac_app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kobac_app.ui.screen.*

object AppRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val HOME = "home"
    const val CONNECT_ACCOUNT = "connect_account"
    const val MY_DATA_CONSENT = "my_data_consent"
    const val SELECT_BANK = "select_bank"
    const val CONNECTED_ACCOUNT = "connected_account/{show_assets}" // Argument for showing assets
    const val SELECT_VIRTUAL_ASSET = "select_virtual_asset"

    fun connectedAccountRoute(showAssets: Boolean) = "connected_account/$showAssets"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.SPLASH) {
        // ... other composables
        composable(AppRoutes.SPLASH) {
            SplashScreen(navController = navController)
        }
        composable(AppRoutes.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(AppRoutes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(AppRoutes.CONNECT_ACCOUNT) {
            ConnectAccountScreen(navController = navController)
        }
        composable(AppRoutes.MY_DATA_CONSENT) {
            MyDataConsentScreen(navController = navController)
        }
        composable(AppRoutes.SELECT_BANK) {
            SelectBankScreen(navController = navController)
        }
        composable(
            route = AppRoutes.CONNECTED_ACCOUNT,
            arguments = listOf(navArgument("show_assets") { type = NavType.BoolType })
        ) { backStackEntry ->
            val showAssets = backStackEntry.arguments?.getBoolean("show_assets") ?: false
            ConnectedAccountScreen(navController = navController, showVirtualAssets = showAssets)
        }
        composable(AppRoutes.SELECT_VIRTUAL_ASSET) {
            SelectVirtualAssetScreen(navController = navController)
        }
    }
}
