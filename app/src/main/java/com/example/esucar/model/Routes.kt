package com.example.esucar.model


//archivo con las rutas de las pantallas secundarios
sealed class SubDestination(val route: String) {
    //ruta para detalle de asignatura
    object DetalleAsignatura : SubDestination("detalle_asignatura/{asignaturaId}") {
        fun createRoute(asignaturaId: Int) = "detalle_asignatura/$asignaturaId"
    }

    //ruta formulario
    object FormularioConsulta : SubDestination("formulario_consulta/{asignaturaId}"){
        fun createRoute(asignaturaId: Int) = "formulario_consulta/$asignaturaId"
    }
    //ruta detalles consultas
    object DetalleConsulta: SubDestination("detalle_consulta/{consultaId}"){
        fun createRoute(consultaId: Int) = "detalle_consulta/$consultaId"
    }
}