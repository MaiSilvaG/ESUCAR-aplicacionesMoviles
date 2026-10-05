package com.example.esucar.data
data class Material(
    val id: Int,
    val titulo: String,
    val tipo: String,
    val unidadId: Int,
    val estudiado: Boolean
)

val materiales = listOf(
    // Aplicaciones Móviles - (unidadid 1 -10)
    Material(
        1,
        "Guía Unidad 1",
        "Guía",
        1,
        false
    ),

    // Aplicaciones Móviles - Unidad 2
    Material(
        2,
        "Presentación Unidad 2",
        "Presentación",
        2,
        true
    ),
    // Desarrollo FullStack II - (unidadid 11 -21)
    Material(
        2,
        "Guía Unidad 1",
        "Guía",
        11,
        false
    ),

    // Cloud Native - (unidadid 22 -23)
    Material(
        3,
        "Guía Unidad 1",
        "Guía",
        22,
        false
    )
)