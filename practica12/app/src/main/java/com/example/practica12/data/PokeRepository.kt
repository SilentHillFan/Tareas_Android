package com.example.practica12.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Instancia única (singleton) de Retrofit.
 * GsonConverterFactory es el encargado de transformar el JSON
 * que devuelve la PokeAPI en nuestras Data Classes.
 */
object RetrofitClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val pokeApi: PokeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApi::class.java)
    }
}

/**
 * Repositorio: única puerta de entrada a los datos.
 * La UI nunca habla directamente con Retrofit, siempre con esta clase.
 */
class PokeRepository {
    private val api = RetrofitClient.pokeApi

    suspend fun getTypes(): List<NamedResource> = api.getTypes().results

    suspend fun getPokemonsByType(id: String): List<NamedResource> =
        api.getPokemonsByType(id).pokemon.map { it.pokemon }

    suspend fun getPokemonDetail(id: String): PokemonDetailResponse =
        api.getPokemonDetail(id)
}
