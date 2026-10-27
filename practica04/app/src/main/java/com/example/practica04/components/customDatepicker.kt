package com.example.practica04.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Paso 6: selector de fecha con el calendario nativo de Material 3.
 *
 * El estado del diálogo queda dentro del componente, pero la fecha elegida se
 * notifica hacia FormScreen con el formato "dd/mm/yyyy" para que pueda
 * mostrarse en el resumen del formulario.
 *
 * @param selectedDateText fecha formateada o el texto por defecto.
 * @param onDateSelected notifica la fecha elegida en formato dd/MM/yyyy.
 */
@Composable
fun CustomDatePicker(
    selectedDateText: String,
    onDateSelected: (String) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Button(
        onClick = { openDialog = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(selectedDateText)
    }

    if (openDialog) {
        DatePickerDialog(
            onDismissRequest = { openDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        formatter.timeZone = TimeZone.getTimeZone("UTC")
                        onDateSelected(formatter.format(Date(millis)))
                    }
                    openDialog = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
