plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.aboutlibraries)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

// 配置版本信息
/* val commitHash by lazy { "git rev-parse --short HEAD".exec()}
val verCode = "git rev-list --count HEAD".exec().toInt() */

android {
    namespace = "cn.super12138.todo"
    compileSdk = 36

    // 获取 Release 签名
    val releaseSigning = if (project.hasProperty("releaseStoreFile")) {
        signingConfigs.create("release") {
            storeFile = File(project.properties["releaseStoreFile"] as String)
            storePassword = project.properties["releaseStorePassword"] as String
            keyAlias = project.properties["releaseKeyAlias"] as String
            keyPassword = project.properties["releaseKeyPassword"] as String
        }
    } else {
        signingConfigs.getByName("debug")
    }

    defaultConfig {
        applicationId = "cn.super12138.todo"
        minSdk = 24
        targetSdk = 36
        versionCode = 659
        versionName = "2.1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        base.archivesName.set("todo-${versionName}")
    }

    buildTypes {
        all {
            signingConfig = releaseSigning
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    aboutLibraries {
        collect {
            configPath = file("$projectDir/licences")
        }
    }
    buildFeatures {
        compose = true
    }
    // F-Droid 构建无法检测依赖信息块，所以将其忽略
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {

    // Android X
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.runtime.livedata)
    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.animation)
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icon.core)
    implementation(libs.androidx.material.icon.extended)
    // About Libraries
    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose)
    // M3 Color
    implementation(libs.com.kyant0.m3color)
    // Konfetti
    implementation(libs.nl.dionsegijn.konfetti.compose)
    // Lazy Column Scrollbar
    implementation(libs.lazycolumnscrollbar)
    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// 命令执行工具类
/*fun String.exec(): String = exec(this)

fun Project.exec(command: String): String = providers.exec {
    commandLine(command.split(" "))
}.standardOutput.asText.get().trim()*/
