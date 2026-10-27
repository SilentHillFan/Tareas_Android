package com.example.practica05

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica05.data.PreferencesManager

/**
 * Paso 4 del guion: FormScreen es un Composable que muestra un formulario
 * sencillo con un campo de texto y dos interruptores (Switch). Los datos se
 * guardan automáticamente en SharedPreferences al interactuar con cada control.
 *
 * Se añadieron además tres botones (Guardar, Cargar y Borrar) que operan sobre
 * el mismo PreferencesManager.
 */
@Composable
fun FormScreen() {
    // Paso 4: se obtiene el contexto actual para crear el PreferencesManager.
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    // Estados que mantienen los valores del formulario.
    var username by rememberSaveable { mutableStateOf("") }
    var darkModeEnabled by rememberSaveable { mutableStateOf(false) }
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }
    var usernameError by rememberSaveable { mutableStateOf(false) }

    // Paso 5: al abrir la pantalla se cargan los datos guardados.
    LaunchedEffect(Unit) {
        username = preferencesManager.getUsername()
        darkModeEnabled = preferencesManager.getDarkMode()
        notificationsEnabled = preferencesManager.getNotifications()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Práctica 5: Configuración",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            HorizontalDivider()

            // Campo de texto para el nombre de usuario.
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = false
                    // Guarda automáticamente al interactuar.
                    preferencesManager.saveUsername(it)
                },
                label = { Text("Nombre de Usuario") },
                singleLine = true,
                isError = usernameError,
                supportingText = {
                    if (usernameError) {
                        Text("El nombre no puede estar vacío")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Interruptor para el modo oscuro.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Modo Oscuro:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = {
                        darkModeEnabled = it
                        // Guarda automáticamente al interactuar.
                        preferencesManager.saveDarkMode(it)
                    }
                )
            }

            // Interruptor para las notificaciones.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recibir Notificaciones:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = {
                        notificationsEnabled = it
                        // Guarda automáticamente al interactuar.
                        preferencesManager.saveNotifications(it)
                    }
                )
            }

            // Botón "Guardar": almacena los tres valores en SharedPreferences.
            Button(
                onClick = {
                    if (username.isBlank()) {
                        usernameError = true
                    } else {
                        preferencesManager.saveUsername(username)
                        preferencesManager.saveDarkMode(darkModeEnabled)
                        preferencesManager.saveNotifications(notificationsEnabled)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Preferencias")
            }

            // Botón "Cargar": recupera los valores guardados.
            OutlinedButton(
                onClick = {
                    username = preferencesManager.getUsername()
                    darkModeEnabled = preferencesManager.getDarkMode()
                    notificationsEnabled = preferencesManager.getNotifications()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recargar Datos Guardados")
            }

            // Botón "Borrar": elimina todas las preferencias.
            OutlinedButton(
                onClick = {
                    preferencesManager.clear()
                    username = ""
                    darkModeEnabled = false
                    notificationsEnabled = true
                    usernameError = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Restablecer Configuración")
            }
        }
    }
}
