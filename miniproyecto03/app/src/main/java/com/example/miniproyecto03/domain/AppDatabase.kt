package com.example.miniproyecto03.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.miniproyecto03.data.Publicacion
import com.example.miniproyecto03.data.PublicacionDao

/**
 * Mini Proyecto 03 — Red Social Local.
 *
 * Punto de acceso principal a la conexión subyacente con SQLite.
 *
 * @Database indica las entidades (tablas) y la versión del esquema;
 * Room genera la implementación en tiempo de compilación. El patrón
 * Singleton evita abrir múltiples instancias de la base de datos al
 * mismo tiempo (equivalente al patrón de SQLiteOpenHelper).
 */
@Database(entities = [Publicacion::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Acceso a las operaciones CRUD de la tabla publicaciones.
    abstract fun publicacionDao(): PublicacionDao

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
                    "publicaciones_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
