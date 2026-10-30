package com.example.practica09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Práctica 09 — Galería de Imágenes.
 *
 * MainActivity solo aloja la pantalla de la galería: dentro de setContent
 * se invoca a GaleriaScreen().
 *
 * Nota: el material la escribe GalleryScreen() en el Paso 7; aquí se usa
 * el nombre real de la función definida en galleryScreen.kt (GaleriaScreen).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GaleriaScreen()
        }
    }
}
