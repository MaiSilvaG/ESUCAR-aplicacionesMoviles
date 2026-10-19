package com.example.esucar.data

import com.example.esucar.data.entity.Asignatura
import com.example.esucar.data.entity.Consulta
import com.example.esucar.data.entity.Material
import com.example.esucar.data.entity.Roles
import com.example.esucar.data.entity.Unidad
import com.example.esucar.data.entity.Usuario

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
        "2026-2")
)

val unidades = listOf(
    Unidad(1, 1, "Unidad 1"),
    Unidad(2, 1, "Unidad 2"),
    Unidad(3,1,"Unidad 3"),
    Unidad(4, 2, "Unidad 1"),
    Unidad(5,2,"Unidad 2"),
    Unidad(6, 2, "Unidad 3")
)

val materiales = listOf(
    Material(
        1,
        1,
        "Apuntes de la unidad",
        "PDF",
        true
    ),
    Material(
        2,
        2,
        "Video",
        "Video",
        false
    ),
    Material(
        3,
        3,
        "Guía de ejercicios",
        "PDF",
        false
    )
)

val consultas = listOf(
    Consulta(
        1,
        1,
        2,
        "nombre estudiante",
        "nombre docente",
        "asunto",
        "contenido de la consulta",
        "30/09/2026",
        true,
        "respuesta del docente"
    ),
    Consulta(
        2,
        2,
        3,
        "nombre del estudiante",
        "nombre del docente",
        "poner asunto de la consulta",
        "contenido de la consulta",
        "29/09/2026",
        false,
        null
    )
)

val usuarios = listOf(
    Usuario(
        1,
        "Maria Silva",
        "maria@a.cl",
        "1234",
        Roles.ESTUDIANTE
    ),
    Usuario(
        2,
        "Profesor Prueba",
        "profe@a.cl",
        "1234",
        Roles.DOCENTE
    )
)