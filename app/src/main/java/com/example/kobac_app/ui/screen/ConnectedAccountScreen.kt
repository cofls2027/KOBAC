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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kobac_app.R
import com.example.kobac_app.ui.AppRoutes
import com.example.kobac_app.ui.theme.*

data class Account(val bankName: String, val balance: String, val logo: Int)
data class ConnectedVirtualAsset(val address: String, val amount: String, val value: String, val logo: Int)

@Composable
fun ConnectedAccountScreen(navController: NavController, showVirtualAssets: Boolean) {
    val bankAccounts = listOf(
        Account("우리은행", "3,000,000원", R.drawable.wooribank),
        Account("우리은행", "3,000,000원", R.drawable.wooribank),
        Account("우리은행", "3,000,000원", R.drawable.wooribank),
        Account("우리은행", "3,000,000원", R.drawable.wooribank)
    )
    val virtualAssets = listOf(
        ConnectedVirtualAsset("tz1095...14fh14oin", "3,000,000 XTZ", "1,200,000원", R.drawable.xtz),
        ConnectedVirtualAsset("tz1095...14fh14oin", "3,000,000 XTZ", "1,200,000원", R.drawable.xtz),
        ConnectedVirtualAsset("tz1095...14fh14oin", "3,000,000 XTZ", "1,200,000원", R.drawable.xtz)
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
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            SummaryCard()
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Text("은행 계좌", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
            Spacer(modifier = Modifier.height(8.dp))
            BankAccountList(bankAccounts)
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Text("가상 자산 계좌", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
            Spacer(modifier = Modifier.height(8.dp))
            if (showVirtualAssets) {
                VirtualAssetAccountList(virtualAssets)
            } else {
                VirtualAssetEmptyCard(navController)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SummaryCard() {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = LightGray)) {
        Column(modifier = Modifier.padding(20.dp)) {
            SummaryRow("총 잔액", "3,000,000원", isTotal = true)
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
            SummaryRow("은행 잔액", "3,000,000원")
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow("가상자산 잔액", "3,000,000원")
        }
    }
}

@Composable
fun SummaryRow(title: String, amount: String, isTotal: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 16.sp, color = if (isTotal) Black else Gray, modifier = Modifier.weight(1f))
        Text(amount, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
    }
}

@Composable
fun BankAccountList(accounts: List<Account>) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = LightGray)) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            accounts.forEach { account ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = account.logo), contentDescription = "${account.bankName} Logo", modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(account.bankName, fontSize = 16.sp, color = Black, modifier = Modifier.weight(1f))
                    Text(account.balance, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Black)
                }
            }
        }
    }
}

@Composable
fun VirtualAssetAccountList(assets: List<ConnectedVirtualAsset>) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = LightGray)) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            assets.forEach { asset ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = asset.logo), contentDescription = null, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(asset.address, fontSize = 16.sp, color = Black, modifier = Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(asset.amount, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Black)
                        Text(asset.value, fontSize = 14.sp, color = Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun VirtualAssetEmptyCard(navController: NavController) {
    Column {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = LightGray)) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("아직 연결된 계좌 정보가 없습니다.", color = Gray, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(4.dp))
                Text("Knot을 통해 가상자산 정보까지 한번에 확인하세요!", color = Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(AppRoutes.SELECT_VIRTUAL_ASSET) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
        ) {
            Text("가상자산 연결하기", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, name = "Initial State - No Virtual Assets")
@Composable
fun ConnectedAccountScreenPreview() {
    KOBAC_appTheme {
        ConnectedAccountScreen(navController = rememberNavController(), showVirtualAssets = false)
    }
}

@Preview(showBackground = true, name = "Final State - With Virtual Assets")
@Composable
fun ConnectedAccountScreenWithAssetsPreview() {
    KOBAC_appTheme {
        ConnectedAccountScreen(navController = rememberNavController(), showVirtualAssets = true)
    }
}
