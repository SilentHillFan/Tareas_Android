package com.example.practica12.data

/**
 * Modelo genérico que usa la PokeAPI para referenciar otros recursos:
 * un nombre y la URL donde vive el recurso completo.
 */
data class NamedResource(val name: String, val url: String) {
    // Extraemos el identificador numérico de la URL
    // ej: https://pokeapi.co/api/v2/type/12/ -> "12"
    val id: String
        get() = url.trimEnd('/').substringAfterLast('/')
}

// Pantalla 1: Lista de Categorías (Tipos)
data class TypeListResponse(val results: List<NamedResource>)

// Pantalla 2: Trae los pokemones de la categoría seleccionada
data class TypeDetailResponse(val pokemon: List<PokemonSlot>)
data class PokemonSlot(val pokemon: NamedResource)

// Pantalla 3: Detalle del Pokémon seleccionado
data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val stats: List<StatSlot>,
    val types: List<TypeSlot>
)

data class StatSlot(val base_stat: Int, val stat: NamedResource)
data class TypeSlot(val type: NamedResource)
