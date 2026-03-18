package com.lans.composeresult.hilt.simple

import android.util.Log
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lans.composeresult.core.ResultStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val resultStore: ResultStore
) : ViewModel() {

    init {
        // 在 ViewModel 中“监听”全局 ResultStore 的变动
        snapshotFlow { resultStore.allResults().toMap() }
            .filter { resultStore.hasTag<String>("user_name") }
            .onEach {
                // 比如：一旦某个特定的 Key 发生变化，立即上报埋点或同步数据库
                val name = resultStore.getResult<String>("user_name")
                reportLoginEvent(name)
            }
            .launchIn(viewModelScope)
    }

    private fun reportLoginEvent(name: String?) {
        Log.e("TAG", "reportLoginEvent $name ")
    }

}