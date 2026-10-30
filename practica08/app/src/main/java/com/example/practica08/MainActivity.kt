package com.example.practica08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.practica08.presentation.MainScreen

/**
 * Práctica 08 — SQLite / Room Database.
 *
 * MainActivity solo aloja la pantalla principal: dentro de setContent se
 * invoca a MainScreen(), que integra Room con la interfaz reactiva.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}
