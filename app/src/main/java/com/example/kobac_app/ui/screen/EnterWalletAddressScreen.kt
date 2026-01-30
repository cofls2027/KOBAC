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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kobac_app.R
import com.example.kobac_app.ui.AppRoutes
import com.example.kobac_app.ui.theme.*
import com.example.kobac_app.data.api.RetrofitClient
import com.example.kobac_app.data.api.ApiService
import com.example.kobac_app.data.model.BlockchainScanRequest
import com.example.kobac_app.data.model.ScannedAsset
import com.example.kobac_app.data.util.TokenManager
import kotlinx.coroutines.launch

@Composable
fun EnterWalletAddressScreen(navController: NavController) {
    var addressInput by remember { mutableStateOf("") }
    var scannedAssets by remember { mutableStateOf<List<ScannedAsset>>(emptyList()) }
    var assetCount by remember { mutableStateOf(0) }
    var scannedAddress by remember { mutableStateOf("") }
    var scannedChains by remember { mutableStateOf<List<String>>(emptyList()) }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val apiService = remember { RetrofitClient.createService<ApiService>() }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                "가상자산 지갑 주소를 입력하세요!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = addressInput,
                onValueChange = { addressInput = it },
                placeholder = { Text("지갑 주소를 입력하세요", color = Gray) },
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
                        scannedAddress = addressInput
                        scope.launch {
                            try {
                                val request = BlockchainScanRequest(address = addressInput)
                                val response = apiService.blockchainScan(request)
                                scannedAssets = response.assets
                                assetCount = response.assetCount
                                scannedChains = response.chains
                            } catch (e: Exception) {
                                // Handle error
                            }
                        }
                    }
                    focusManager.clearFocus()
                })
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("선택된 가상자산", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp)
            ) {
                items(scannedAssets) { asset ->
                    WalletAddressListItem(asset)
                }
            }
        }

        if (scannedAssets.isNotEmpty()) {
            Button(
                onClick = {
                    val addressesToSave = scannedChains.associateWith { scannedAddress }.toMutableMap()
                    addressesToSave["auto"] = scannedAddress
                    TokenManager.saveCryptoAddresses(addressesToSave)
                    navController.navigate(AppRoutes.CONNECTING_VIRTUAL_ASSET)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 20.dp)
                    .height(56.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
            ) {
                Text("${assetCount}개 연결하기", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WalletAddressListItem(asset: ScannedAsset) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = getCryptoLogo(asset.symbol, asset.chain)), contentDescription = "Asset Logo", modifier = Modifier.size(36.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(asset.symbol, fontSize = 16.sp, color = Black, modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Text("${asset.valueKrw}원", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Black)
            Text("${asset.balance} ${asset.symbol}", fontSize = 12.sp, color = Gray)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EnterWalletAddressScreenPreview() {
    KOBAC_appTheme {
        EnterWalletAddressScreen(navController = rememberNavController())
    }
}
