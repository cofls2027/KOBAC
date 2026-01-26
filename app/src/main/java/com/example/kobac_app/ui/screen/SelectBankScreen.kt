package com.example.kobac_app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kobac_app.R
import com.example.kobac_app.ui.AppRoutes
import com.example.kobac_app.ui.components.CustomCheckbox
import com.example.kobac_app.ui.theme.*

data class Asset(val name: String, val logo: Int, val category: String)

@Composable
fun SelectBankScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("은행") }
    val categories = listOf("은행", "카드", "증권", "보험")

    val allAssets = remember {
        listOf(
            // 은행
            Asset("우리은행", R.drawable.wooribank, "은행"),
            Asset("신한은행", R.drawable.shinhanbank, "은행"),
            Asset("토스뱅크", R.drawable.tossbank, "은행"),
            // 카드
            Asset("현대카드", R.drawable.knot, "카드"),
            Asset("농협카드", R.drawable.knot, "카드"),
            Asset("우리카드", R.drawable.knot, "카드"),
            // 증권
            Asset("키움증권", R.drawable.knot, "증권"),
            Asset("한화투자증권", R.drawable.knot, "증권"),
            Asset("SK증권", R.drawable.knot, "증권"),
            // 보험
            Asset("삼성화재", R.drawable.knot, "보험"),
            Asset("DB손해보험", R.drawable.knot, "보험"),
            Asset("현대해상", R.drawable.knot, "보험")
        )
    }
    var selectedAssets by remember { mutableStateOf(setOf<Asset>()) }

    val currentCategoryAssets = allAssets.filter { it.category == selectedCategory }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text("어떤 자산을 연결할까요?", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Black)
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
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    categories.forEach { category ->
                        Text(
                            text = category,
                            fontSize = 18.sp,
                            fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedCategory == category) Black else Gray,
                            modifier = Modifier.clickable { selectedCategory = category }
                        )
                    }
                }
                TextButton(onClick = {
                    val currentCategoryItems = currentCategoryAssets.toSet()
                    if (selectedAssets.containsAll(currentCategoryItems)) {
                        selectedAssets = selectedAssets - currentCategoryItems
                    } else {
                        selectedAssets = selectedAssets + currentCategoryItems
                    }
                }) {
                    Text("전체 선택", color = Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp) // Padding for the button
            ) {
                items(currentCategoryAssets.filter { it.name.contains(searchQuery, ignoreCase = true) }) { asset ->
                    AssetListItem(asset = asset, isChecked = asset in selectedAssets) {
                        selectedAssets = if (it) selectedAssets + asset else selectedAssets - asset
                    }
                }
            }
        }

        if (selectedAssets.isNotEmpty()) {
            Button(
                onClick = { navController.navigate(AppRoutes.CONNECTING) },
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
fun AssetListItem(asset: Asset, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = asset.logo), contentDescription = "${asset.name} Logo", modifier = Modifier.size(36.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(asset.name, fontSize = 16.sp, color = Black, modifier = Modifier.weight(1f))
        CustomCheckbox(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
fun SelectBankScreenPreview() {
    KOBAC_appTheme {
        SelectBankScreen(navController = rememberNavController())
    }
}
