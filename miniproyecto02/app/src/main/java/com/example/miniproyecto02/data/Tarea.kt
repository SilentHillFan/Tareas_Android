package com.example.miniproyecto02.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Mini Proyecto 02 — Administrador de Tareas Locales.
 *
 * @Entity define la tabla "tareas" de la base de datos local SQLite.
 * Es el equivalente a la sentencia CREATE TABLE: cada propiedad de la
 * data class corresponde a una columna de la tabla.
 */
@Entity(tableName = "tareas")
data class Tarea(
    // Clave primaria autogenerada: Room asigna el id en cada inserción.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Título descriptivo de la tarea.
    val titulo: String,
    // Estado de la tarea (permite marcar como completada y filtrar).
    val completada: Boolean = false,
    // Marca de tiempo de creación: permite ordenar por fecha.
    val fechaCreacion: Long = System.currentTimeMillis()
)
