plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
    id("com.vanniktech.maven.publish") version "0.36.0"
    signing
}

android {
    namespace = "com.lans.composeresult.core"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
//
//    发布到 maven { url = uri("https://jitpack.io") }
//
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }


}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.saveable)
}

// 发布到 maven { url = uri("https://jitpack.io") }
afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                // 关键点：从 Android 的 release 组件获取产物
                from(components["release"])

                groupId = "io.github.lans"
                artifactId = "composeResult"
                version = "1.0.2"
            }
        }
    }
}

// 发布到 mavenCentral()
//mavenPublishing {
//    coordinates("io.github.lans", "composeResult", "1.0.1")
//
//    pom {
//        name = "ComposeResult"
//        description = "ComposeResult 是一个专为 Jetpack Compose 打造的轻量级、响应式页面通信库。它利用 CompositionLocal 和 rememberSaveable 实现了跨页面的状态共享与结果回传，优雅地取代了繁琐的 SavedStateHandle 方案。"
//        url = "https://github.com/Lans/ComposeResult"
//
//        // 开源协议配置，可根据需要修改
//        licenses {
//            license {
//                name = "The Apache License, Version 2.0"
//                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
//            }
//        }
//
//        developers {
//            developer {
//                id = "lans"
//                name = "lans"
//                email = "wylans@163.com"
//            }
//        }
//
//        scm {
//            connection = "scm:git:github.com/Lans/ComposeResult.git"
//            developerConnection = "scm:git:ssh://github.com/Lans/ComposeResult.git"
//            url = "https://github.com/Lans/ComposeResult"
//        }
//    }
//
//    publishToMavenCentral()
//    signAllPublications()
//}
