package com.example.miniproyecto02.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object: define las operaciones CRUD sobre la tabla "tareas".
 * Cada anotación corresponde a una sentencia SQL (INSERT, SELECT, UPDATE,
 * DELETE); Room genera la implementación en tiempo de compilación.
 *
 * Al devolver un Flow, Compose sabrá cuándo actualizar la UI
 * automáticamente (lectura reactiva). Las operaciones de escritura se
 * declaran con suspend para ejecutarse en segundo plano (Corrutinas).
 */
@Dao
interface TareaDao {

    // SELECT: consulta reactiva de todas las tareas (equivalente a
    // rawQuery("SELECT * FROM tareas") con un Cursor/Observable).
    @Query("SELECT * FROM tareas")
    fun obtenerTodas(): Flow<List<Tarea>>

    // INSERT: registra una tarea nueva
    // (INSERT INTO tareas (titulo, completada, fechaCreacion) VALUES ...).
    @Insert
    suspend fun insertar(tarea: Tarea)

    // UPDATE: opera dos usos del CRUD — marcar/desmarcar como completada
    // y editar el título; Room actualiza la fila por la clave primaria.
    @Update
    suspend fun actualizar(tarea: Tarea)

    // DELETE: elimina una tarea de la tabla (por clave primaria).
    @Delete
    suspend fun eliminar(tarea: Tarea)

    // DELETE condicional: limpia todas las tareas completadas.
    // Es la opción "Borrar completadas" del menú de la ToolBar.
    @Query("DELETE FROM tareas WHERE completada = 1")
    suspend fun borrarCompletadas()
}
