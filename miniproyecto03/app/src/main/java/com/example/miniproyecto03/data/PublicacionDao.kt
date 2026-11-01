package com.example.miniproyecto03.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object: define las operaciones sobre la tabla
 * "publicaciones". Room genera la implementación en tiempo de
 * compilación a partir de las sentencias SQL de cada anotación.
 *
 * Al devolver un Flow, Compose se entera de los cambios y redibuja el
 * listado automáticamente (lectura reactiva). Las escrituras se
 * declaran con suspend para ejecutarse en segundo plano (Corrutinas).
 */
@Dao
interface PublicacionDao {

    // SELECT: consulta reactiva de todas las publicaciones, de la más
    // reciente a la más antigua (equivalente a rawQuery con Cursor).
    @Query("SELECT * FROM publicaciones ORDER BY fecha DESC")
    fun obtenerTodas(): Flow<List<Publicacion>>

    // INSERT: registra una publicación nueva.
    @Insert
    suspend fun insertar(publicacion: Publicacion)

    // DELETE: elimina la publicación (por clave primaria).
    @Delete
    suspend fun eliminar(publicacion: Publicacion)
}
