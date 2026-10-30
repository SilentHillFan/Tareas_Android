package com.example.miniproyecto02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.miniproyecto02.presentation.MainScreen

/**
 * Mini Proyecto 02 — Administrador de Tareas Locales.
 *
 * MainActivity solo aloja la pantalla principal: dentro de setContent se
 * invoca a MainScreen(), que integra ToolBar, menús, LazyColumn y Room.
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
