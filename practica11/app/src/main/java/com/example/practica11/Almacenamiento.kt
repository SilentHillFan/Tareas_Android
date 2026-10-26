package com.example.practica11

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

/** Destino donde se guardará / leerá el archivo de texto. */
enum class DestinoAlmacenamiento(val etiqueta: String) {
    INTERNO("Memoria interna"),
    EXTERNO("Externa / SDCard")
}

/** Resultado de una operación de lectura/escritura, listo para mostrarlo en la interfaz. */
data class ResultadoOperacion(
    val exito: Boolean,
    val ruta: String,
    val detalle: String,
    val contenido: String = ""
)

/**
 * Capa de acceso a archivos de la Práctica 11.
 *
 * Utiliza [File], [FileOutputStream] y [FileInputStream] (junto con [BufferedReader]) para
 * leer y escribir archivos de texto tanto en la memoria interna como en el almacenamiento
 * externo, e informa qué permiso de almacenamiento corresponde solicitar en tiempo de
 * ejecución según la versión de Android.
 */
object Almacenamiento {

    /** Carpeta pública que se usa en Android 9 o anterior (donde sí se necesita permiso). */
    private const val CARPETA_PUBLICA = "Practica11"

    /**
     * Permiso que debe solicitarse en tiempo de ejecución.
     *  - Android 13+ : READ_MEDIA_AUDIO  (reemplaza a READ_EXTERNAL_STORAGE)
     *  - Android 10-12: READ_EXTERNAL_STORAGE
     *  - Android 9-  : WRITE_EXTERNAL_STORAGE
     */
    fun permisoRequerido(): String = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            android.Manifest.permission.READ_MEDIA_AUDIO

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
            android.Manifest.permission.READ_EXTERNAL_STORAGE

        else -> android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    /** Nombre legible del permiso actual, para mostrarlo en la interfaz. */
    fun nombrePermisoRequerido(): String = when (permisoRequerido()) {
        android.Manifest.permission.READ_MEDIA_AUDIO -> "READ_MEDIA_AUDIO (Android 13+)"
        android.Manifest.permission.READ_EXTERNAL_STORAGE -> "READ_EXTERNAL_STORAGE (Android 10-12)"
        else -> "WRITE_EXTERNAL_STORAGE (Android 9 o anterior)"
    }

    /** Indica si el permiso de almacenamiento ya fue concedido. */
    fun permisoConcedido(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, permisoRequerido()) ==
            PackageManager.PERMISSION_GRANTED

    /**
     * Carpeta real donde se va a trabajar según el destino elegido.
     *
     *  - INTERNO : directorio privado de la app (/data/data/<paquete>/files).
     *  - EXTERNO: en Android 9 o inferior se usa el almacenamiento público (Documentos/Practica11);
     *              a partir de Android 10 (Scoped Storage) la app escribe en su directorio
     *              externo propio (getExternalFilesDir), que es la tarjeta SD / almacenamiento
     *              externo sin necesidad de permisos adicionales.
     */
    private fun carpetaDestino(context: Context, destino: DestinoAlmacenamiento): File {
        if (destino == DestinoAlmacenamiento.INTERNO) {
            return context.filesDir
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.getExternalFilesDir(null) ?: context.filesDir
        } else {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                CARPETA_PUBLICA
            ).apply { mkdirs() }
        }
    }

    /** Evita rutas vacías o con caracteres no permitidos. */
    private fun nombreValido(nombre: String): String {
        val limpio = nombre.trim().ifEmpty { "archivo.txt" }
        return if (limpio.endsWith(".txt")) limpio else "$limpio.txt"
    }

    /**
     * Escribe el texto en el archivo indicado usando [FileOutputStream].
     */
    fun guardar(
        context: Context,
        destino: DestinoAlmacenamiento,
        nombre: String,
        texto: String
    ): ResultadoOperacion {
        return try {
            val carpeta = carpetaDestino(context, destino)
            if (!carpeta.exists()) carpeta.mkdirs()

            val archivo = File(carpeta, nombreValido(nombre))

            // Escritura con FileOutputStream; use {} cierra el flujo automáticamente
            FileOutputStream(archivo).use { flujo ->
                flujo.write(texto.toByteArray(Charsets.UTF_8))
                flujo.flush()
            }

            ResultadoOperacion(
                exito = true,
                ruta = archivo.absolutePath,
                detalle = "Archivo guardado correctamente (${archivo.length()} bytes)"
            )
        } catch (error: SecurityException) {
            ResultadoOperacion(
                exito = false,
                ruta = "",
                detalle = "Permiso denegado: concedé el permiso de almacenamiento o usá Memoria interna."
            )
        } catch (error: IOException) {
            ResultadoOperacion(
                exito = false,
                ruta = "",
                detalle = "Error al guardar: ${error.message}"
            )
        }
    }

    /**
     * Lee el archivo indicado usando [FileInputStream] y [BufferedReader].
     */
    fun leer(
        context: Context,
        destino: DestinoAlmacenamiento,
        nombre: String
    ): ResultadoOperacion {
        val archivo = File(carpetaDestino(context, destino), nombreValido(nombre))

        if (!archivo.exists() || !archivo.isFile) {
            return ResultadoOperacion(
                exito = false,
                ruta = archivo.absolutePath,
                detalle = "El archivo no existe todavía en ese destino."
            )
        }

        return try {
            val contenido = FileInputStream(archivo).use { flujo ->
                BufferedReader(InputStreamReader(flujo, Charsets.UTF_8)).readText()
            }

            ResultadoOperacion(
                exito = true,
                ruta = archivo.absolutePath,
                detalle = "Lectura correcta (${archivo.length()} bytes)",
                contenido = contenido
            )
        } catch (error: SecurityException) {
            ResultadoOperacion(
                exito = false,
                ruta = archivo.absolutePath,
                detalle = "Permiso denegado para leer el archivo."
            )
        } catch (error: IOException) {
            ResultadoOperacion(
                exito = false,
                ruta = archivo.absolutePath,
                detalle = "Error al leer: ${error.message}"
            )
        }
    }

    /**
     * Lista los archivos .txt existentes en el destino seleccionado.
     */
    fun listar(context: Context, destino: DestinoAlmacenamiento): ResultadoOperacion {
        val carpeta = carpetaDestino(context, destino)
        val archivos = carpeta.listFiles { archivo -> archivo.isFile && archivo.name.endsWith(".txt") }
            ?: emptyArray()

        return if (archivos.isEmpty()) {
            ResultadoOperacion(
                exito = false,
                ruta = carpeta.absolutePath,
                detalle = "No hay archivos .txt en esta carpeta."
            )
        } else {
            ResultadoOperacion(
                exito = true,
                ruta = carpeta.absolutePath,
                detalle = "${archivos.size} archivo(s) encontrado(s)",
                contenido = archivos.joinToString("\n") { "${it.name}  (${it.length()} bytes)" }
            )
        }
    }
}
