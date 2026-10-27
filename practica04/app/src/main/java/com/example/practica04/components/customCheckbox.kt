package com.example.practica04.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

/**
 * Paso 5: casilla de verificación clásica (la que va con palomita).
 *
 * El estado (isChecked) vive en FormScreen para que la pantalla principal
 * pueda recopilarlo al pulsar el botón de procesamiento; aquí solo se
 * despliega y se notifica el cambio con onCheckedChange.
 *
 * @param isChecked estado actual de la casilla.
 * @param onCheckedChange invierte el valor entre true y false en cada toque.
 */
@Composable
fun CustomCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
        Text("Acepto los términos y condiciones")
    }
}
