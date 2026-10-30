package com.example.practica10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Práctica 10 — Uso de la Cámara.
 *
 * MainActivity solo aloja la pantalla de la cámara: dentro de setContent
 * se invoca a CamaraScreen() (Paso 7 del material).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CamaraScreen()
        }
    }
}
