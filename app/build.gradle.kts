import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}

android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    val firebaseProperties: Map<String, String> = lazy {
        val properties = Properties()
        properties.load(File("firebase.properties").inputStream())
        properties.map { it.key.toString() to it.value.toString() }.toMap()
    }.value
    flavorDimensions += AppConfig.flavorDimension
    productFlavors {
        create(AppConfig.flavorDev) {
            dimension = AppConfig.flavorDimension
            versionNameSuffix = "-dev"
            buildConfigField("String", "FIREBASE_KEY", "\"${firebaseProperties["firebase.auth"] as String}\"")
        }
        create(AppConfig.flaverProduction) {
            dimension = AppConfig.flavorDimension
            versionNameSuffix = "-prod"
            buildConfigField("String", "FIREBASE_KEY", "\"${firebaseProperties["firebase.auth"] as String}\"")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = AppConfig.javaCompatibility
        targetCompatibility = AppConfig.javaCompatibility
    }
    kotlinOptions {
        jvmTarget = AppConfig.javaJvmTarget
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":android-session-manager"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-extended:1.6.6")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Google
    implementation(libs.play.services.auth.base)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    // Koin
    implementation(platform("io.insert-koin:koin-bom:3.5.0"))
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")
    implementation("io.insert-koin:koin-android:3.5.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // Logged
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Navigation + Serialization
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Google credentials
    implementation("com.google.android.gms:play-services-auth:21.1.1")

    // Tink
    implementation("com.google.crypto.tink:tink-android:1.7.0")
}