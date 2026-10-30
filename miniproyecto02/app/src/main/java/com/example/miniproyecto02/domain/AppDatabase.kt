package com.example.miniproyecto02.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.miniproyecto02.data.Tarea
import com.example.miniproyecto02.data.TareaDao

/**
 * Punto de acceso principal a la conexión subyacente con SQLite.
 *
 * @Database indica las entidades (tablas) y la versión del esquema;
 * Room genera la implementación en tiempo de compilación. El patrón
 * Singleton evita abrir múltiples instancias de la base de datos al
 * mismo tiempo (equivalente al patrón de SQLiteOpenHelper).
 */
@Database(entities = [Tarea::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Acceso a las operaciones CRUD de la tabla tareas.
    abstract fun tareaDao(): TareaDao

    companion object {
        // Única instancia de la base de datos (Singleton).
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Si ya existe una instancia, se devuelve; si no, se crea
            // de forma sincronizada para evitar condiciones de carrera.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tareas_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
