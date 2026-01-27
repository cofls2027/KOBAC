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
import kotlinx.coroutines.delay

@Composable
fun ConnectingScreen(navController: NavController) {
    val images = remember {
        listOf(
            R.drawable.wooribank,
            R.drawable.shinhanbank,
            R.drawable.tossbank,
            R.drawable.hdcard,
            R.drawable.nhcard,
            R.drawable.wooricard
        )
    }
    var currentImageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(2000) // 2초 딜레이
        navController.navigate(AppRoutes.connectionCompleteRoute(isVirtualAsset = false))
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
fun ConnectingScreenPreview() {
    KOBAC_appTheme {
        ConnectingScreen(navController = rememberNavController())
    }
}
