package com.example.practica11

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

/**
 * Pantalla 2 (Consigna de la rúbrica): lectura y escritura de archivos de texto.
 *
 * Permite ingresar texto, elegir la ubicación de destino (memoria interna o
 * almacenamiento externo / SDCard) y ejecutar Guardar, Leer y Listar con
 * [File], [java.io.FileOutputStream] y [java.io.FileInputStream].
 *
 * Los permisos de almacenamiento se solicitan en tiempo de ejecución.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivosScreen() {
    val context = LocalContext.current

    var nombreArchivo by remember { mutableStateOf("notas_practica11.txt") }
    var texto by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf(DestinoAlmacenamiento.INTERNO) }
    var resultado by remember { mutableStateOf<ResultadoOperacion?>(null) }
    var permisoConcedido by remember { mutableStateOf(Almacenamiento.permisoConcedido(context)) }

    // Solicitud del permiso en tiempo de ejecución (runtime permissions)
    val solicitudPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoConcedido = concedido
        resultado = ResultadoOperacion(
            exito = concedido,
            ruta = "",
            detalle = if (concedido) {
                "Permiso de almacenamiento concedido."
            } else {
                "Permiso de almacenamiento DENEGADO. Podrás usar Memoria interna, " +
                    "pero no el almacenamiento público de la tarjeta SD."
            }
        )
    }

    fun solicitarPermiso() {
        solicitudPermiso.launch(Almacenamiento.permisoRequerido())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Archivos y permisos") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ---------------------------------------------------------------
            // Estado del permiso
            // ---------------------------------------------------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (permisoConcedido) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (permisoConcedido) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (permisoConcedido) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            }
                        )
                        Text(
                            text = if (permisoConcedido) "Permiso concedido" else "Permiso NO concedido",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Permiso solicitado: ${Almacenamiento.nombrePermisoRequerido()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = ::solicitarPermiso) {
                        Text("Solicitar permiso de almacenamiento")
                    }
                }
            }

            // ---------------------------------------------------------------
            // Nombre del archivo y contenido
            // ---------------------------------------------------------------
            OutlinedTextField(
                value = nombreArchivo,
                onValueChange = { nombreArchivo = it },
                label = { Text("Nombre del archivo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Texto a guardar") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            // ---------------------------------------------------------------
            // Selección de destino
            // ---------------------------------------------------------------
            Text("Ubicación de destino", style = MaterialTheme.typography.titleSmall)
            DestinoAlmacenamiento.entries.forEach { opcion ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = destino == opcion,
                        onClick = {
                            destino = opcion
                            // Se pide el permiso en el momento en que se elige la SDCard
                            if (opcion == DestinoAlmacenamiento.EXTERNO && !permisoConcedido) {
                                solicitarPermiso()
                            }
                        }
                    )
                    Text(opcion.etiqueta, style = MaterialTheme.typography.bodyLarge)
                }
            }

            // ---------------------------------------------------------------
            // Acciones
            // ---------------------------------------------------------------
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        resultado = Almacenamiento.guardar(context, destino, nombreArchivo, texto)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }
                OutlinedButton(
                    onClick = {
                        resultado = Almacenamiento.leer(context, destino, nombreArchivo)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Leer")
                }
            }

            OutlinedButton(
                onClick = { resultado = Almacenamiento.listar(context, destino) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Listar archivos del destino")
            }

            // ---------------------------------------------------------------
            // Resultado de la operación
            // ---------------------------------------------------------------
            resultado?.let { res ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (res.exito) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        }
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = res.detalle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (res.exito) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            }
                        )
                        if (res.ruta.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Ruta: ${res.ruta}",
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        if (res.contenido.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = res.contenido,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}
