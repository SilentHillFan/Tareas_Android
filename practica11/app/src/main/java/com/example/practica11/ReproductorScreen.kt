package com.example.practica11

import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Pantalla 1 (Paso 5 del guion): mini reproductor MP3.
 *
 * Se usa Storage Access Framework mediante [ActivityResultContracts.GetContent] para que el
 * usuario elija un archivo de audio desde el explorador nativo, evitando así los permisos
 * directos de almacenamiento que Scoped Storage restringe a partir de Android 10.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReproductorScreen() {
    val context = LocalContext.current

    // Estados para la URI del archivo, el reproductor y si está sonando
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("Ningún archivo seleccionado") }

    // Lanzador para abrir el explorador de archivos (filtra únicamente audios)
    val selectorAudio = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            audioUri = uri

            // Liberar el reproductor anterior si existía para evitar fugas de memoria
            mediaPlayer?.release()

            // Crear un nuevo MediaPlayer con el archivo seleccionado
            mediaPlayer = MediaPlayer.create(context, uri).also { reproductor ->
                reproductor.start()
                // Escuchar cuando la canción termine automáticamente
                reproductor.setOnCompletionListener { isPlaying = false }
            }
            isPlaying = mediaPlayer != null
            mensaje = if (isPlaying) {
                "Audio cargado y reproduciéndose"
            } else {
                "No se pudo abrir el archivo de audio"
            }
        }
    }

    // DisposableEffect libera los recursos del MediaPlayer si el usuario sale de la app
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mini Reproductor MP3") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para abrir el explorador y filtro de archivos "solo audios"
            Button(onClick = { selectorAudio.launch("audio/*") }) {
                Text("Buscar MP3 en SD/Memoria")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Controles de reproducción
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reproducir
                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null && !isPlaying) {
                            mediaPlayer?.start()
                            isPlaying = true
                            mensaje = "Reproduciendo"
                        }
                    }
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir")
                }

                // Pausar
                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null && isPlaying) {
                            mediaPlayer?.pause()
                            isPlaying = false
                            mensaje = "En pausa"
                        }
                    }
                ) {
                    Icon(Icons.Default.Pause, contentDescription = "Pausar")
                }

                // RETO (entregable): Detener != Pausar: además de pausar, regresa al segundo 0
                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null) {
                            mediaPlayer?.pause()
                            mediaPlayer?.seekTo(0)
                            isPlaying = false
                            mensaje = "Detenido (posición en 0:00)"
                        }
                    }
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Detener")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (audioUri == null) {
                    "Ningún archivo seleccionado"
                } else {
                    "Audio cargado correctamente"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
