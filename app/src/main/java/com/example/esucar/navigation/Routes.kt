package com.example.esucar.navigation

//archivo con las rutas de las pantallas secundarios
sealed class SubDestination(val route: String) {
    //ruta para detalle de asignatura
    object DetalleAsignatura : SubDestination("detalle_asignatura/{asignaturaId}") {
        fun createRoute(asignaturaId: Int) = "detalle_asignatura/$asignaturaId"
    }
}