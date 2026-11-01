package com.example.miniproyecto03.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.miniproyecto03.data.Publicacion
import com.example.miniproyecto03.domain.AppDatabase
import com.example.miniproyecto03.domain.PublicacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado del formulario de alta (título, descripción, fecha y foto).
 */
data class FormularioState(
    val titulo: String = "",
    val descripcion: String = "",
    // Fecha en milisegundos: arranca en el día de hoy (se puede cambiar
    // con el selector de fecha del formulario).
    val fecha: Long = System.currentTimeMillis(),
    // Ruta de la foto elegida (cámara o galería); vacía si no hay foto.
    val fotoUri: String = ""
)

/**
 * Mini Proyecto 03 — Red Social Local.
 *
 * ViewModel de la arquitectura MVVM: conserva y expone el estado de la
 * UI (lista de publicaciones + formulario), sobrevive a los cambios de
 * configuración y desacopla la pantalla de los datos. Equivalente en la
 * arquitectura clásica a un Presenter/Repository observado con LiveData.
 *
 * @param application necesario para llegar al contexto de la app y abrir
 * la base de datos de Room (AndroidViewModel + Factory).
 */
class PublicacionViewModel(application: Application) : AndroidViewModel(application) {

    // Toda la lógica de datos pasa por el repositorio (capa MVVM).
    private val repository: PublicacionRepository

    // Lista reactiva de publicaciones expuesta a la UI como StateFlow
    // (equivalente a LiveData<List<Publicacion>> en XML).
    val publicaciones: StateFlow<List<Publicacion>>

    // Estado observable del formulario de alta.
    private val _formulario = MutableStateFlow(FormularioState())
    val formulario: StateFlow<FormularioState> = _formulario.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).publicacionDao()
        repository = PublicacionRepository(dao)
        publicaciones = repository.todas
            .stateIn(
                scope = viewModelScope,
                // Se mantiene activo mientras la UI lo observe: la app
                // está en primer plano con la pantalla abierta.
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    }

    // ---------------------------------------------------------- formulario

    fun onTituloCambiado(valor: String) {
        _formulario.value = _formulario.value.copy(titulo = valor)
    }

    fun onDescripcionCambiada(valor: String) {
        _formulario.value = _formulario.value.copy(descripcion = valor)
    }

    fun onFechaCambiada(valor: Long) {
        _formulario.value = _formulario.value.copy(fecha = valor)
    }

    fun onFotoCambiada(ruta: String) {
        _formulario.value = _formulario.value.copy(fotoUri = ruta)
    }

    /**
     * Guarda la publicación si el formulario es válido (título no vacío,
     * descripción no vacía y con foto) y limpia el formulario.
     *
     * @return true si se guardó; false si faltaban datos.
     */
    fun publicar(): Boolean {
        val estado = _formulario.value
        if (estado.titulo.isBlank() || estado.descripcion.isBlank() || estado.fotoUri.isEmpty()) {
            return false
        }
        viewModelScope.launch {
            repository.insertar(
                Publicacion(
                    titulo = estado.titulo.trim(),
                    descripcion = estado.descripcion.trim(),
                    fotoUri = estado.fotoUri,
                    fecha = estado.fecha
                )
            )
        }
        // Reinicia el formulario para la próxima publicación.
        _formulario.value = FormularioState()
        return true
    }

    /** Limpia el formulario sin publicar. */
    fun limpiarFormulario() {
        _formulario.value = FormularioState()
    }

    /** Elimina una publicación del listado. */
    fun eliminar(publicacion: Publicacion) {
        viewModelScope.launch {
            repository.eliminar(publicacion)
        }
    }

    /**
     * Factory: AndroidViewModel exige Application en el constructor, así
     * que ViewModelProvider.Factory lo inyecta desde la Activity.
     */
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PublicacionViewModel(application) as T
        }
    }
}
