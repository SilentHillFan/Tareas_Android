package com.example.miniproyecto01.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miniproyecto01.data.PreferencesManager

// Opciones del menú desplegable de Carrera.
private val CARRERAS = listOf(
    "Ingeniería en Sistemas",
    "Ingeniería Industrial",
    "Administración de Empresas",
    "Contaduría"
)

/**
 * Pantalla 1: Formulario de Registro.
 *
 * Captura los cinco datos del Mini Proyecto 01 con los controles vistos en
 * las Prácticas 4 y 5 (OutlinedTextField, DropdownMenu, RadioButton y Switch).
 * Al registrar se persiste la información en SharedPreferences y se navega
 * a la pantalla de detalle enviando los datos como argumentos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(onRegister: (String, String, String, String, String) -> Unit) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    // Estados del formulario.
    var matricula by rememberSaveable { mutableStateOf("") }
    var nombre by rememberSaveable { mutableStateOf("") }
    var carrera by rememberSaveable { mutableStateOf("") }
    var turno by rememberSaveable { mutableStateOf("Matutino") }
    var estatus by rememberSaveable { mutableStateOf("Activo") }
    var carreraMenuExpanded by remember { mutableStateOf(false) }
    var matriculaError by rememberSaveable { mutableStateOf(false) }
    var nombreError by rememberSaveable { mutableStateOf(false) }
    var carreraError by rememberSaveable { mutableStateOf(false) }

    // Persistencia: al abrir la app se recuerda el último registro (Práctica 5).
    LaunchedEffect(Unit) {
        matricula = preferencesManager.getLastMatricula()
        nombre = preferencesManager.getLastNombre()
        carrera = preferencesManager.getLastCarrera()
        turno = preferencesManager.getLastTurno()
        estatus = preferencesManager.getLastEstatus()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sistema de Registro de Estudiantes",
                fontSize = 22.sp,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            HorizontalDivider()

            // Matrícula.
            OutlinedTextField(
                value = matricula,
                onValueChange = {
                    matricula = it
                    matriculaError = false
                },
                label = { Text("Matrícula") },
                singleLine = true,
                isError = matriculaError,
                supportingText = {
                    if (matriculaError) Text("Ingrese la matrícula")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Nombre completo.
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = false
                },
                label = { Text("Nombre completo") },
                singleLine = true,
                isError = nombreError,
                supportingText = {
                    if (nombreError) Text("Ingrese el nombre completo")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Carrera: menú desplegable (Exposed Dropdown Menu).
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = carrera,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Carrera") },
                    isError = carreraError,
                    supportingText = {
                        if (carreraError) Text("Seleccione una carrera")
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Desplegar opciones"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                DropdownMenu(
                    expanded = carreraMenuExpanded,
                    onDismissRequest = { carreraMenuExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    CARRERAS.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                carrera = opcion
                                carreraError = false
                                carreraMenuExpanded = false
                            }
                        )
                    }
                }
                // Capa transparente sobre el campo (que es de solo lectura)
                // para abrir el menú desplegable al tocarlo.
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { carreraMenuExpanded = true }
                )
            }

            // Turno: RadioButtons (Matutino / Vespertino).
            Text(
                text = "Turno",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = turno == "Matutino",
                    onClick = { turno = "Matutino" }
                )
                Text(
                    text = "Matutino",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 24.dp)
                )
                RadioButton(
                    selected = turno == "Vespertino",
                    onClick = { turno = "Vespertino" }
                )
                Text(
                    text = "Vespertino",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Estatus: Switch (Activo / Inactivo).
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estatus: $estatus",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = estatus == "Activo",
                    onCheckedChange = { estatus = if (it) "Activo" else "Inactivo" }
                )
            }

            // Botón Registrar: valida, persiste y navega al detalle.
            Button(
                onClick = {
                    matriculaError = matricula.isBlank()
                    nombreError = nombre.isBlank()
                    carreraError = carrera.isBlank()

                    if (!matriculaError && !nombreError && !carreraError) {
                        // Persistencia local (Práctica 5).
                        preferencesManager.saveRegistration(
                            matricula, nombre, carrera, turno, estatus
                        )
                        // Navegación con paso de parámetros (Práctica 3).
                        onRegister(matricula, nombre, carrera, turno, estatus)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
        }
    }
}
