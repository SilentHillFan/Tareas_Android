package com.example.practica04

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica04.components.CustomCheckbox
import com.example.practica04.components.CustomDatePicker
import com.example.practica04.components.CustomRadioButton
import com.example.practica04.components.CustomSpinner
import com.example.practica04.components.CustomSwitch

/**
 * Paso 4 y 10: pantalla principal del formulario.
 *
 * Integra los cinco controles creados en el package components. Aquí vive el
 * estado de cada control, de modo que el botón de procesamiento pueda
 * recopilar todos los valores y desplegarlos de forma resumida.
 */
@Composable
fun FormScreen() {

    // Estado de los 5 controles (elevado desde cada componente)
    var switchOn by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Opción 1") }
    var isChecked by remember { mutableStateOf(false) }
    var spinnerText by remember { mutableStateOf("Seleccionar Opción") }
    var selectedDateText by remember { mutableStateOf("Seleccionar Fecha") }

    // Texto consolidado que muestra el botón de procesamiento
    var resumen by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            // Insets del sistema: sin ellos el título queda bajo la barra de
            // estado y el botón final bajo la barra de navegación.
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Práctica 4: Componentes Avanzados",
            fontSize = 22.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider()

        // 1. Switch
        Text("1. Switch", style = MaterialTheme.typography.titleMedium)
        CustomSwitch(
            isChecked = switchOn,
            onCheckedChange = { switchOn = it }
        )

        HorizontalDivider()

        // 2. RadioButton
        Text("2. RadioButton", style = MaterialTheme.typography.titleMedium)
        CustomRadioButton(
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it }
        )

        HorizontalDivider()

        // 3. Checkbox
        Text("3. Checkbox", style = MaterialTheme.typography.titleMedium)
        CustomCheckbox(
            isChecked = isChecked,
            onCheckedChange = { isChecked = it }
        )

        HorizontalDivider()

        // 4. Spinner
        Text(
            "4. Spinner (DropdownMenu)",
            style = MaterialTheme.typography.titleMedium
        )
        CustomSpinner(
            selectedText = spinnerText,
            onOptionSelected = { spinnerText = it }
        )

        HorizontalDivider()

        // 5. DatePicker
        Text("5. DatePicker", style = MaterialTheme.typography.titleMedium)
        CustomDatePicker(
            selectedDateText = selectedDateText,
            onDateSelected = { selectedDateText = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de procesamiento: recopila la información de los 5 controles
        // y la despliega en un contenedor de texto (consigna de la práctica).
        Button(
            onClick = {
                resumen = "Switch: ${if (switchOn) "Activado" else "Desactivado"}\n" +
                        "RadioButton: $selectedOption\n" +
                        "CheckBox: ${if (isChecked) "Aceptado" else "No aceptado"}\n" +
                        "Spinner: $spinnerText\n" +
                        "DatePicker: $selectedDateText"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Probar Formulario")
        }

        if (resumen.isNotEmpty()) {
            Text(
                text = "Resumen del formulario:\n$resumen",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
