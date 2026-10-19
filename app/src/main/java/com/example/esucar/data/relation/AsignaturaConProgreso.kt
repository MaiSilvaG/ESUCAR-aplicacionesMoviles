package com.example.esucar.data.relation

import androidx.room.Embedded
import com.example.esucar.data.entity.Asignatura

data class AsignaturaConProgreso(
    @Embedded val asignatura: Asignatura,
    val totalMateriales: Int,
    val materialEstudiado: Int
)