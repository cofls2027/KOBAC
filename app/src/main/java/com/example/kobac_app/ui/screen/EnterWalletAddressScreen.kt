package com.example.kobac_app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kobac_app.R
import com.example.kobac_app.ui.AppRoutes
import com.example.kobac_app.ui.theme.*

data class WalletAddress(val address: String, val logo: Int)

@Composable
fun EnterWalletAddressScreen(navController: NavController) {
    var addressInput by remember { mutableStateOf("") }
    var walletAddresses by remember { mutableStateOf(listOf<WalletAddress>()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val assetLogos = listOf(R.drawable.btc, R.drawable.eth, R.drawable.sol, R.drawable.xrp) // Placeholder logos

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text("가상자산 지갑 주소를 입력하세요!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Black)
            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = addressInput,
                onValueChange = { addressInput = it },
                placeholder = { Text("검색하기", color = Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGray, unfocusedContainerColor = LightGray, disabledContainerColor = LightGray,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Black
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (addressInput.isNotBlank()) {
                        val logo = assetLogos[walletAddresses.size % assetLogos.size]
                        walletAddresses = walletAddresses + WalletAddress(addressInput, logo)
                        addressInput = ""
                        keyboardController?.hide()
                    }
                })
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("선택된 가상자산", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp) // Padding for the button
            ) {
                items(walletAddresses) { wallet ->
                    WalletAddressListItem(wallet)
                }
            }
        }

        if (walletAddresses.isNotEmpty()) {
            Button(
                onClick = { navController.navigate(AppRoutes.CONNECTING_VIRTUAL_ASSET) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .height(56.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
            ) {
                Text("${walletAddresses.size}개 연결하기", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WalletAddressListItem(wallet: WalletAddress) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = wallet.logo), contentDescription = "Asset Logo", modifier = Modifier.size(36.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(wallet.address, fontSize = 16.sp, color = Black, modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun EnterWalletAddressScreenPreview() {
    KOBAC_appTheme {
        EnterWalletAddressScreen(navController = rememberNavController())
    }
}
