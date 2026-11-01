plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "be.mychat.assistant"
    compileSdk = 35
    defaultConfig {
        applicationId = "be.mychat.assistant"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1"
    }
}
