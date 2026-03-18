package com.lans.composeresult.core

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver

/**
 * 专为 ResultStore 设计的恢复器
 */
internal fun ResultStoreSaver(): Saver<ResultStore, *> = Saver<ResultStore, Map<String, Any?>>(
    save = { it.dump() },
    restore = { savedMap -> ResultStore(initialData = savedMap) }
)