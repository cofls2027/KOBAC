package com.example.kobac_app.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.rememberCoroutineScope
import com.example.kobac_app.R
import com.example.kobac_app.ui.AppRoutes
import com.example.kobac_app.ui.theme.*
import com.example.kobac_app.data.api.RetrofitClient
import com.example.kobac_app.data.api.ApiService
import com.example.kobac_app.data.model.LoginRequest
import com.example.kobac_app.data.util.TokenManager
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val apiService = remember { RetrofitClient.createService<ApiService>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("아이디", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start, color = Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = id, onValueChange = { id = it },
            placeholder = { Text("아이디를 입력하세요", color = Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LightGray, unfocusedContainerColor = LightGray, disabledContainerColor = LightGray,
                focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Black
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("비밀번호", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start, color = Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password, onValueChange = { password = it },
            placeholder = { Text("비밀번호를 입력하세요", color = Gray) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LightGray, unfocusedContainerColor = LightGray, disabledContainerColor = LightGray,
                focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, cursorColor = Black
            )
        )

        TextButton(onClick = { /* TODO */ }, modifier = Modifier.align(Alignment.End)) {
            Text("비밀번호 찾기", color = Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (id.isBlank() || password.isBlank()) {
                    errorMessage = "이메일과 비밀번호를 입력해주세요"
                    return@Button
                }
                
                isLoading = true
                errorMessage = null
                
                scope.launch {
                    try {
                        val response = apiService.login(LoginRequest(id, password))
                        
                        if (response.success && response.data != null) {
                            val loginResponse = response.data
                            TokenManager.saveToken(loginResponse.accessToken, loginResponse.expiresInSec)
                            TokenManager.saveUser(
                                loginResponse.user.userId,
                                loginResponse.user.email,
                                loginResponse.user.name
                            )
                            
                            // 로그인 성공 시 홈 화면으로 이동
                            navController.navigate(AppRoutes.HOME) {
                                popUpTo(AppRoutes.LOGIN) { inclusive = true }
                            }
                        } else {
                            errorMessage = response.error?.message ?: "로그인에 실패했습니다"
                        }
                    } catch (e: java.net.SocketTimeoutException) {
                        errorMessage = "서버 연결 시간이 초과되었습니다. 네트워크 연결을 확인하고 다시 시도해주세요."
                    } catch (e: java.net.UnknownHostException) {
                        errorMessage = "서버에 연결할 수 없습니다. 인터넷 연결을 확인해주세요."
                    } catch (e: java.io.IOException) {
                        errorMessage = "네트워크 오류가 발생했습니다. 연결을 확인하고 다시 시도해주세요."
                    } catch (e: Exception) {
                        errorMessage = "오류가 발생했습니다: ${e.message ?: "알 수 없는 오류"}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("로그인", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        // 에러 메시지 표시
        errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("OR", color = Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(
                onClick = { /* TODO: Facebook Login */ },
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Image(painter = painterResource(id = R.drawable.facebook_logo), contentDescription = "Facebook Login", modifier = Modifier.size(40.dp))
            }
            OutlinedButton(
                onClick = { /* TODO: Google Login */ },
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Image(painter = painterResource(id = R.drawable.google_logo), contentDescription = "Google Login", modifier = Modifier.size(40.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Gray)) { append("계정이 없으신가요? ") }
            pushStringAnnotation(tag = "SignUp", annotation = "SignUp")
            withStyle(style = SpanStyle(color = DeepBlue, fontWeight = FontWeight.Bold)) { append("회원가입하기") }
            pop()
        }

        ClickableText(text = annotatedString, onClick = { /* TODO */ })
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    KOBAC_appTheme {
        LoginScreen(navController = rememberNavController())
    }
}
