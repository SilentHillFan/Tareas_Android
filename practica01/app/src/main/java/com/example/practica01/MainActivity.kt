package com.example.practica01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.practica01.ui.theme.Practica01Theme

/**
 * Paso 7: se borraron de aquí todas las funciones @Composable / @Preview
 * porque ahora viven en sus propios archivos (FirstScreen.kt, SecondScreen.kt,
 * AppNavigator.kt).
 *
 * Paso 9: setContent mostraba FirstScreen().
 * Paso 11: ya existe AppNavigation(), así que MainActivity delega en ella
 * para que el NavHost sea quien maneje las dos pantallas.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practica01Theme {
                AppNavigation()
            }
        }
    }
}
