package com.example.practica02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Actividad principal (Paso 6 del guion).
 *
 * El setContent muestra directamente CalculatorScreen(), declarada en
 * PantallaCalc.kt. No se envuelve en el tema porque la calculadora define
 * su propia paleta de colores (AppBackground, CalcBackground, DarkScreen y
 * ButtonColor) directamente en su composable.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorScreen()
        }
    }
}
