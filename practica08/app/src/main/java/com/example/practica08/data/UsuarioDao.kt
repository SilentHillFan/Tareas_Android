package com.example.practica08.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object: define los métodos para acceder a la base de datos.
 * Cada anotación corresponde a una sentencia SQL (INSERT, SELECT, UPDATE,
 * DELETE); Room genera la implementación en tiempo de compilación.
 *
 * Al devolver un Flow, Compose sabrá cuándo actualizar la UI
 * automáticamente (lectura reactiva). Las operaciones de escritura se
 * ejecutan en segundo plano (Corrutinas).
 */
@Dao
interface UsuarioDao {

    // SELECT: consulta reactiva de todos los registros (equivalente a
    // rawQuery("SELECT * FROM usuarios") con un Cursor/Observable).
    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): Flow<List<Usuario>>

    // INSERT: equivalente a "INSERT INTO usuarios (nombre) VALUES (...)".
    @Insert
    fun insertar(usuario: Usuario)

    // UPDATE: parte del CRUD de la consigna; actualiza un registro
    // existente (Room identifica la fila por la clave primaria).
    @Update
    fun actualizar(usuario: Usuario)

    // RETO: DELETE: elimina un registro de la tabla. Se declara con
    // suspend para ejecutarse fuera del hilo principal (Corrutina).
    @Delete
    suspend fun eliminar(usuario: Usuario)
}
