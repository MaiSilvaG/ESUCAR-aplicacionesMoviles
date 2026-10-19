package com.example.esucar.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.esucar.data.entity.Asignatura
import com.example.esucar.data.entity.Consulta
import com.example.esucar.data.entity.Unidad

data class ConsultaDetalle(
    @Embedded val consulta: Consulta,
    @Relation(parentColumn = "asignaturaId", entityColumn = "id")
    val asignatura: Asignatura,
    @Relation(parentColumn = "unidadId", entityColumn = "id")
    val unidad: Unidad
)