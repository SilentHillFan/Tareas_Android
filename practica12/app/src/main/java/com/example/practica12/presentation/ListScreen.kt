package com.example.practica12.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Pantalla 1: lista dinámica de categorías (Tipos) traídas desde la API.
 */
@Composable
fun TiposScreen(viewModel: PokeViewModel, onTypeClick: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.tipos.value) { tipo ->
                ListItemPoke(
                    texto = tipo.name.uppercase(),
                    onClick = { onTypeClick(tipo.id) }
                )
            }
        }
        CargandoOError(viewModel)
    }
}

/**
 * Pantalla 2: lista dinámica de Pokémon pertenecientes al tipo seleccionado.
 * [LaunchedEffect] dispara la petición asíncrona cada vez que cambia el tipo.
 */
@Composable
fun PokemonsByTypeScreen(
    viewModel: PokeViewModel,
    typeId: String,
    onPokeClick: (String) -> Unit
) {
    LaunchedEffect(typeId) { viewModel.cargarPokemonsPorTipo(typeId) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.pokemonsPorTipo.value) { poke ->
                ListItemPoke(
                    texto = "#${poke.id} - ${poke.name.uppercase()}",
                    onClick = { onPokeClick(poke.id) }
                )
            }
        }
        CargandoOError(viewModel)
    }
}

/**
 * Fila reutilizable de las dos listas, con una Pokébola dibujada
 * únicamente con primitivas de Compose (Box + CircleShape).
 */
@Composable
fun ListItemPoke(texto: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pokébola simulada
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.Red, CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.White, CircleShape)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = texto, style = MaterialTheme.typography.titleMedium)
        }
    }
}

/**
 * Indicador de carga y mensaje de error: da retroalimentación al usuario
 * mientras la petición HTTP está en curso o si falló.
 * Se declara como extensión de [BoxScope] para poder centrarse dentro del Box.
 */
@Composable
fun BoxScope.CargandoOError(viewModel: PokeViewModel) {
    when {
        viewModel.cargando.value -> {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        viewModel.error.value != null -> {
            Text(
                text = viewModel.error.value.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
            )
        }
    }
}
