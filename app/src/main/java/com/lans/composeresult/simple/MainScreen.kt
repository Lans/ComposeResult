package com.lans.composeresult.simple

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lans.composeresult.core.ResultStoreProvider


enum class DemoType { Nav2, Nav3 }

@Composable
fun MainScreen() {
    // 使用枚举让状态更清晰
    var currentDemo by remember { mutableStateOf(DemoType.Nav2) }

    // 重点：ResultStoreProvider 包裹在最外层，
    // 这样无论你在 Nav2 还是 Nav3 中设置数据，在切换 Tab 时都能保持状态。
    ResultStoreProvider {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selectedType = currentDemo,
                    onSelect = { currentDemo = it }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // 顶部状态栏
                DemoHeader(currentDemo)

                Box(modifier = Modifier.weight(1f)) {
                    when (currentDemo) {
                        DemoType.Nav2 -> Nav2Demo()
                        DemoType.Nav3 -> Nav3Demo()
                    }
                }
            }
        }
    }
}

@Composable
fun DemoHeader(type: DemoType) {
    val title = if (type == DemoType.Nav2) "Navigation 2.x 演示" else "Navigation 3 (状态驱动) 演示"
    val color = if (type == DemoType.Nav2) Color(0xFF6200EE) else Color(0xFF03DAC5)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, color = color)
        Text(text = "通过 ResultStore 实现跨页面通信", style = MaterialTheme.typography.bodySmall)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun BottomNavigationBar(selectedType: DemoType, onSelect: (DemoType) -> Unit) {
    Surface(tonalElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onSelect(DemoType.Nav2) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedType == DemoType.Nav2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(text = "Nav 2.x")
            }

            Button(
                onClick = { onSelect(DemoType.Nav3) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedType == DemoType.Nav3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(text = "Nav 3.0")
            }
        }
    }
}