package com.example.practica08.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practica08.data.Usuario
import com.example.practica08.data.UsuarioDao

/**
 * Punto de acceso principal a la conexión subyacente con SQLite.
 *
 * @Database indica las entidades (tablas) y la versión del esquema;
 * Room genera la implementación en tiempo de compilación. El patrón
 * Singleton evita abrir múltiples instancias de la base de datos al
 * mismo tiempo (equivalente al patrón de SQLiteOpenHelper).
 */
@Database(entities = [Usuario::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Acceso a las operaciones CRUD de la tabla usuarios.
    abstract fun usuarioDao(): UsuarioDao

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
                    "mi_base_datos"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
