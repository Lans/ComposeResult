package com.lans.composeresult.core

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

class ResultStore {
    val resultStateMap = mutableStateMapOf<String, MutableState<Any?>>()

    inline fun <reified T> setResult(result: T, tag: String = "") {
        val key = "${T::class.java.name}_$tag"
        if (resultStateMap.containsKey(key)) {
            resultStateMap[key]?.value = result
        } else {
            resultStateMap[key] = mutableStateOf(result)
        }
    }

    @Composable
    inline fun <reified T> getResultAsState(tag: String = ""): State<T?> {
        val key = "${T::class.java.name}_$tag"
        return remember(key, resultStateMap[key]) {
            derivedStateOf { resultStateMap[key]?.value as? T }
        }
    }

    inline fun <reified T> consumeResult(tag: String = ""): T? {
        val key = "${T::class.java.name}_$tag"
        val value = resultStateMap[key]?.value as? T
        if (value != null) resultStateMap.remove(key)
        return value
    }
}

internal fun ResultStoreSaver(): Saver<ResultStore, *> = Saver(
    save = { it.resultStateMap.mapValues { entry -> entry.value.value } },
    restore = { savedMap ->
        ResultStore().apply {
            savedMap.forEach { (key, value) ->
                resultStateMap[key] = mutableStateOf(value)
            }
        }
    }
)

val LocalResultStore = staticCompositionLocalOf<ResultStore> {
    error("No ResultStore provided")
}

@Composable
fun ResultStoreProvider(content: @Composable () -> Unit) {
    val store = rememberSaveable(saver = ResultStoreSaver()) { ResultStore() }
    CompositionLocalProvider(LocalResultStore provides store, content = content)
}