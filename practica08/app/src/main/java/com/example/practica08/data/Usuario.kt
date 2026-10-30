package com.example.practica08.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Práctica 08 — SQLite / Room Database.
 *
 * @Entity define la estructura de la tabla en la base de datos local
 * SQLite. Es el equivalente a la sentencia CREATE TABLE: cada propiedad
 * de la data class corresponde a una columna de la tabla.
 *
 * La tabla se llama "usuarios" (misma que usa la consulta
 * "SELECT * FROM usuarios" del UsuarioDao).
 */
@Entity(tableName = "usuarios")
data class Usuario(
    // Clave primaria autogenerada: Room asigna el id en cada inserción.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Columnas de la tabla.
    val nombre: String
)
