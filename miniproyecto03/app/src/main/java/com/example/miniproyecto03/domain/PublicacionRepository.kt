package com.example.miniproyecto03.domain

import com.example.miniproyecto03.data.Publicacion
import com.example.miniproyecto03.data.PublicacionDao
import kotlinx.coroutines.flow.Flow

/**
 * Capa intermedia de la arquitectura MVVM: Repository.
 *
 * El Repository concentra el acceso a los datos (aquí Room) y expone
 * una interfaz sencilla al ViewModel, sin que la capa de presentación
 * conozca el DAO ni la base de datos. En una app mayor sería aquí donde
 * se decidiría si un dato viene de Room, de la red o de un caché.
 *
 * @param dao el DAO de la tabla publicaciones.
 */
class PublicacionRepository(private val dao: PublicacionDao) {

    // Listado reactivo: al cambiar la tabla, el Flow emite la lista
    // nueva y Compose redibuja el listado solo.
    val todas: Flow<List<Publicacion>> = dao.obtenerTodas()

    // Inserta una publicación en segundo plano (suspending).
    suspend fun insertar(publicacion: Publicacion) = dao.insertar(publicacion)

    // Elimina una publicación en segundo plano (suspending).
    suspend fun eliminar(publicacion: Publicacion) = dao.eliminar(publicacion)
}
