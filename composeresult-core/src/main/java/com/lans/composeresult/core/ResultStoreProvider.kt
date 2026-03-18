package com.lans.composeresult.core

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * CompositionLocal 定义
 */
val LocalResultStore = staticCompositionLocalOf<ResultStore> {
    error("未检测到 ResultStoreProvider。请在根节点包裹 ResultStoreProvider { ... }")
}

/**
 * 结果存储提供者
 * @param store 可选参数。如果使用 Hilt/Koin，请传入注入的单例；如果不传，则自动创建局部 Store。
 */
@Composable
fun ResultStoreProvider(
    store: ResultStore? = null,
    content: @Composable () -> Unit
) {
    // 逻辑：
    // 1. 如果外部提供了 store (单例)，则直接使用，不开启内部 saveable (由单例自行处理或生命周期管理)
    // 2. 如果外部未提供，则创建一个基于 rememberSaveable 的自动恢复实例
    val finalStore = if (store != null) {
        // 模式 A：外部注入（Hilt/Koin），生命周期随单例，不参与 Bundle 序列化
        store
    } else {
        rememberSaveable(
            saver = Saver(save = { it.dump() }, restore = { ResultStore(it) })
        ) { ResultStore() }
    }
    CompositionLocalProvider(LocalResultStore provides finalStore) {
        content()
    }
}

/**
 * 快捷获取当前 ResultStore 的扩展
 */
@Composable
fun rememberResultStore() = LocalResultStore.current