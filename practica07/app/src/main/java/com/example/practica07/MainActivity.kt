package com.example.practica07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Práctica 07 — Menú, ToolBar y Material Design.
 *
 * MainActivity solo aloja la pantalla principal: dentro de setContent se
 * invoca a MainScreen(), que contiene el Scaffold con la TopAppBar, el
 * menú desplegable y el FAB.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}
