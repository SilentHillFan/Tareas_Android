package com.example.practica04.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Paso 7: opciones exclusivas (equivalente a un RadioGroup con RadioButtons).
 *
 * El estado seleccionado se recibe desde FormScreen y cada interacción se
 * notifica con onOptionSelected, de modo que la pantalla principal pueda
 * mostrar la opción elegida en el resumen.
 *
 * @param selectedOption valor de la opción marcada ("Opción 1" u "Opción 2").
 * @param onOptionSelected notifica la opción elegida al pulsarla.
 */
@Composable
fun CustomRadioButton(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = (selectedOption == "Opción 1"),
            onClick = { onOptionSelected("Opción 1") }
        )
        Text(
            text = "Opción 1",
            modifier = Modifier.clickable { onOptionSelected("Opción 1") }
        )

        Spacer(modifier = Modifier.width(16.dp))

        RadioButton(
            selected = (selectedOption == "Opción 2"),
            onClick = { onOptionSelected("Opción 2") }
        )
        Text(
            text = "Opción 2",
            modifier = Modifier.clickable { onOptionSelected("Opción 2") }
        )
    }
}
