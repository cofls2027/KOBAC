package com.example.kobac_app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kobac_app.ui.screen.*

object AppRoutes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val CONNECT_ACCOUNT = "connect_account"
    const val MY_DATA_CONSENT = "my_data_consent"
    const val SELECT_BANK = "select_bank"
    const val CONNECTING = "connecting"
    const val CONNECTION_COMPLETE = "connection_complete"
    const val CONNECTED_ACCOUNT = "connected_account/{show_assets}" // Argument for showing assets
    const val SELECT_VIRTUAL_ASSET = "select_virtual_asset"
    const val ENTER_WALLET_ADDRESS = "enter_wallet_address"
    const val CONNECTING_VIRTUAL_ASSET = "connecting_virtual_asset"

    fun connectedAccountRoute(showAssets: Boolean) = "connected_account/$showAssets"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Start destination is now LOGIN
    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
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
        composable(AppRoutes.CONNECTING) {
            ConnectingScreen(navController = navController)
        }
        composable(AppRoutes.CONNECTION_COMPLETE) {
            ConnectionCompleteScreen(navController = navController)
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
        composable(AppRoutes.ENTER_WALLET_ADDRESS) {
            EnterWalletAddressScreen(navController = navController)
        }
        composable(AppRoutes.CONNECTING_VIRTUAL_ASSET) {
            ConnectingVirtualAssetScreen(navController = navController)
        }
    }
}
