package com.example.miniproyecto01

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.miniproyecto01.screens.DetalleScreen
import com.example.miniproyecto01.screens.FormScreen

/**
 * Mini Proyecto 01: Sistema de Registro de Estudiantes.
 *
 * MainActivity configura la navegación con Jetpack Navigation-Compose:
 *  - "registro":  Pantalla 1, formulario de registro.
 *  - "detalle":   Pantalla 2, confirmación/detalle con los datos recibidos
 *                 como argumentos de ruta.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "registro"
            ) {
                // Pantalla 1: formulario.
                composable("registro") {
                    FormScreen(
                        onRegister = { matricula, nombre, carrera, turno, estatus ->
                            // Se codifican los valores por si contienen espacios,
                            // acentos o caracteres especiales; la navegación los
                            // decodifica automáticamente al leer los argumentos.
                            val args = listOf(matricula, nombre, carrera, turno, estatus)
                                .joinToString("/") { Uri.encode(it) }
                            navController.navigate("detalle/$args")
                        }
                    )
                }

                // Pantalla 2: detalle con los cinco datos del formulario.
                composable(
                    route = "detalle/{matricula}/{nombre}/{carrera}/{turno}/{estatus}"
                ) { backStackEntry ->
                    DetalleScreen(
                        matricula = backStackEntry.arguments?.getString("matricula").orEmpty(),
                        nombre = backStackEntry.arguments?.getString("nombre").orEmpty(),
                        carrera = backStackEntry.arguments?.getString("carrera").orEmpty(),
                        turno = backStackEntry.arguments?.getString("turno").orEmpty(),
                        estatus = backStackEntry.arguments?.getString("estatus").orEmpty(),
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
