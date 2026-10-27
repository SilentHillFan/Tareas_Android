package com.example.practica05.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Paso 3 del guion: PreferencesManager centraliza el uso de SharedPreferences.
 * Recibe el Context desde FormScreen y expone métodos simples para guardar y
 * leer las preferencias del usuario (nombre, modo oscuro y notificaciones).
 */
class PreferencesManager(context: Context) {

    // Nombre del archivo donde se almacenan las preferencias.
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    // Guarda el nombre de usuario.
    fun saveUsername(username: String) {
        prefs.edit().putString("username", username).apply()
    }

    // Devuelve el nombre de usuario guardado (vacío si no existe).
    fun getUsername(): String {
        return prefs.getString("username", "") ?: ""
    }

    // Guarda el estado del modo oscuro.
    fun saveDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("dark_mode", enabled).apply()
    }

    // Devuelve el estado del modo oscuro (desactivado por defecto).
    fun getDarkMode(): Boolean {
        return prefs.getBoolean("dark_mode", false)
    }

    // Guarda el estado de las notificaciones.
    fun saveNotifications(enabled: Boolean) {
        prefs.edit().putBoolean("notifications", enabled).apply()
    }

    // Devuelve el estado de las notificaciones (activadas por defecto).
    fun getNotifications(): Boolean {
        return prefs.getBoolean("notifications", true)
    }

    // Elimina todas las preferencias guardadas (botón "Borrar").
    fun clear() {
        prefs.edit().clear().apply()
    }
}
