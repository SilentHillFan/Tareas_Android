package com.example.miniproyecto01.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla 2: Confirmación / Detalle.
 *
 * Recibe los cinco datos del formulario como argumentos de la ruta de
 * navegación y los despliega en pantalla, con un botón para volver al
 * formulario.
 */
@Composable
fun DetalleScreen(
    matricula: String,
    nombre: String,
    carrera: String,
    turno: String,
    estatus: String,
    onBack: () -> Unit
) {
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
                text = "Registro Confirmado",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            HorizontalDivider()

            // Fila con etiqueta y valor del dato recibido.
            DatoDetalle(etiqueta = "Matrícula", valor = matricula)
            DatoDetalle(etiqueta = "Nombre completo", valor = nombre)
            DatoDetalle(etiqueta = "Carrera", valor = carrera)
            DatoDetalle(etiqueta = "Turno", valor = turno)
            DatoDetalle(etiqueta = "Estatus", valor = estatus)

            HorizontalDivider()

            Text(
                text = "Datos recibidos desde el formulario mediante argumentos de navegación.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al formulario")
            }
        }
    }
}

@Composable
private fun DatoDetalle(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.6f)
        )
    }
}
