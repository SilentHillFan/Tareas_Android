package com.example.practica12

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practica12.presentation.PokeViewModel
import com.example.practica12.presentation.PokemonDetailScreen
import com.example.practica12.presentation.PokemonsByTypeScreen
import com.example.practica12.presentation.TiposScreen

/**
 * Actividad principal: enlaza las 3 pantallas de la práctica con un NavHost.
 *
 *  "tipos"        -> lista de categorías
 *  "pokemons/{id}" -> pokemones de una categoría
 *  "detalle/{id}"  -> carta TCG con el detalle del Pokémon
 *
 * El PokeViewModel se crea aquí (con `by viewModels`) para que sobreviva
 * a los cambios de pantalla y no repita peticiones innecesarias.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: PokeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "tipos",
                modifier = Modifier.systemBarsPadding()
            ) {
                composable("tipos") {
                    TiposScreen(viewModel) { id ->
                        navController.navigate("pokemons/$id")
                    }
                }
                composable("pokemons/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id") ?: ""
                    PokemonsByTypeScreen(viewModel, id) { pokeId ->
                        navController.navigate("detalle/$pokeId")
                    }
                }
                composable("detalle/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id") ?: ""
                    PokemonDetailScreen(viewModel, id)
                }
            }
        }
    }
}
