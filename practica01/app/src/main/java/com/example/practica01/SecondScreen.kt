package com.example.practica01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Segunda pantalla: recibe el mensaje capturado en FirstScreen y lo muestra.
 * No tiene campo de entrada ni botón de acción: solo presenta información.
 *
 * El color de fondo es distinto al de la primera pantalla a propósito,
 * para que se note visualmente que son dos pantallas diferentes.
 *
 * RETO: se agregó el TopAppBar con el botón de retorno, para no depender
 * únicamente del botón físico del dispositivo.
 *
 * @param mensaje texto enviado desde FirstScreen a través del NavHost.
 * @param onBack vuelve a la pantalla anterior (popBackStack).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(mensaje: String, onBack: () -> Unit) {
    Scaffold(
        containerColor = Color(0xFFF3E5F5),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Segunda Pantalla",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    // RETO: botón de retorno dentro de la barra superior
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a la pantalla anterior"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Texto recibido:",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = mensaje,
                fontSize = 24.sp
            )
        }
    }
}
