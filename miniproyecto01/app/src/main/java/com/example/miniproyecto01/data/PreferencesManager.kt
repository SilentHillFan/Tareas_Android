package com.example.miniproyecto01.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Persistencia local con SharedPreferences (Práctica 5) adaptada al
 * Mini Proyecto 01: guarda el último formulario registrado para que al
 * reabrir la aplicación se recuerde la última matrícula y su configuración.
 */
class PreferencesManager(context: Context) {

    // Archivo donde se almacenan los datos del registro.
    private val prefs: SharedPreferences =
        context.getSharedPreferences("student_registry", Context.MODE_PRIVATE)

    // Guarda el registro completo capturado en el formulario.
    fun saveRegistration(
        matricula: String,
        nombre: String,
        carrera: String,
        turno: String,
        estatus: String
    ) {
        prefs.edit()
            .putString("matricula", matricula)
            .putString("nombre", nombre)
            .putString("carrera", carrera)
            .putString("turno", turno)
            .putString("estatus", estatus)
            .apply()
    }

    // Devuelve la última matrícula registrada (vacía si no existe).
    fun getLastMatricula(): String = prefs.getString("matricula", "") ?: ""

    // Devuelve el último nombre registrado (vacío si no existe).
    fun getLastNombre(): String = prefs.getString("nombre", "") ?: ""

    // Devuelve la última carrera registrada (vacía si no existe).
    fun getLastCarrera(): String = prefs.getString("carrera", "") ?: ""

    // Devuelve el último turno registrado (Matutino por defecto).
    fun getLastTurno(): String = prefs.getString("turno", "Matutino") ?: "Matutino"

    // Devuelve el último estatus registrado (Activo por defecto).
    fun getLastEstatus(): String = prefs.getString("estatus", "Activo") ?: "Activo"
}
