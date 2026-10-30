package com.example.miniproyecto02.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.miniproyecto02.data.Tarea
import com.example.miniproyecto02.domain.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Criterio de ordenamiento seleccionado en el menú de la ToolBar. */
private enum class OrdenTareas { POR_FECHA, POR_ESTADO }

/**
 * Mini Proyecto 02 — Administrador de Tareas Locales.
 *
 * Integra los conceptos de las prácticas anteriores:
 *
 *  - Scaffold + TopAppBar + DropdownMenu -> ToolBar y menú de opciones
 *    (Práctica 07): las opciones gestionan el listado (borrar
 *    completadas, ordenar por fecha o por estado).
 *  - LazyColumn con Cards                -> RecyclerView con Adapter y
 *    ViewHolder (Práctica 06): cada item es una tarea de la lista.
 *  - Room con collectAsState()           -> persistencia local SQLite
 *    conectada de forma reactiva con Compose (Práctica 08): la lista se
 *    redibuja sola al insertar, editar, completar o eliminar.
 *
 * Equivalencias con la implementación tradicional:
 *
 *  - @Entity            -> tabla "tareas" (CREATE TABLE)
 *  - TareaDao           -> las sentencias SQL (INSERT/SELECT/UPDATE/DELETE)
 *  - AppDatabase        -> SQLiteOpenHelper (conexión + Singleton)
 *  - Adapter/ViewHolder -> LazyColumn + items()
 *  - OptionsMenu        -> DropdownMenu del menú de desbordamiento
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current

    // Instancia de la base de datos y DAO (patrón Singleton).
    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.tareaDao()

    // Corrutina para las operaciones de escritura (insertar/editar/eliminar).
    val coroutineScope = rememberCoroutineScope()

    // Flow del DAO convertido en Estado de Compose: la UI se redibuja
    // automáticamente cada vez que cambian los datos en SQLite.
    val tareas by dao.obtenerTodas().collectAsState(initial = emptyList())

    // Texto pendiente de guardar en el campo de nueva tarea.
    var nuevoTitulo by remember { mutableStateOf("") }

    // Menú de desbordamiento de la ToolBar.
    var menuExpandido by remember { mutableStateOf(false) }

    // Criterio de ordenamiento activo (opción elegida en el menú).
    var orden by remember { mutableStateOf(OrdenTareas.POR_FECHA) }

    // Estado del diálogo de edición (null = cerrado).
    var tareaEnEdicion by remember { mutableStateOf<Tarea?>(null) }
    var textoEdicion by remember { mutableStateOf("") }

    // Lista reordenada según la opción seleccionada en el menú.
    val tareasOrdenadas = when (orden) {
        // Más recientes primero.
        OrdenTareas.POR_FECHA -> tareas.sortedByDescending { it.fechaCreacion }
        // Pendientes primero, completadas al final.
        OrdenTareas.POR_ESTADO -> tareas.sortedBy { it.completada }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrador de Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                // Ícono de navegación (hamburguesa) de la ToolBar.
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Clic en Navegación", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                // Acciones de la ToolBar: menú de desbordamiento con las
                // opciones de gestión del listado (Options Menu tradicional).
                actions = {
                    IconButton(onClick = { menuExpandido = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opciones"
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false }
                    ) {
                        // 1) Borrar todas las tareas completadas.
                        DropdownMenuItem(
                            text = { Text("Borrar completadas") },
                            onClick = {
                                menuExpandido = false
                                coroutineScope.launch(Dispatchers.IO) {
                                    dao.borrarCompletadas()
                                }
                                Toast.makeText(
                                    context,
                                    "Tareas completadas eliminadas",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                        // 2) Ordenar por fecha de creación (más recientes).
                        DropdownMenuItem(
                            text = { Text("Ordenar por fecha") },
                            onClick = {
                                menuExpandido = false
                                orden = OrdenTareas.POR_FECHA
                                Toast.makeText(
                                    context,
                                    "Orden: por fecha de creación",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                        // 3) Ordenar por estado (pendientes primero).
                        DropdownMenuItem(
                            text = { Text("Ordenar por estado") },
                            onClick = {
                                menuExpandido = false
                                orden = OrdenTareas.POR_ESTADO
                                Toast.makeText(
                                    context,
                                    "Orden: pendientes primero",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            )
        },
        // Botón flotante: guarda la tarea escrita en el campo.
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (nuevoTitulo.isNotBlank()) {
                        coroutineScope.launch(Dispatchers.IO) {
                            // INSERT: registrar la tarea en SQLite.
                            dao.insertar(Tarea(titulo = nuevoTitulo.trim()))
                            // Limpiar el campo en el hilo principal.
                            withContext(Dispatchers.Main) {
                                nuevoTitulo = ""
                            }
                        }
                        Toast.makeText(context, "Tarea agregada", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar tarea"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            // Campo para escribir la tarea a crear.
            OutlinedTextField(
                value = nuevoTitulo,
                onValueChange = { nuevoTitulo = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tareas registradas (${tareas.size}):",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (tareas.isEmpty()) {
                Text(
                    text = "Aún no hay tareas. Agrega una con el botón +.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Listado dinámico (equivalente al RecyclerView de XML).
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tareasOrdenadas, key = { it.id }) { tarea ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Marcar como completada (UPDATE de la fila).
                            Checkbox(
                                checked = tarea.completada,
                                onCheckedChange = { completada ->
                                    coroutineScope.launch(Dispatchers.IO) {
                                        dao.actualizar(
                                            tarea.copy(completada = completada)
                                        )
                                    }
                                }
                            )
                            // Título tachado cuando la tarea está completada.
                            Text(
                                text = tarea.titulo,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 4.dp),
                                textDecoration = if (tarea.completada) {
                                    TextDecoration.LineThrough
                                } else {
                                    TextDecoration.None
                                }
                            )
                            // Editar: abre el diálogo con el título precargado.
                            IconButton(onClick = {
                                textoEdicion = tarea.titulo
                                tareaEnEdicion = tarea
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar ${tarea.titulo}"
                                )
                            }
                            // Eliminar (papelera): DELETE de la fila.
                            IconButton(onClick = {
                                coroutineScope.launch(Dispatchers.IO) {
                                    dao.eliminar(tarea)
                                }
                                Toast.makeText(
                                    context,
                                    "Tarea eliminada",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar ${tarea.titulo}"
                                )
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de edición (UPDATE del título de la tarea).
        tareaEnEdicion?.let { tarea ->
            AlertDialog(
                onDismissRequest = { tareaEnEdicion = null },
                title = { Text("Editar tarea") },
                text = {
                    OutlinedTextField(
                        value = textoEdicion,
                        onValueChange = { textoEdicion = it },
                        label = { Text("Título de la tarea") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (textoEdicion.isNotBlank()) {
                            coroutineScope.launch(Dispatchers.IO) {
                                dao.actualizar(
                                    tarea.copy(titulo = textoEdicion.trim())
                                )
                            }
                            Toast.makeText(
                                context,
                                "Tarea actualizada",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        tareaEnEdicion = null
                    }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { tareaEnEdicion = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
