package com.example.miniproyecto03.presentation

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.miniproyecto03.data.Publicacion
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Mini Proyecto 03 — Red Social Local.
 *
 * Pantalla única con formulario de alta y listado de publicaciones.
 *
 * Equivalencias con la arquitectura clásica (Activities + XML + LiveData):
 *
 *  - PublicacionViewModel (MVVM) -> presenter/repository observado con
 *    LiveData: la UI solo lee StateFlow y llama a funciones de negocio.
 *  - StateFlow<List<Publicacion>> -> LiveData<List<Publicacion>>.
 *  - DAO de Room -> SQLiteOpenHelper + rawQuery/ContentValues.
 *  - AsyncImage (Coil) -> Glide en un ImageView.
 *  - rememberLauncherForActivityResult -> startActivityForResult().
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val contexto = LocalContext.current
    val alcance = rememberCoroutineScope()

    // ViewModel con Factory (necesita Application para abrir Room).
    val viewModel: PublicacionViewModel = viewModel(
        factory = PublicacionViewModel.Factory(contexto.applicationContext as Application)
    )

    val publicaciones by viewModel.publicaciones.collectAsState()
    val formulario by viewModel.formulario.collectAsState()

    // Estado del selector de fecha (DatePicker de Material 3).
    var mostrarDatePicker by remember { mutableStateOf(false) }

    // Ruta del archivo de cámara pendiente de confirmar.
    var rutaCamaraPendiente by remember { mutableStateOf("") }

    // ------------------------------------------------------ Cámara (FileProvider)
    // Contrato TakePicture(): la app de cámara nativa escribe la foto
    // directamente en el archivo que nosotros le entregamos como URI
    // content:// generada por FileProvider.
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito && File(rutaCamaraPendiente).exists()) {
            viewModel.onFotoCambiada(rutaCamaraPendiente)
        } else {
            // El usuario canceló: no dejamos archivos vacíos.
            File(rutaCamaraPendiente).takeIf { it.exists() && it.length() == 0L }?.delete()
            Toast.makeText(contexto, "No se tomó la fotografía", Toast.LENGTH_SHORT).show()
        }
    }

    // Permiso de cámara en tiempo de ejecución (solicitar y manejar).
    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            lanzarCamara(contexto, camaraLauncher) { rutaCamaraPendiente = it }
        } else {
            Toast.makeText(
                contexto,
                "Permiso de cámara denegado: no se puede tomar fotos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun pedirCamara() {
        val concedido = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (concedido) {
            lanzarCamara(contexto, camaraLauncher) { rutaCamaraPendiente = it }
        } else {
            permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // -------------------------------------------------------- Galería (Picker)
    // PickVisualMedia (Photo Picker): NO requiere permisos, el usuario
    // solo concede acceso a la imagen elegida.
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            alcance.launch {
                val destino = copiarFotoDeGaleria(contexto, uri)
                if (destino != null) {
                    viewModel.onFotoCambiada(destino)
                } else {
                    Toast.makeText(contexto, "No se pudo cargar la imagen", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Red Social Local") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        // FAB: guarda la publicación si el formulario es válido.
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (viewModel.publicar()) {
                        Toast.makeText(contexto, "Publicación guardada", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            contexto,
                            "Completa título, descripción y foto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Publicar"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp)
        ) {
            // --------------------------------------------------- Formulario de alta
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Nueva publicación",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = formulario.titulo,
                            onValueChange = viewModel::onTituloCambiado,
                            label = { Text("Título") },
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = formulario.descripcion,
                            onValueChange = viewModel::onDescripcionCambiada,
                            label = { Text("Descripción") },
                            minLines = 3,
                            maxLines = 5,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Default
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // ------------------------------------------ Selector de fecha
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Fecha:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(onClick = { mostrarDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = formatearFecha(formulario.fecha))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // ---------------------------------- Cámara y Galería
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(onClick = { pedirCamara() }) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Tomar foto")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = {
                                    galeriaLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Galería")
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // -------------------------------------- Miniatura de la foto
                        if (formulario.fotoUri.isEmpty()) {
                            Text(
                                text = "Sin fotografía seleccionada",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = File(formulario.fotoUri),
                                    contentDescription = "Foto seleccionada",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Fotografía adjunta",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    TextButton(onClick = viewModel::limpiarFormulario) {
                                        Text(text = "Quitar foto")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // -------------------------------------------------------- Listado
            item {
                Text(
                    text = "Publicaciones (${publicaciones.size})",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (publicaciones.isEmpty()) {
                item {
                    Text(
                        text = "Aún no hay publicaciones. ¡Crea la primera!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            items(publicaciones, key = { it.id }) { publicacion ->
                TarjetaPublicacion(
                    publicacion = publicacion,
                    onEliminar = { viewModel.eliminar(publicacion) }
                )
            }
        }
    }

    // ------------------------------------------------- Diálogo del DatePicker
    if (mostrarDatePicker) {
        val estadoFecha = rememberDatePickerState(
            initialSelectedDateMillis = formulario.fecha
        )
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        estadoFecha.selectedDateMillis?.let { millis ->
                            viewModel.onFechaCambiada(normalizarFechaLocal(millis))
                        }
                        mostrarDatePicker = false
                    }
                ) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = estadoFecha)
        }
    }
}

/**
 * Tarjeta de una publicación: foto (Coil), título, descripción, fecha
 * formateada y botón de eliminar.
 */
@Composable
private fun TarjetaPublicacion(
    publicacion: Publicacion,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Foto de la publicación cargada desde el almacenamiento interno.
            AsyncImage(
                model = File(publicacion.fotoUri),
                contentDescription = "Fotografía de ${publicacion.titulo}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = publicacion.titulo,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = publicacion.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Fecha",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatearFecha(publicacion.fecha),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    IconButton(onClick = onEliminar) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar ${publicacion.titulo}",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

/**
 * Crea el archivo de la foto en la carpeta interna "fotos/" de la app y
 * lo comparte con la cámara mediante FileProvider, lanzando después el
 * contrato TakePicture().
 *
 * @param alGuardar callback que informa la ruta elegida al ViewModel.
 */
private fun lanzarCamara(
    contexto: android.content.Context,
    launcher: androidx.activity.result.ActivityResultLauncher<android.net.Uri>,
    alGuardar: (String) -> Unit
) {
    val carpeta = File(contexto.filesDir, "fotos").apply { mkdirs() }
    val archivo = File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
    val uri = androidx.core.content.FileProvider.getUriForFile(
        contexto,
        "${contexto.packageName}.fileprovider",
        archivo
    )
    alGuardar(archivo.absolutePath)
    launcher.launch(uri)
}

/**
 * Copia la imagen elegida en el Photo Picker al almacenamiento interno
 * de la app (filesDir/fotos/), para que la publicación pueda mostrarla
 * siempre sin depender de permisos de la galería.
 *
 * @return la ruta del archivo copiado o null si falló.
 */
private suspend fun copiarFotoDeGaleria(
    contexto: android.content.Context,
    uri: android.net.Uri
): String? = try {
    val carpeta = File(contexto.filesDir, "fotos").apply { mkdirs() }
    val destino = File(carpeta, "galeria_${System.currentTimeMillis()}.jpg")
    val entrada = contexto.contentResolver.openInputStream(uri) ?: return null
    entrada.use { origen ->
        destino.outputStream().use { destinoStream ->
            origen.copyTo(destinoStream)
        }
    }
    destino.absolutePath
} catch (e: Exception) {
    null
}

/**
 * Convierte los milisegundos UTC que devuelve el DatePicker (medianoche
 * UTC del día elegido) a medianoche local del mismo día calendario, para
 * que la fecha formateada en la tarjeta sea el día que eligió el usuario.
 */
private fun normalizarFechaLocal(millisUtc: Long): Long {
    // Leemos año/mes/día en UTC (como los entrega el DatePicker)...
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        timeInMillis = millisUtc
    }
    // ...y los volvemos a componer en la zona horaria del dispositivo,
    // a medianoche, para que formatearlos muestre el día correcto.
    return Calendar.getInstance().apply {
        clear()
        set(
            utc.get(Calendar.YEAR),
            utc.get(Calendar.MONTH),
            utc.get(Calendar.DAY_OF_MONTH),
            12, 0, 0
        )
    }.timeInMillis
}

/** Formatea una fecha en epoch como dd/MM/yyyy. */
private fun formatearFecha(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))
