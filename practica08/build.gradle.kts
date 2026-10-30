// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Paso 4: KSP (Kotlin Symbol Processing) para el procesamiento de
    // anotaciones de Room. La versión indica la compatibilidad con Kotlin.
    id("com.google.devtools.ksp") version "2.2.10-2.0.2" apply false
}