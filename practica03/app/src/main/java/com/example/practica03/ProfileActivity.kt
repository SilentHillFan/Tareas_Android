package com.example.practica03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * Paso 5: Activity de destino.
 *
 * Su responsabilidad es recibir el objeto Intent que la invocó, extraer los
 * datos adjuntos con getStringExtra() y pasárselos a ProfileScreen.
 *
 * getStringExtra devuelve null si la clave no existe, por lo que el operador
 * Elvis (?:) aporta un valor por defecto en vez de permitir un nulo.
 *
 * Debe estar declarada en AndroidManifest.xml (Paso 8), si no el Intent
 * explícito falla y la aplicación se cierra.
 */
class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Traemos los valores del Intent
        val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Sin Nombre"
        val correo = intent.getStringExtra("EXTRA_CORREO") ?: "Sin Correo"

        setContent {
            ProfileScreen(
                nombre = nombre,
                correo = correo,
                onBackClick = { finish() }
            )
        }
    }
}
