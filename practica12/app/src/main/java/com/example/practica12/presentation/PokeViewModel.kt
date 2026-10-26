package com.example.practica12.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica12.data.NamedResource
import com.example.practica12.data.PokeRepository
import com.example.practica12.data.PokemonDetailResponse
import kotlinx.coroutines.launch

/**
 * PokeViewModel (MVVM).
 *
 * Toda la carga de datos ocurre dentro de [viewModelScope], es decir, en un hilo
 * secundario proporcionado por las corrutinas. Así el hilo principal de la UI
 * jamás se bloquea esperando la respuesta de internet.
 */
class PokeViewModel : ViewModel() {

    private val repository = PokeRepository()

    // Pantalla 1: categorías (tipos)
    var tipos = mutableStateOf<List<NamedResource>>(emptyList())

    // Pantalla 2: pokemones del tipo seleccionado
    var pokemonsPorTipo = mutableStateOf<List<NamedResource>>(emptyList())

    // Pantalla 3: detalle del pokemon seleccionado
    var pokemonDetalle = mutableStateOf<PokemonDetailResponse?>(null)

    // Retroalimentación para la interfaz: cargando / error
    var cargando = mutableStateOf(false)
    var error = mutableStateOf<String?>(null)

    init {
        // Cargar las categorías apenas se abra la app
        viewModelScope.launch {
            cargando.value = true
            try {
                tipos.value = repository.getTypes()
                error.value = null
            } catch (e: Exception) {
                error.value = "No se pudo conectar con la PokeAPI: ${e.message}"
            } finally {
                cargando.value = false
            }
        }
    }

    fun cargarPokemonsPorTipo(id: String) {
        viewModelScope.launch {
            cargando.value = true
            try {
                pokemonsPorTipo.value = repository.getPokemonsByType(id)
                error.value = null
            } catch (e: Exception) {
                error.value = "Error al cargar los pokemones: ${e.message}"
            } finally {
                cargando.value = false
            }
        }
    }

    fun cargarDetallePokemon(id: String) {
        pokemonDetalle.value = null // Limpiamos la vista anterior
        viewModelScope.launch {
            cargando.value = true
            try {
                pokemonDetalle.value = repository.getPokemonDetail(id)
                error.value = null
            } catch (e: Exception) {
                error.value = "Error al cargar el detalle: ${e.message}"
            } finally {
                cargando.value = false
            }
        }
    }
}
