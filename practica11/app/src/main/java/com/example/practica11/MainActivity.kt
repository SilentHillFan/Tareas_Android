package com.example.practica11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.practica11.ui.theme.Practica11Theme

/**
 * Actividad principal de la Práctica 11.
 *
 * La app tiene dos pantallas:
 *  1. Reproductor  -> guion del PDF (Storage Access Framework + MediaPlayer).
 *  2. Archivos     -> rúbrica (File / FileInputStream / FileOutputStream + permisos).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practica11Theme {
                Practica11App()
            }
        }
    }
}

/** Nombres de las dos pestañas de la aplicación. */
private val titulosPantallas = listOf("Reproductor", "Archivos")

/**
 * Contenedor general: barra de navegación inferior + contenido de la pestaña activa.
 */
@Composable
fun Practica11App() {
    var indiceSeleccionado by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                titulosPantallas.forEachIndexed { indice, titulo ->
                    NavigationBarItem(
                        selected = indiceSeleccionado == indice,
                        onClick = { indiceSeleccionado = indice },
                        icon = {
                            Icon(
                                imageVector = if (indice == 0) {
                                    Icons.Default.Audiotrack
                                } else {
                                    Icons.Default.Folder
                                },
                                contentDescription = titulo
                            )
                        },
                        label = { Text(titulo) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (indiceSeleccionado) {
                0 -> ReproductorScreen()
                else -> ArchivosScreen()
            }
        }
    }
}
