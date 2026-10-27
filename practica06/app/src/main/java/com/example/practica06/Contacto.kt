package com.example.practica06

/**
 * Modelo de datos de la Práctica 06: representa la información de un
 * contacto individual dentro de la lista dinámica.
 *
 * Es el equivalente al modelo que se mapearía en el onBindViewHolder()
 * de un Adapter de RecyclerView tradicional: cada propiedad corresponde
 * a un dato que se vincula con una vista de la celda.
 */
data class Contacto(
    val id: Int,
    val nombre: String,
    val telefono: String
)

// Función para simular una base de datos o API devolviendo una lista.
// Equivale a la fuente de datos que alimentaría el Adapter de un
// RecyclerView: aquí la colección se construye en memoria y se entrega
// completa a LazyColumn.
fun obtenerContactosDummy(): List<Contacto> {
    return listOf(
        Contacto(1, "Ana García", "555-1001"),
        Contacto(2, "Luis Pérez", "555-1002"),
        Contacto(3, "María López", "555-1003"),
        Contacto(4, "Carlos Ramírez", "555-1004"),
        Contacto(5, "Sofía Torres", "555-1005"),
        Contacto(6, "Jorge Medina", "555-1006"),
        Contacto(7, "Laura Salazar", "555-1007"),
        Contacto(8, "Pedro Castillo", "555-1008"),
        Contacto(9, "Elena Rojas", "555-1009"),
        Contacto(10, "Diego Castro", "555-1010"),
        Contacto(11, "Valeria Silva", "555-1011"),
        Contacto(12, "Ricardo Soto", "555-1012")
    )
}
