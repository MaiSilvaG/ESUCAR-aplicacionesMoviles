package com.example.esucar.data

data class Asignatura(
    val id: Int,
    val nombre: String,
    val codigo: String,
    val periodo: String
)

val asignaturas = listOf(
    Asignatura(
        1,
        "Desarrollo de Aplicaciones Móviles",
        "DSY1105",
        "2026-2"
    ),
    Asignatura(
        2,
        "Desarrollo FullStack II",
        "DSY1104",
        "2026-2"
    ),
    Asignatura(
        3,
        "Desarrollo Cloud Native I",
        "DSY1107",
        "2026-2"
    )
)