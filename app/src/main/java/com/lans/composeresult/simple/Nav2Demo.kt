package com.lans.composeresult.simple

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lans.composeresult.core.LocalResultStore
import com.lans.composeresult.core.ResultStoreProvider

@Composable
fun Nav2Demo() {
    val navController = rememberNavController()

    ResultStoreProvider { // 1. 提供全局存储
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                val store = LocalResultStore.current
                // 2. 观察来自 Selector 的结果
                val selectedColor: Int? by store.getResultAsState<Int?>(tag = "color_picker")

                Column(
                    Modifier
                        .fillMaxSize()
                        .background(selectedColor?.let { Color(it) } ?: Color.White),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Text("Nav 2.x 首页", style = MaterialTheme.typography.headlineMedium)
                    Button(onClick = { navController.navigate("selector") }) {
                        Text("去选择背景颜色")
                    }
                }
            }

            composable("selector") {
                val store = LocalResultStore.current

                Column(
                    Modifier.fillMaxSize(),
                    Arrangement.SpaceEvenly,
                    Alignment.CenterHorizontally
                ) {
                    Text("选择颜色")
                    listOf(0xFFFFCDD2, 0xFFC8E6C9, 0xFFBBDEFB).forEach { color ->
                        Button(onClick = {
                            // 3. 设置结果并返回
                            store.setResult(color.toInt(), tag = "color_picker")
                            navController.popBackStack()
                        }, colors = ButtonDefaults.buttonColors(containerColor = Color(color))) {
                            Text("应用此颜色")
                        }
                    }
                }
            }
        }
    }
}