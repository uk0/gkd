plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

//val composeVersion = "1.3.3"
@Suppress("UnstableApiUsage")
android {
    compileSdk = 33
    buildToolsVersion = "33.0.0"

    defaultConfig {
        applicationId = "li.songe.gkd"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        kapt {
            arguments {
//                room 依赖每次构建的产物来执行自动迁移
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("./android.jks")
            storePassword = "KdMQ6pqiNSJ6Sype"
            keyAlias = "key0"
            keyPassword = "KdMQ6pqiNSJ6Sype"
        }
    }

    kotlin {
        sourceSets.debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        sourceSets.release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }

    buildTypes {
        release {
            manifestPlaceholders += mapOf()
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["appName"] = "搞快点"
        }
        debug {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["appName"] = "搞快点-dev"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

//        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
//        忽略 compose 对 kotlin 版本的限制
//        freeCompilerArgs = freeCompilerArgs + listOf(
//            "-P",
//            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
//        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
//        compose 编译器的版本, 需要注意它与 compose 的版本不一致
//        https://mvnrepository.com/artifact/androidx.compose.compiler/compiler
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packagingOptions {
        resources {
            // Due to https://github.com/Kotlin/kotlinx.coroutines/issues/2023
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/licenses/*"
            excludes += "**/attach_hotspot_windows.dll"
            excludes += "META-INF/io.netty.*"
        }
    }
    configurations.all {
        resolutionStrategy{
            //    https://github.com/Kotlin/kotlinx.coroutines/issues/2023
            exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-debug")
        }
    }
}

dependencies {

//    normal
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")

//    ktx
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

//    compose
    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.material:material:1.4.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.0")
    implementation("androidx.activity:activity-compose:1.7.0")

//    test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//    https://github.com/RikkaApps/Shizuku-API
//    val shizukuVersion = "12.1.0"
    implementation("dev.rikka.shizuku:api:12.1.0")
    implementation("dev.rikka.shizuku:provider:12.1.0")

    //    工具集合类
    //    https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/README-CN.md
    implementation("com.blankj:utilcodex:1.31.0")

//    https://developer.android.com/jetpack/compose/navigation
//    implementation("androidx.navigation:navigation-compose:2.4.2")
//    2.4.0/2.4.1/2.4.2 在 红米k40 android 12 需要 向左滑动 才会渲染, 在 android studio 自带 preview 也不渲染
//    implementation("com.google.accompanist:accompanist-navigation-animation:0.23.1")

//    https://bugly.qq.com/docs/user-guide/instruction-manual-android/
    implementation("com.tencent.bugly:crashreport:4.0.4")

//    https://developer.android.google.cn/training/data-storage/room?hl=zh-cn
//    val roomVersion = "2.4.3"
    implementation("androidx.room:room-runtime:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")

//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.google.code.gson:gson:2.8.9")

//    https://github.com/Tencent/MMKV/blob/master/README_CN.md
    implementation("com.tencent:mmkv:1.2.13")

//    ktor
    implementation("io.ktor:ktor-server-core:2.2.3")
    implementation("io.ktor:ktor-server-netty:2.2.3")
//    https://ktor.io/docs/cors.html#install_plugin
    implementation("io.ktor:ktor-server-cors:2.2.3")
    implementation("io.ktor:ktor-server-content-negotiation:2.2.3")

//  请注意,当 client 和 server 版本不一致时, 会报错 socket hang up
    implementation("io.ktor:ktor-client-core:2.2.3")
    implementation("io.ktor:ktor-client-cio:2.2.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.3")

//    https://github.com/Kotlin/kotlinx.serialization/blob/master/docs
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

//    https://dylancaicoding.github.io/ActivityResultLauncher/#/
    implementation("com.github.DylanCaiCoding:ActivityResultLauncher:1.1.2")

//    https://github.com/journeyapps/zxing-android-embedded
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")


//    implementation("androidx.startup:startup-runtime:1.1.1")

    implementation("com.google.accompanist:accompanist-drawablepainter:0.23.1")
    implementation("com.google.accompanist:accompanist-placeholder-material:0.23.1")


    ksp(project(":room_processor"))
    implementation(project(mapOf("path" to ":selector")))
    implementation(project(mapOf("path" to ":router")))

//    https://github.com/falkreon/Jankson
    implementation("blue.endless:jankson:1.2.1")

//    https://github.com/Kotlin/kotlinx.collections.immutable
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

    implementation("io.github.torrydo:floating-bubble-view:0.5.2")

}