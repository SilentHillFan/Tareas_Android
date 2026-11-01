package com.example.miniproyecto03.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Mini Proyecto 03 — Red Social Local.
 *
 * @Entity define la tabla "publicaciones" de la base de datos local
 * SQLite. Cada propiedad es una columna: los cuatro campos exigidos por
 * la consigna (título, descripción, fotografía y fecha).
 */
@Entity(tableName = "publicaciones")
data class Publicacion(
    // Clave primaria autogenerada: Room asigna el id en cada alta.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Título de la publicación.
    val titulo: String,
    // Descripción o texto de la publicación.
    val descripcion: String,
    // Ruta del archivo de la foto dentro del almacenamiento interno de la
    // app (guardada vía FileProvider o copiada desde la galería).
    val fotoUri: String,
    // Fecha en milisegundos (epoch): se guarda como Long para poder
    // ordenar el listado y se formatea en la interfaz (dd/MM/yyyy).
    val fecha: Long
)
