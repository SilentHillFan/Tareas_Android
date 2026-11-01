plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // Plugin KSP (Kotlin Symbol Processing) para compilar el procesador
    // de anotaciones de Room.
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.miniproyecto03"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.miniproyecto03"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Dependencias de Room Database (persistencia local SQLite).
    // Se usa 2.7.1 porque 2.6.1 no es compatible con KSP2
    // (error "unexpected jvm signature V"). Misma API.
    val room_version = "2.7.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    // Librería extendida de íconos de Material Design (Add, Delete,
    // PhotoCamera y Image usados en el formulario y en las tarjetas).
    implementation("androidx.compose.material:material-icons-extended")
    // Coil (Coroutine Image Loader): dibuja la foto local de cada
    // publicación en la cuadrícula de Compose (equivale a Glide en XML).
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // lifecycle-viewmodel-compose: API viewModel() para crear el
    // ViewModel de la arquitectura MVVM desde Compose.
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}