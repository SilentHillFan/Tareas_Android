package com.example.practica12.data

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz del servicio web.
 * Retrofit convierte cada método anotado con @GET en una petición HTTP real
 * y devuelve la respuesta ya convertida a nuestros Data Classes (via Gson).
 *
 * Documentación de los recursos: https://pokeapi.co/docs/v2
 */
interface PokeApi {

    // Lista de todos los tipos de Pokémon
    @GET("type")
    suspend fun getTypes(): TypeListResponse

    // Pokémon que pertenecen a un tipo concreto
    @GET("type/{id}")
    suspend fun getPokemonsByType(@Path("id") id: String): TypeDetailResponse

    // Detalle de un Pokémon (stats, tipos, medidas)
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: String): PokemonDetailResponse
}
