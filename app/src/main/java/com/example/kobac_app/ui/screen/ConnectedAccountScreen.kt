package com.example.kobac_app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.kobac_app.ui.theme.*

// Data classes for financial and virtual assets
data class FinancialAsset(val name: String, val balance: String, val logo: Int)
data class VirtualAsset(val address: String, val balanceKrw: String, val balanceAsset: String, val logo: Int)

@Composable
fun ConnectedAccountScreen(navController: NavController, showVirtualAssets: Boolean) {
    // Sample data based on the new layout
    val financialAssets = listOf(
        FinancialAsset("우리은행", "3,000,000원", R.drawable.wooribank),
        FinancialAsset("키움증권", "3,000,000원", R.drawable.knot), // Placeholder logo
        FinancialAsset("토스뱅크", "3,000,000원", R.drawable.tossbank)
    )
    val virtualAssets = listOf(
        VirtualAsset("0x123...1334", "3,000,000원", "0.13 BTC", R.drawable.knot), // Placeholder logo
        VirtualAsset("0x123...1334", "3,000,000원", "0.13 ETH", R.drawable.knot), // Placeholder logo
        VirtualAsset("0x123...1334", "3,000,000원", "0.13 SOL", R.drawable.knot), // Placeholder logo
        VirtualAsset("0x123...1334", "3,000,000원", "0.13 XRP", R.drawable.knot)  // Placeholder logo
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        item { Spacer(modifier = Modifier.height(60.dp)) }
        item {
            Text("Knot, 새로운 금융을 한번에", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Black)
            Spacer(modifier = Modifier.height(32.dp))
        }
        item {
            SummarySection()
            Spacer(modifier = Modifier.height(32.dp))
        }
        item {
            Text("금융 계좌", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
            Spacer(modifier = Modifier.height(8.dp))
            FinancialAssetList(financialAssets)
            Spacer(modifier = Modifier.height(32.dp))
        }
        item {
            Text("가상 자산 계좌", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
            Spacer(modifier = Modifier.height(8.dp))
            if (showVirtualAssets) {
                VirtualAssetList(virtualAssets)
            } else {
                VirtualAssetEmptyCard(navController)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SummarySection() {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        SummaryRow("총 자산", "3,000,000원", amountColor = ButtonBlue, isTotal = true)
        Spacer(modifier = Modifier.height(16.dp))
        SummaryRow("금융 계좌", "3,000,000원", titleColor = Gray, amountWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.height(8.dp))
        SummaryRow("가상자산 계좌", "3,000,000원", titleColor = Gray, amountWeight = FontWeight.Normal)
    }
}

@Composable
fun SummaryRow(
    title: String,
    amount: String,
    titleColor: Color = Black,
    amountColor: Color = Black,
    isTotal: Boolean = false,
    amountWeight: FontWeight = FontWeight.Bold
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = if (isTotal) 18.sp else 16.sp, color = titleColor, modifier = Modifier.weight(1f))
        Text(amount, fontSize = if (isTotal) 20.sp else 18.sp, fontWeight = amountWeight, color = amountColor)
    }
}

@Composable
fun FinancialAssetList(assets: List<FinancialAsset>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightGray)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            assets.forEachIndexed { index, asset ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = asset.logo),
                        contentDescription = "${asset.name} Logo",
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(asset.name, fontSize = 16.sp, color = Black, modifier = Modifier.weight(1f))
                    Text(asset.balance, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Black)
                }
                if (index < assets.lastIndex) {
                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun VirtualAssetList(assets: List<VirtualAsset>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightGray)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            assets.forEachIndexed { index, asset ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = asset.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(asset.address, fontSize = 16.sp, color = Black, modifier = Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(asset.balanceKrw, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Black)
                        Text(asset.balanceAsset, fontSize = 14.sp, color = Gray)
                    }
                }
                if (index < assets.lastIndex) {
                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun VirtualAssetEmptyCard(navController: NavController) {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LightGray)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
            ) {
                Text("아직 연결된 계좌 정보가 없습니다.", color = Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Knot을 통해 가상자산 정보까지 한번에 확인하세요!", color = Gray, fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(AppRoutes.SELECT_VIRTUAL_ASSET) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
        ) {
            Text("가상자산 연결하기", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, name = "Final State - With Virtual Assets")
@Composable
fun ConnectedAccountScreenWithAssetsPreview() {
    KOBAC_appTheme {
        ConnectedAccountScreen(navController = rememberNavController(), showVirtualAssets = true)
    }
}

@Preview(showBackground = true, name = "Initial State - No Virtual Assets")
@Composable
fun ConnectedAccountScreenPreview() {
    KOBAC_appTheme {
        ConnectedAccountScreen(navController = rememberNavController(), showVirtualAssets = false)
    }
}
