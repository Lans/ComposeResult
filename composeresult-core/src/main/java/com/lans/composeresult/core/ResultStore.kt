package com.lans.composeresult.core


import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember

/**
 * ResultStore 是跨页面通信的核心存储器。
 * 支持响应式观察、进程死亡恢复以及 DI 单例注入。
 */
class ResultStore(
    initialData: Map<String, Any?> = emptyMap()
) {
    // 使用快照状态 Map，确保任何增删改都能触发 Composable 重组
    @PublishedApi
    internal val _resultStateMap = mutableStateMapOf<String, Any?>().apply {
        putAll(initialData)
    }

    /**
     * 设置结果
     * @param result 要保存的数据
     * @param tag 可选标签，用于区分同类型的不同业务
     */
    inline fun <reified T> setResult(result: T, tag: String = "") {
        val key = makeKey<T>(tag)
        _resultStateMap[key] = result
    }

    /**
     * 以 State 形式获取结果，支持 Compose 响应式自动刷新
     */
    @Composable
    inline fun <reified T> getResultAsState(tag: String = ""): State<T?> {
        val key = makeKey<T>(tag)
        // 使用 derivedStateOf 监听 Map 的变动
        return remember(key) {
            derivedStateOf { _resultStateMap[key] as? T }
        }
    }

    /**
     * 消费结果：获取数据并立即从存储中移除（常用于一次性通知）
     */
    inline fun <reified T> consumeResult(tag: String = ""): T? {
        val key = makeKey<T>(tag)
        return (_resultStateMap.remove(key) as? T)
    }

    /**
     * 移除特定结果
     */
    inline fun <reified T> removeResult(tag: String = "") {
        _resultStateMap.remove(makeKey<T>(tag))
    }

    /**
     * 清空所有数据
     */
    fun clear() = _resultStateMap.clear()

    /**
     * 导出当前快照（用于保存状态）
     */
    fun dump(): Map<String, Any?> = _resultStateMap.toMap()

    @PublishedApi
    internal inline fun <reified T> makeKey(tag: String) = "${T::class.java.name}_$tag"
}