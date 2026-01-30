package com.example.kobac_app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kobac_app.R
import com.example.kobac_app.ui.AppRoutes
import com.example.kobac_app.ui.theme.Black
import com.example.kobac_app.ui.theme.KOBAC_appTheme
import com.example.kobac_app.data.api.RetrofitClient
import com.example.kobac_app.data.api.ApiService
import com.example.kobac_app.data.model.ConnectRequest
import com.example.kobac_app.data.util.TokenManager
import kotlinx.coroutines.delay

@Composable
fun ConnectingVirtualAssetScreen(navController: NavController) {
    val images = remember {
        listOf(
            R.drawable.btc,
            R.drawable.eth,
            R.drawable.sol,
            R.drawable.xrp
        )
    }
    var currentImageIndex by remember { mutableStateOf(0) }
    val apiService = remember { RetrofitClient.createService<ApiService>() }

    LaunchedEffect(Unit) {
        try {
            val cryptoAddresses = TokenManager.getCryptoAddresses()
            val request = ConnectRequest(
                mockToken = "test-token-123",
                userSearchId = "user_ci_12345_xyz", // 고정값
                cryptoAddresses = cryptoAddresses
            )
            apiService.connectMyData(request)

            // 성공 시 (2xx 응답)
            navController.navigate(AppRoutes.connectionCompleteRoute(isVirtualAsset = true))
        } catch (e: Exception) {
            // 실패 시 (네트워크 오류 또는 2xx 이외 응답)
            // 필요 시 에러 처리 로직 추가
            navController.navigate(AppRoutes.connectionCompleteRoute(isVirtualAsset = true))
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500) // 0.5초마다 이미지 변경
            currentImageIndex = (currentImageIndex + 1) % images.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "자산을 연결하고 있어요\n조금만 기다려주세요!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                textAlign = TextAlign.Start,
                lineHeight = 36.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Image(
            painter = painterResource(id = images[currentImageIndex]),
            contentDescription = "Connecting Animation",
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectingVirtualAssetScreenPreview() {
    KOBAC_appTheme {
        ConnectingVirtualAssetScreen(navController = rememberNavController())
    }
}
