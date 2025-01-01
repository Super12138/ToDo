plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

val baseVersionName = "1.0.6"
val commitHash by lazy { "git rev-parse --short HEAD".exec() }
val verCode by lazy { "git rev-list --count HEAD".exec().toInt() }

android {
    namespace = "cn.super12138.todo"
    compileSdk = 35

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
        targetSdk = 35
        versionCode = verCode
        versionName = "${baseVersionName}-${commitHash}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        base.archivesName.set("todo-${baseVersionName}")
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Android X
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.security)
    // Material Design
    implementation(libs.material)
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    // FastScroll
    implementation(project(":fastscroll"))
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

fun String.exec(): String = exec(this)

fun Project.exec(command: String): String = providers.exec {
    commandLine(command.split(" "))
}.standardOutput.asText.get().trim()