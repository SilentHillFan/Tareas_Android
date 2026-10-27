package com.example.practica06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Práctica 06 — LazyColumn y Listas Dinámicas.
 *
 * MainActivity solo aloja la única pantalla de la aplicación; dentro de
 * setContent se invoca ListaScreen().
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaScreen()
        }
    }
}
