package com.example.kobac_app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kobac_app.R
import com.example.kobac_app.ui.AppRoutes
import com.example.kobac_app.ui.theme.*

@Composable
fun MyDataConsentScreen(navController: NavController) {
    var isChecked by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        val annotatedTitle = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("자산을 연결하려면\n")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("~~님의 동의가 필요해요")
            }
        }

        Text(
            text = annotatedTitle,
            color = Black,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "자산을 연결하기 전 신중하게 고민하여 필요한 서비스만 연결해주세요.\n사용하지 않는 서비스는 언제든지 삭제할 수 있습니다.",
            color = Gray,
            fontSize = 11.sp,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.knot),
            contentDescription = "Knot Logo",
            modifier = Modifier.size(170.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isChecked = !isChecked }
                .border(
                    width = 1.dp,
                    color = if (isChecked) ButtonBlue else Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            color = LightGray,
            contentColor = Black
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Checked",
                    tint = if (isChecked) ButtonBlue else Gray
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("필수 동의", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("마이데이터 서비스 이용약관", color = Gray, fontSize = 14.sp)
                }
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Expand", tint = Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(AppRoutes.SELECT_BANK) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
            enabled = isChecked
        ) {
            Text("다음으로", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun MyDataConsentScreenPreview() {
    KOBAC_appTheme {
        MyDataConsentScreen(navController = rememberNavController())
    }
}
