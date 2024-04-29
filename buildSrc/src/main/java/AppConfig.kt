import org.gradle.api.JavaVersion

object AppConfig {
    const val compileSdk = 34
    const val minSdk = 26
    const val targetSdk = 34
    const val applicationId = "com.github.crisacm.sessionmanager"
    const val versionCode = 1
    const val versionName = "1.0.0"
    const val androidTestInstrumentation = "androidx.test.runner.AndroidJUnitRunner"

    val javaJvmTarget = JavaVersion.VERSION_1_8.toString()
    val javaCompatibility = JavaVersion.VERSION_1_8
}
