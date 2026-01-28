package com.example.kobac_app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.kobac_app.ui.theme.*
import com.example.kobac_app.data.api.RetrofitClient
import com.example.kobac_app.data.api.ApiService
import com.example.kobac_app.data.model.PortfolioResponse
import com.example.kobac_app.data.model.CryptoAsset
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

// Data classes for financial and virtual assets
data class FinancialAsset(val name: String, val balance: String, val logo: Int)
data class VirtualAsset(val address: String, val balanceKrw: String, val balanceAsset: String, val logo: Int)

@Composable
fun ConnectedAccountScreen(navController: NavController, showVirtualAssets: Boolean) {
    var portfolioData by remember { mutableStateOf<PortfolioResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val apiService = remember { RetrofitClient.createService<ApiService>() }

    LaunchedEffect(Unit) {
        try {
            val response = apiService.getPortfolio()
            portfolioData = response
        } catch (e: Exception) {
            errorMessage = "네트워크 오류가 발생했습니다: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    // 포트폴리오 데이터를 FinancialAsset 리스트로 변환
    val financialAssets = remember(portfolioData) {
        portfolioData?.let { data ->
            buildList {
                // 은행 계좌
                data.bankList.forEach { bank ->
                    add(FinancialAsset(
                        name = bank.bankName,
                        balance = formatAmount(bank.balanceAmt),
                        logo = getBankLogo(bank.bankName)
                    ))
                }
                // 은행 IRP
                data.bankIrpList.forEach { bankIrp ->
                    add(FinancialAsset(
                        name = "${bankIrp.bankName} IRP",
                        balance = formatAmount(bankIrp.balanceAmt),
                        logo = getBankLogo(bankIrp.bankName)
                    ))
                }
                // 증권 계좌
                data.investList.forEach { invest ->
                    add(FinancialAsset(
                        name = invest.companyName,
                        balance = formatAmount(invest.totalEvalAmt),
                        logo = getInvestLogo(invest.companyName)
                    ))
                }
                // 증권 IRP
                data.investIrpList.forEach { investIrp ->
                    add(FinancialAsset(
                        name = "${investIrp.companyName} IRP",
                        balance = formatAmount(investIrp.totalEvalAmt),
                        logo = getInvestLogo(investIrp.companyName)
                    ))
                }
            }
        } ?: emptyList()
    }

    // 하드코딩된 가상자산 데이터 (백엔드 연결 제거)
    val hardcodedCryptoAssets = remember {
        listOf(
            CryptoAsset(
                symbol = "BTC",
                chain = "btc",
                balance = "0.7730799200",
                valueKrw = "100304800.38",
                valueUsd = "77157.54"
            ),
            CryptoAsset(
                symbol = "ETH",
                chain = "eth",
                balance = "945.7081664328",
                valueKrw = "4145038893.48",
                valueUsd = "3188491.46"
            )
        )
    }

    val virtualAssets = remember(portfolioData, showVirtualAssets) {
        if (showVirtualAssets) {
            // 가상자산 연결 완료 시 하드코딩된 데이터 사용
            hardcodedCryptoAssets.map { crypto ->
                val valueKrw = crypto.valueKrw.toDoubleOrNull() ?: 0.0
                VirtualAsset(
                    address = "${crypto.symbol} (${crypto.chain.uppercase()})",
                    balanceKrw = formatAmount(valueKrw),
                    balanceAsset = "${crypto.balance} ${crypto.symbol}",
                    logo = getCryptoLogo(crypto.symbol, crypto.chain)
                )
            }
        } else {
            // 가상자산 연결 전에는 서버 데이터 사용 (없으면 빈 리스트)
            portfolioData?.cryptoList?.map { crypto ->
                val valueKrw = crypto.valueKrw.toDoubleOrNull() ?: 0.0
                VirtualAsset(
                    address = "${crypto.symbol} (${crypto.chain.uppercase()})",
                    balanceKrw = formatAmount(valueKrw),
                    balanceAsset = "${crypto.balance} ${crypto.symbol}",
                    logo = getCryptoLogo(crypto.symbol, crypto.chain)
                )
            } ?: emptyList()
        }
    }

    val financialAssetTotal = portfolioData?.let { data ->
        data.bankList.sumOf { it.balanceAmt.toDoubleOrNull() ?: 0.0 } +
        data.bankIrpList.sumOf { it.balanceAmt.toDoubleOrNull() ?: 0.0 } +
        data.investList.sumOf { it.totalEvalAmt.toDoubleOrNull() ?: 0.0 } +
        data.investIrpList.sumOf { it.totalEvalAmt.toDoubleOrNull() ?: 0.0 }
    } ?: 0.0
    val virtualAssetTotal = remember(portfolioData, showVirtualAssets) {
        if (showVirtualAssets) {
            // 가상자산 연결 완료 시 하드코딩된 데이터의 총합 사용
            hardcodedCryptoAssets.sumOf { it.valueKrw.toDoubleOrNull() ?: 0.0 }
        } else {
            // 가상자산 연결 전에는 서버 데이터 사용
            portfolioData?.cryptoList?.sumOf { it.valueKrw.toDoubleOrNull() ?: 0.0 } ?: 0.0
        }
    }
    val totalNetWorth = financialAssetTotal + virtualAssetTotal

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(errorMessage ?: "오류가 발생했습니다", color = Color.Red)
            }
        }
    } else {
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
                SummarySection(
                    totalNetWorth = totalNetWorth,
                    financialAssetTotal = financialAssetTotal,
                    virtualAssetTotal = virtualAssetTotal
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                Text("금융 자산", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
                Spacer(modifier = Modifier.height(8.dp))
                if (financialAssets.isNotEmpty()) {
                    FinancialAssetList(financialAssets)
                } else {
                    Text("연결된 금융 자산이 없습니다.", color = Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                Text("가상 자산 계좌", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
                Spacer(modifier = Modifier.height(8.dp))
                if (showVirtualAssets && virtualAssets.isNotEmpty()) {
                    VirtualAssetList(virtualAssets)
                } else {
                    VirtualAssetEmptyCard(navController)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

fun formatAmount(amount: String): String {
    val amountDouble = amount.toDoubleOrNull() ?: 0.0
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
    return "${formatter.format(amountDouble.toLong())}원"
}

fun formatAmount(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
    return "${formatter.format(amount.toLong())}원"
}

fun getCryptoLogo(symbol: String, chain: String): Int {
    return when {
        symbol.equals("BTC", ignoreCase = true) || chain.equals("btc", ignoreCase = true) -> R.drawable.btc
        symbol.equals("ETH", ignoreCase = true) || chain.equals("eth", ignoreCase = true) -> R.drawable.eth
        symbol.equals("SOL", ignoreCase = true) || chain.equals("sol", ignoreCase = true) -> R.drawable.sol
        symbol.equals("XRP", ignoreCase = true) || chain.equals("xrp", ignoreCase = true) -> R.drawable.xrp
        else -> R.drawable.btc // 기본값
    }
}

fun getBankLogo(bankName: String): Int {
    return when {
        bankName.contains("우리") -> R.drawable.wooribank
        bankName.contains("신한") -> R.drawable.shinhanbank
        bankName.contains("토스") -> R.drawable.tossbank
        bankName.contains("KB") || bankName.contains("국민") -> R.drawable.kookminbank
        bankName.contains("하나") -> R.drawable.hanabank
        bankName.contains("NH") || bankName.contains("농협") -> R.drawable.nhbank
        else -> R.drawable.wooribank // 기본값
    }
}

fun getInvestLogo(companyName: String): Int {
    return when {
        companyName.contains("키움") -> R.drawable.kiwoom
        else -> R.drawable.kiwoom // 기본값
    }
}

@Composable
fun SummarySection(
    totalNetWorth: Double,
    financialAssetTotal: Double,
    virtualAssetTotal: Double
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        SummaryRow("총 자산", formatAmount(totalNetWorth), amountColor = ButtonBlue, isTotal = true)
        Spacer(modifier = Modifier.height(16.dp))
        SummaryRow("금융 자산", formatAmount(financialAssetTotal), titleColor = Gray, amountWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.height(8.dp))
        SummaryRow("가상자산 계좌", formatAmount(virtualAssetTotal), titleColor = Gray, amountWeight = FontWeight.Normal)
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
                        Text(asset.balanceAsset, fontSize = 12.sp, color = Gray)
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("연결된 가상자산 계좌가 없어요", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Knot과 함께 가상자산을 연결해 보세요!", fontSize = 12.sp, color = Gray)
            }
            Button(
                onClick = { navController.navigate(AppRoutes.SELECT_VIRTUAL_ASSET) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
            ) {
                Text("연결하기", color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectedAccountScreenPreview() {
    KOBAC_appTheme {
        ConnectedAccountScreen(navController = rememberNavController(), showVirtualAssets = true)
    }
}
