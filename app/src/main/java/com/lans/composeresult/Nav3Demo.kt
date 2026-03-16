package com.lans.composeresult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lans.composeresult.core.LocalResultStore
import com.lans.composeresult.core.ResultStoreProvider

// Nav3Demo.kt
sealed class NavDest {
    data object Dashboard : NavDest()
    data object ProfileEditor : NavDest()
}

@Composable
fun Nav3Demo() {
    // Nav 3 的核心：导航即状态
    var backStack by rememberSaveable { mutableStateOf(listOf<NavDest>(NavDest.Dashboard)) }

    ResultStoreProvider {
        // 根据栈顶状态分发页面
        when (val current = backStack.last()) {
            is NavDest.Dashboard -> {
                val store = LocalResultStore.current
                val nickname by store.getResultAsState<String>(tag = "edit_name")

                Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    Text("Nav 3 仪表盘", style = MaterialTheme.typography.headlineMedium)
                    Text("用户昵称: ${nickname ?: "未设置"}")

                    Button(onClick = { backStack = backStack + NavDest.ProfileEditor }) {
                        Text("编辑资料")
                    }
                }
            }

            is NavDest.ProfileEditor -> {
                val store = LocalResultStore.current
                var text by remember { mutableStateOf("") }

                Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("输入新昵称") })

                    Button(onClick = {
                        // 通过 ResultStore 跨页面回传
                        store.setResult(text, tag = "edit_name")
                        backStack = backStack.dropLast(1) // 模拟返回
                    }) {
                        Text("保存并返回")
                    }
                }
            }
        }
    }
}

@Preview(name = "Nav3Demo")
@Composable
private fun PreviewNav3Demo() {
    Nav3Demo()
}