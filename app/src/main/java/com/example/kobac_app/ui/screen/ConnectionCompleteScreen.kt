package com.example.kobac_app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kobac_app.ui.AppRoutes
import com.example.kobac_app.ui.theme.Black
import com.example.kobac_app.ui.theme.Gray
import com.example.kobac_app.ui.theme.KOBAC_appTheme
import kotlinx.coroutines.delay

@Composable
fun ConnectionCompleteScreen(navController: NavController, isVirtualAsset: Boolean) {
    LaunchedEffect(Unit) {
        delay(2000) // 2초 후 최종 화면으로 이동
        val route = AppRoutes.connectedAccountRoute(showAssets = isVirtualAsset)
        navController.navigate(route) {
            popUpTo(AppRoutes.HOME) { inclusive = false } // Keep Home on the back stack
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연결 완료!",
            fontSize = 18.sp,
            color = Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "~~님,\n환영합니다!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            textAlign = TextAlign.Center,
            lineHeight = 48.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectionCompleteScreenPreview() {
    KOBAC_appTheme {
        ConnectionCompleteScreen(navController = rememberNavController(), isVirtualAsset = false)
    }
}
