package com.example.miniproyecto03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.miniproyecto03.presentation.MainScreen
import com.example.miniproyecto03.ui.theme.miniproyecto03Theme

/**
 * Mini Proyecto 03 — Red Social Local.
 *
 * MainActivity solo aloja la pantalla principal: dentro de setContent
 * se aplica el tema y se invoca a MainScreen(), que integra el
 * formulario, el listado, la cámara/galería y Room.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            miniproyecto03Theme {
                MainScreen()
            }
        }
    }
}
