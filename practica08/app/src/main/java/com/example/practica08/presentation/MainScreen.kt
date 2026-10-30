package com.example.practica08.presentation

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.example.practica08.data.Usuario
import com.example.practica08.domain.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Práctica 08 — integración de Room con Jetpack Compose.
 *
 * La capa de datos (Room) se conecta con la interfaz de forma reactiva:
 * el Flow devuelto por el DAO se transforma en un Estado de Compose con
 * collectAsState(), de modo que la lista se redibuja sola cada vez que
 * cambian los datos (sin actualizar la vista manualmente).
 */
@Composable
fun MainScreen() {
    val context = LocalContext.current
    // Obtenemos la instancia de la BD y el DAO (Singleton).
    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.usuarioDao()

    // Variable para ejecutar las corrutinas (inserciones/eliminaciones).
    val coroutineScope = rememberCoroutineScope()

    // collectAsState() transforma el Flow en un Estado que redibuja la
    // UI si hay cambios.
    val usuarios by dao.obtenerTodos().collectAsState(initial = emptyList())

    var nuevoNombre by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        OutlinedTextField(
            value = nuevoNombre,
            onValueChange = { nuevoNombre = it },
            label = { Text("Nombre del usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (nuevoNombre.isNotBlank()) {
                    coroutineScope.launch(Dispatchers.IO) {
                        // 1. Guardar en la base de datos (hilo secundario).
                        dao.insertar(Usuario(nombre = nuevoNombre))

                        // 2. Regresar al hilo principal para actualizar la UI.
                        withContext(Dispatchers.Main) {
                            nuevoNombre = ""
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Usuario")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Lista de Usuarios Registrados:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(usuarios) { usuario ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ID: ${usuario.id} | Nombre: ${usuario.nombre}",
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                        )
                        // RETO: ícono de papelera para eliminar el registro.
                        IconButton(onClick = {
                            // Corrutina que llama al método eliminar del DAO
                            // pasándole el usuario correspondiente.
                            coroutineScope.launch {
                                dao.eliminar(usuario)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar ${usuario.nombre}"
                            )
                        }
                    }
                }
            }
        }
    }
}
