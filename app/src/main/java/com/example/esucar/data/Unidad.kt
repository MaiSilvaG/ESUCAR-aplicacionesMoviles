package com.example.esucar.data

data class Unidad(
    val id: Int,
    val nombre: String,
    val asignaturaId: Int
)

val unidades = listOf(

    // Desarrollo de Aplicaciones Móviles - DSY1105
    Unidad(
        1,
        "Unidad 1",
        1
    ),
    Unidad(
        2,
        "Unidad 2",
        1
    ),
    Unidad(
        3,
        "Unidad 3",
        1
    ),

    // Desarrollo FullStack II - DSY1104
    Unidad(
        4,
        "Unidad 1",
        2
    ),
    Unidad(
        5,
        "Unidad 2",
        2
    ),
    Unidad(
        6,
        "Unidad 3",
        2
    ),

    // Desarrollo Cloud Native I - DSY1107
    Unidad(
        7,
        "Unidad 1",
        3
    ),
    Unidad(
        8,
        "Unidad 2",
        3
    ),
    Unidad(
        9,
        "Unidad 3",
        3
    )
)