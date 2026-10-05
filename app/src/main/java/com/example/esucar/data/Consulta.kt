package com.example.esucar.data

data class Consulta(
    val id: Int,
    val estudiante: String,
    val docente: String,
    val asunto: String,
    val unidad: String,
    val contenido: String,
    val fecha: String,
    val respondida: Boolean,
    val respuesta: String?
)

val consultas = listOf(
    Consulta(
        1,
        "nombre estudiante",
        "nombre docente",
        "poner asunto de la consulta",
        "Unidad 1",
        "contenido de la consulta",
        "30/09/2026",
        true,
        "respuesta del dodcente"
    ),

    Consulta(
        2,
        "nombre del estudiante",
        "nombre del docente",
        "poner asunto de la consulta",
        "Unidad 2",
        "contenido de la consulta",
        "29/09/2026",
        false,
        null
    )


)