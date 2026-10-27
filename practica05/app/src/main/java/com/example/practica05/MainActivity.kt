package com.example.practica05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Paso 6 del guion: dentro de setContent únicamente se invoca a FormScreen(),
 * la pantalla que se encarga de mostrar el formulario y gestionar las
 * preferencias del usuario.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FormScreen()
        }
    }
}
