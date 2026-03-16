

# ComposeResult 🚀

**ComposeResult** 是一个专为 Jetpack Compose 打造的轻量级、响应式页面通信库。它利用 `CompositionLocal` 和 `rememberSaveable` 实现了跨页面的状态共享与结果回传，优雅地取代了繁琐的 `SavedStateHandle` 方案。

---

## 🌟 核心优势

* **极简集成**：无需配置 ViewModel，无需手动管理 NavBackStackEntry。
* **响应式 UI**：基于 Compose State，数据一旦设置，所有订阅页自动重组。
* **可靠的持久化**：原生支持 **Process Death（进程重启）**，数据自动通过 Bundle 恢复。
* **全架构支持**：适配 Navigation 2.x、Navigation 3.0 以及所有自定义导航逻辑。

---

## 📦 安装指南

### 1. 添加仓库 (settings.gradle.kts)

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

```

### 2. 引入依赖 (build.gradle.kts)

```kotlin
dependencies {
    implementation("com.github.Lans:ComposeResult:1.0.1")
}

```

---

## 🛠 技术原理

`ComposeResult` 在 Composition 树的根部构建了一个持久化的数据中心。

1. **Provider**: `ResultStoreProvider` 初始化存储空间。
2. **Storage**: 内部使用 `MutableStateMap` 确保响应式，并绑定 `Saver` 实现数据持久化。
3. **Saveable**: 数据会自动挂载到 Android 系统的 `SaveableStateRegistry`，确保在系统回收资源后数据不丢失。

---

## 💾 支持的数据类型

由于底层依赖 Bundle 机制，回传的数据 `T` 必须符合以下要求：

| 类型分类 | 具体支持 | 建议 |
| --- | --- | --- |
| **基础类型** | `Int`, `String`, `Boolean`, `Long`, `Float` 等 | 直接使用 |
| **自定义对象** | 实现 `Parcelable` 或 `Serializable` 接口 | 推荐使用 `@Parcelize` |
| **集合/数组** | `IntArray`, `ArrayList<T>` 等 | 内部元素也需支持序列化 |

**代码示例：回传自定义 Model**

```kotlin
@Parcelize
data class UserInfo(val name: String, val age: Int) : Parcelable

// 发送数据
store.setResult(UserInfo("Lans", 18), tag = "user_info")

// 观察数据
val info by store.getResultAsState<UserInfo>(tag = "user_info")

```

---

## 📖 实战演示

### 1. Navigation 2.x (传统路由导航)

模拟从选择器页面返回数据并自动刷新首页背景。

```kotlin
@Composable
fun Nav2Demo() {
    val navController = rememberNavController()

    ResultStoreProvider { 
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                val store = LocalResultStore.current
                // 观察结果：selectedColor 变化时，UI 自动刷新
                val selectedColor by store.getResultAsState<Int?>(tag = "color_picker")

                Column(
                    Modifier.fillMaxSize().background(selectedColor?.let { Color(it) } ?: Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Nav 2.x 首页", style = MaterialTheme.typography.headlineMedium)
                    Button(onClick = { navController.navigate("selector") }) {
                        Text("去选择背景颜色")
                    }
                }
            }

            composable("selector") {
                val store = LocalResultStore.current
                Button(onClick = {
                    // 设置结果并返回，home 页将自动感知
                    store.setResult(0xFFBBDEFB.toInt(), tag = "color_picker")
                    navController.popBackStack()
                }) {
                    Text("应用蓝色")
                }
            }
        }
    }
}

```

### 2. Navigation 3 (声明式状态导航)

在 Navigation 3 等完全由状态驱动的导航架构下，数据解耦更加彻底。

```kotlin
@Composable
fun Nav3Demo() {
    // 导航栈状态
    var backStack by rememberSaveable { mutableStateOf(listOf<NavDest>(NavDest.Dashboard)) }

    ResultStoreProvider {
        when (val current = backStack.last()) {
            is NavDest.Dashboard -> {
                val name by LocalResultStore.current.getResultAsState<String>(tag = "edit_name")
                Text("用户昵称: ${name ?: "未设置"}")
                Button(onClick = { backStack = backStack + NavDest.ProfileEditor }) { Text("编辑") }
            }
            is NavDest.ProfileEditor -> {
                val store = LocalResultStore.current
                Button(onClick = {
                    store.setResult("Lans", tag = "edit_name")
                    backStack = backStack.dropLast(1) 
                }) { Text("保存返回") }
            }
        }
    }
}

```

---

## ⚠️ 注意事项

1. **Tag 唯一性**：请确保不同业务逻辑使用不同的 `tag`，建议定义常量管理。
2. **数据大小**：避免在 Result 中传递过大的 Bitmap 或海量列表，建议只回传数据 ID。
3. **生命周期**：`ResultStoreProvider` 的位置决定了数据的存活范围。通常建议包裹在 `Activity` 的 `setContent` 内部最外层。

---

## 📄 License

```text
Copyright 2026 Lans

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

```
