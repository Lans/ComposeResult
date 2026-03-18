package com.lans.composeresult.hilt.simple

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lans.composeresult.core.LocalResultStore

@Composable
fun HomeScreen() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val viewModel: HomeViewModel = hiltViewModel()

            val store = LocalResultStore.current
            // 监听 tag 为 "user_name" 的 String 类型数据
            // 一旦 ViewModel 调用 setResult，这里会自动触发重组刷新 UI
            val name by store.getResultAsState<String>(tag = "user_name")

            Column(
                Modifier
                    .fillMaxSize(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Text(text = "当前用户: ${name ?: "未登录"}")

                // 按钮点击可以跳转到其他页面或触发 ViewModel 逻辑
                Button(onClick = {
                    navController.navigate("login")
                }) {
                    Text("去修改昵称/登录")
                }
            }
        }

        composable("login") {
            val viewModel: LoginViewModel = hiltViewModel()
            var input by remember { mutableStateOf("") }
            Column(
                Modifier
                    .fillMaxSize(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {

                Text(text = "请输入昵称：")
                TextField(
                    value = input, onValueChange = {
                        input = it
                    })
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    viewModel.performLogin(input) {
                        navController.popBackStack() // 登录成功直接返回
                    }
                }) {
                    Text("登录")
                }
            }
        }

    }

}

@Preview(name = "HomeScreen")
@Composable
private fun PreviewHomeScreen() {
    HomeScreen()
}