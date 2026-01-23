package com.example.kobac_app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kobac_app.ui.AppRoutes
import com.example.kobac_app.ui.theme.*

data class VirtualAsset(val name: String, val address: String, val logo: Int)

@Composable
fun SelectVirtualAssetScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val allAssets = remember {
        listOf(
            VirtualAsset("Tezos", "tz1094...14fnks9e3", android.R.drawable.ic_dialog_info),
            VirtualAsset("Crypto.com", "0xa1df...14fnks9e3", android.R.drawable.ic_dialog_dialer),
            VirtualAsset("Ethereum", "0xa1df...14fnks9e3", android.R.drawable.ic_dialog_email),
            VirtualAsset("Bitcoin", "0xa1df...14fnks9e3", android.R.drawable.ic_dialog_map)
        )
    }
    var selectedAssets by remember { mutableStateOf(setOf<VirtualAsset>()) }

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
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("검색하기", color = Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGray, unfocusedContainerColor = LightGray, disabledContainerColor = LightGray,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Black
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("가상자산", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Black)
                TextButton(onClick = { selectedAssets = allAssets.toSet() }) {
                    Text("전체 선택", color = Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp) // Padding for the button
            ) {
                items(allAssets.filter { it.address.contains(searchQuery, ignoreCase = true) || it.name.contains(searchQuery, ignoreCase = true) }) { asset ->
                    VirtualAssetListItem(asset = asset, isChecked = asset in selectedAssets) {
                        selectedAssets = if (it) selectedAssets + asset else selectedAssets - asset
                    }
                }
            }
        }

        if (selectedAssets.isNotEmpty()) {
            Button(
                onClick = {
                    navController.navigate(AppRoutes.connectedAccountRoute(showAssets = true)) {
                        popUpTo(AppRoutes.HOME)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .height(56.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
            ) {
                Text("${selectedAssets.size}개 연결하기", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun VirtualAssetListItem(asset: VirtualAsset, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = asset.logo), contentDescription = "${asset.name} Logo", modifier = Modifier.size(36.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(asset.address, fontSize = 16.sp, color = Black, modifier = Modifier.weight(1f))
        CustomCheckbox(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun CustomCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .border(2.dp, if (checked) DeepBlue else Color.LightGray, CircleShape)
            .background(if (checked) DeepBlue else Color.Transparent)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(Icons.Default.Check, contentDescription = "Checked", tint = Color.White, modifier = Modifier.size(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectVirtualAssetScreenPreview() {
    KOBAC_appTheme {
        SelectVirtualAssetScreen(navController = rememberNavController())
    }
}
