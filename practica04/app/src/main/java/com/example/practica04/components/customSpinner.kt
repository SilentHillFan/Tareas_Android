package com.example.practica04.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

/**
 * Paso 8: Spinner implementado con un OutlinedTextField de solo lectura y un
 * DropdownMenu que se despliega desde el icono de flecha.
 *
 * El estado interno "expanded" controla si el menú está abierto; la opción
 * elegida se recibe y se notifica hacia FormScreen para poder mostrarla en el
 * resumen del formulario.
 *
 * @param selectedText texto actualmente seleccionado en el Spinner.
 * @param onOptionSelected notifica la opción elegida en el menú.
 */
@Composable
fun CustomSpinner(
    selectedText: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Elemento A") },
                onClick = {
                    onOptionSelected("Elemento A")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Elemento B") },
                onClick = {
                    onOptionSelected("Elemento B")
                    expanded = false
                }
            )
        }
    }
}
