package com.example.esucar.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.esucar.data.entity.Material
import com.example.esucar.data.entity.Unidad

data class UnidadConMateriales(
    @Embedded val unidad: Unidad,
    @Relation(
        parentColumn = "id",
        entityColumn = "unidadId",
        entity = Material::class
    )
    val materiales: List<Material> = emptyList()
)