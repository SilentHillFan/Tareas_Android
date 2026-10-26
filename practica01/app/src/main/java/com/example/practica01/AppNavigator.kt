package com.example.practica01

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * Controlador de navegación de la aplicación (Paso 11).
 *
 * startDestination = "first_screen": la app arranca en la primera pantalla.
 * La segunda ruta declara un argumento llamado "texto" de tipo StringType,
 * por lo que Navigation Compose sabe que espera una cadena y puede validarla
 * antes de entregarla a SecondScreen.
 *
 * Los argumentos viajan en el NavBackStackEntry (el historial de navegación)
 * y se recuperan con backStackEntry.arguments?.getString("texto").
 * El operador Elvis (?:) aporta una cadena vacía si el argumento no existe.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "first_screen"
    ) {
        // Pantalla 1 - FirstScreen
        composable("first_screen") {
            FirstScreen(onNavigateToSecondScreen = { texto ->
                navController.navigate("second_screen/$texto")
            })
        }

        // Pantalla 2 con argumento - SecondScreen
        composable(
            route = "second_screen/{texto}",
            arguments = listOf(navArgument("texto") { type = NavType.StringType })
        ) { backStackEntry ->
            val textoRecibido = backStackEntry.arguments?.getString("texto") ?: ""
            SecondScreen(
                mensaje = textoRecibido,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
