package com.example.practica10

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

/**
 * Práctica 10 — Uso de la Cámara.
 *
 * Equivalencias con la implementación tradicional (Activities + XML):
 *
 *  - rememberLauncherForActivityResult -> startActivityForResult() con
 *    onActivityResult(): recupera el resultado de otra Activity (aquí la
 *    app nativa de cámara) sin bloquear la UI.
 *  - ActivityResultContracts.TakePicturePreview -> el contrato que abre
 *    la cámara y devuelve la foto como Bitmap en memoria (no exige
 *    FileProvider ni permiso de almacenamiento).
 *  - RequestPermission (Reto) -> checkSelfPermission() + requestPermissions()
 *    de la API de permisos en tiempo de ejecución.
 *  - Image(bitmap.asImageBitmap()) -> ImageView.setImageBitmap(bitmap).
 *
 * Las fotos viven en una lista observable y se pintan en un
 * LazyVerticalGrid, igual que en la práctica anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamaraScreen() {
    val context = LocalContext.current

    // Lista observable de imágenes (Bitmaps) capturadas con la cámara.
    var listaImagenes by remember { mutableStateOf(listOf<Bitmap>()) }

    // Lanzador para abrir la cámara y recibir el Bitmap capturado.
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            listaImagenes = listaImagenes + bitmap
        }
    }

    // RETO: lanzador para solicitar el permiso de cámara de forma explícita
    // en tiempo de ejecución. Si el usuario lo concede, se abre la cámara;
    // si lo deniega, se avisa con un Toast.
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            camaraLauncher.launch(null)
        } else {
            Toast.makeText(
                context,
                "Permiso de cámara denegado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cámara y Galería") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        // Botón flotante: solicita el permiso si hace falta y toma la foto.
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // RETO: antes de invocar a camaraLauncher verificamos si la
                    // aplicación tiene el permiso android.Manifest.permission.CAMERA.
                    val tienePermiso = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (tienePermiso) {
                        // Permiso concedido: lanzamos la app de cámara nativa.
                        camaraLauncher.launch(null)
                    } else {
                        // Sin permiso: nuevo lanzador con el contrato
                        // ActivityResultContracts.RequestPermission().
                        permisoLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tomar Foto"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            if (listaImagenes.isEmpty()) {
                // Estado inicial: todavía no se ha tomado ninguna foto.
                Text("No hay fotos en la galería. ¡Toma una!")
            } else {
                // Cuadrícula con las fotos capturadas (Bitmap -> ImageBitmap).
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listaImagenes) { bitmap ->
                        ImagenCapturadaItem(bitmap = bitmap)
                    }
                }
            }
        }
    }
}

/**
 * Celda de la cuadrícula: Card cuadrada con la foto capturada.
 *
 * @param bitmap la fotografía devuelta por la app de cámara.
 */
@Composable
fun ImagenCapturadaItem(bitmap: Bitmap) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        // Convertimos el Bitmap a ImageBitmap para que Compose lo renderice.
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Foto capturada",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
