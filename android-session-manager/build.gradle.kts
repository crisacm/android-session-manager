import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "2.0.0"
    id("com.google.protobuf") version "0.9.3"
    id("maven-publish")
}

android {
    namespace = "com.github.crisacm.module.sessionmanager"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.crisacm"
            artifactId = "android-session-manager"
            version = "0.0.7"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // DataStore: Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-preferences-core:1.1.1")

    // DataStore: Proto
    implementation("com.google.protobuf:protobuf-javalite:3.21.12")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:3.21.12" }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                id("java") { option("lite") }
            }
        }
    }
}
