package com.lans.composeresult.core

package com.lans.composeresult.core

import android.os.Bundle
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * 将 ResultStore 与 ViewModel 的 SavedStateHandle 绑定。
 * 绑定后，ResultStore 中的任何变更都会自动持久化到 SavedStateHandle，
 * 从而支持 App 进程被系统杀死后的数据恢复。
 *
 * @param handle ViewModel 中的 SavedStateHandle
 * @param scope 用于监听数据变化的协程作用域，建议传入 viewModelScope
 * @param key 在 SavedStateHandle 中存储的键名
 */
fun ResultStore.bindTo(
    handle: SavedStateHandle,
    scope: CoroutineScope,
    key: String = "compose_result_bundle"
) {
    // 1. 【恢复逻辑】：从 handle 中取出 Bundle 并还原到 ResultStore
    handle.get<Bundle>(key)?.let { bundle ->
        // 将 Bundle 转换为 Map
        val recoveredMap = bundle.keySet().associateWith { bundle.get(it) }
        recoveredMap.forEach { (k, v) ->
            if (v != null) {
                // 直接写入内部存储，不走公共 setResult 以避免触发不必要的 Key 生成逻辑
                _resultStateMap[k] = v
            }
        }
    }

    // 2. 【持久化逻辑】：监听 Map 的变化并同步到 SavedStateHandle
    // snapshotFlow 会在 MutableStateMap 发生任何增删改时发出新值
    snapshotFlow { _resultStateMap.toMap() }
        .onEach { map ->
            val bundle = Bundle()
            map.forEach { (k, v) ->
                // 注意：存入 Bundle 的对象必须支持序列化 (Parcelable/Serializable)
                when (v) {
                    is android.os.Parcelable -> bundle.putParcelable(k, v)
                    is java.io.Serializable -> bundle.putSerializable(k, v)
                    else -> {
                        // 处理基础类型或抛出警告
                        bundle.putReference(k, v)
                    }
                }
            }
            handle[key] = bundle
        }
        .launchIn(scope)
}

/**
 * 内部辅助函数，处理基础类型放入 Bundle
 */
private fun Bundle.putReference(key: String, value: Any?) {
    when (value) {
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Boolean -> putBoolean(key, value)
        is Double -> putDouble(key, value)
        is Float -> putFloat(key, value)
        is Long -> putLong(key, value)
    }
}