package com.example.practica01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Primera pantalla: campo de texto + botón.
 *
 * @remember conserva el valor entre recomposiciones: sin él, Compose
 * reconstruiría la UI y textoIngresado volvería a "" en cada redibujado.
 * @mutableStateOf convierte la variable en un estado observable: cuando cambia,
 * Compose vuelve a dibujar solo los composable que la usan.
 *
 * @param onNavigateToSecondScreen función de navegación que recibe el texto
 * escrito por el usuario y lanza la SecondScreen.
 */
@Composable
fun FirstScreen(onNavigateToSecondScreen: (String) -> Unit) {
    var textoIngresado by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFFE3F2FD)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = textoIngresado,
                onValueChange = { textoIngresado = it },
                label = { Text("Escribe tu texto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    // Solo navegamos si el usuario escribió algo
                    if (textoIngresado.isNotBlank()) {
                        onNavigateToSecondScreen(textoIngresado)
                    }
                }
            ) {
                Text("Ir a SecondScreen")
            }
        }
    }
}
