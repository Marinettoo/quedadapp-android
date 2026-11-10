plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.quedadapp.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.quedadapp.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        // Mismo dominio que producción en las dos variantes (para que el
        // certificado HTTPS cuadre); en depuración, DEBUG_HOST_IP hace
        // que ese dominio se resuelva a la IP del servidor en la red
        // local en vez de salir a internet (ver data/ApiClient.kt). Si
        // esa IP cambia, solo hay que actualizarla aquí.
        debug {
            buildConfigField("String", "API_BASE_URL", "\"https://furbo.marinettoo.es/\"")
            buildConfigField("String", "DEBUG_HOST_IP", "\"192.168.1.43\"")
        }
        release {
            buildConfigField("String", "API_BASE_URL", "\"https://furbo.marinettoo.es/\"")
            buildConfigField("String", "DEBUG_HOST_IP", "\"\"")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Red: Retrofit + OkHttp hablando JSON con kotlinx.serialization (en
    // vez de Gson/Moshi, para quedarnos en Kotlin puro sin reflexión).
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Guardar el token de sesión cifrado en disco (en vez de
    // SharedPreferences normal, que no cifra nada).
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
