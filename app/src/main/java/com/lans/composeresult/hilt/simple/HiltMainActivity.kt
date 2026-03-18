package com.lans.composeresult.hilt.simple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lans.composeresult.core.ResultStore
import com.lans.composeresult.core.ResultStoreProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HiltMainActivity : ComponentActivity() {

    @Inject
    lateinit var globalStore: ResultStore // 注入单例

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ResultStoreProvider(store = globalStore) {
                HomeScreen()
            }
        }
    }
}