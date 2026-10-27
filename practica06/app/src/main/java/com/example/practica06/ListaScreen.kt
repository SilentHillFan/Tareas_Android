package com.example.practica06

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Práctica 06 — LazyColumn y listas dinámicas.
 *
 * LazyColumn es el componente de Jetpack Compose que sustituye a
 * RecyclerView. Equivalencias con el patrón tradicional de vistas XML:
 *
 *  - LazyColumn        -> RecyclerView + LayoutManager (LinearLayoutManager).
 *  - items(...)        -> onCreateViewHolder / onBindViewHolder: por cada
 *                         elemento de la colección se invoca el Composable
 *                         que dibuja la celda.
 *  - Composable de ítem-> ViewHolder: define la vista individual y se
 *                         reutiliza (recomposición) solo para lo visible.
 *
 * Al renderizar de forma perezosa únicamente los elementos que aparecen en
 * pantalla, logra el mismo reciclado de vistas y la misma fluidez en el
 * scroll que se esperaba de un Adapter/ViewHolder bien implementado.
 */
@Composable
fun ListaScreen() {
    // Colección de datos dinámica (12 contactos de prueba).
    val contactos = obtenerContactosDummy()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Equivale al binding de cada posición del Adapter: por cada
        // contacto se compone un ContactoItem (la "celda").
        items(contactos) { contacto ->
            ContactoItem(contacto)
        }
    }
}

/**
 * Ítem individual de la lista: equivalente al ViewHolder de RecyclerView.
 * Diseñado con componentes Material Design (Card) y agrupado en una
 * Column con nombre y teléfono del contacto.
 */
@Composable
fun ContactoItem(contacto: Contacto) {
    // Contexto para el Toast del Reto. Se declara aquí, al inicio del
    // Composable, y no como parámetro de la Card.
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            // Reto: captura del evento de clic sobre el elemento de la
            // lista para mostrar el contacto seleccionado.
            .clickable {
                Toast.makeText(
                    context,
                    "Seleccionaste a ${contacto.nombre}",
                    Toast.LENGTH_SHORT
                ).show()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE3F2FD))
                .padding(16.dp)
        ) {
            Text(
                text = contacto.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tel: ${contacto.telefono}",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}
