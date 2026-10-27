package com.example.practica04.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

/**
 * Paso 9: interruptor de activación.
 *
 * @param isChecked estado actual del Switch.
 * @param onCheckedChange invierte el valor entre true y false en cada toque.
 */
@Composable
fun CustomSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Activar opción:", fontSize = 16.sp)
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}
