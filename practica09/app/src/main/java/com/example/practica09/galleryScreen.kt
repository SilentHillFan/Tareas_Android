package com.example.practica09

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * Práctica 09 — Galería de Imágenes.
 *
 * Equivalencias con la implementación tradicional en XML:
 *
 *  - LazyVerticalGrid -> RecyclerView con GridLayoutManager (los items se
 *                        reciclan al hacer scroll, sin crear vistas nuevas).
 *  - AsyncImage (Coil) -> Glide/Picasso en XML: descarga la imagen de
 *                        internet en segundo plano, la guarda en caché y
 *                        la dibuja sin bloquear la UI.
 *  - Card              -> el CardView tradicional (esquinas redondeadas
 *                        y elevación sobre el fondo).
 *
 * Coil (Coroutine Image Loader) gestiona el ciclo de vida y la memoria:
 * solo se cargan las imágenes visibles y se recicla la caché, por lo que
 * el scroll permanece fluido con muchas imágenes.
 */
@Composable
fun GaleriaScreen() {
    val context = LocalContext.current

    // Lista de URLs de imágenes de prueba (las 8 del material).
    val imagenes = listOf(
        "https://picsum.photos/id/1018/400/400",
        "https://picsum.photos/id/1015/400/400",
        "https://picsum.photos/id/1019/400/400",
        "https://picsum.photos/id/1020/400/400",
        "https://picsum.photos/id/1021/400/400",
        "https://picsum.photos/id/1022/400/400",
        "https://picsum.photos/id/1023/400/400",
        "https://picsum.photos/id/1024/400/400",
        // URL inválida de prueba: permite verificar el estado de error
        // (manejo de errores) exigido en la consigna de la práctica.
        "https://picsum.photos/id/999999/400/400"
    )

    // RETO: estado con la imagen seleccionada; null = diálogo cerrado.
    // El estado vive en GaleriaScreen y se pasa a cada ImagenItem.
    var imagenSeleccionada by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Insets para no quedar debajo de las barras del sistema.
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(8.dp)
            .padding(top = 32.dp)
    ) {
        Text(
            text = "Mi Galería",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )

        // LazyVerticalGrid: cuadrícula con scroll vertical.
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Indicamos que queremos 2 columnas
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(imagenes) { url ->
                ImagenItem(
                    url = url,
                    // RETO: al presionar una imagen se abre la vista de detalle.
                    onClick = { imagenSeleccionada = url }
                )
            }
        }
    }

    // RETO: vista de detalle con la imagen seleccionada en gran tamaño,
    // ocupando el centro de la pantalla (AlertDialog de Compose).
    imagenSeleccionada?.let { url ->
        AlertDialog(
            onDismissRequest = { imagenSeleccionada = null },
            title = { Text("Detalle de la imagen") },
            text = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen ampliada",
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.ic_placeholder),
                    error = painterResource(R.drawable.ic_broken_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            },
            confirmButton = {
                TextButton(onClick = { imagenSeleccionada = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

/**
 * Celda de la cuadrícula: Card cuadrada con la imagen descargada por Coil.
 *
 * @param url     dirección de la imagen en internet.
 * @param onClick RETO: se invoca al presionar la celda para mostrar el
 *                detalle de la imagen (estado manejado en GaleriaScreen).
 */
@Composable
fun ImagenItem(url: String, onClick: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            // RETO: modifier clickable; el clip acota el efecto de ondulación
            // a las esquinas redondeadas de la Card.
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        // AsyncImage de Coil maneja la carga de red y el ciclo de vida:
        //  - placeholder: indicador mientras la imagen se descarga;
        //  - crossfade:    transición suave al terminar la carga;
        //  - error:        se muestra si la URL no se puede descargar.
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = "Imagen de la galería",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_placeholder),
            error = painterResource(R.drawable.ic_broken_image),
            modifier = Modifier.fillMaxSize()
        )
    }
}
