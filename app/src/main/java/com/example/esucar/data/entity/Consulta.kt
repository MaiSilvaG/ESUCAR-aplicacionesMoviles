package com.example.esucar.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "consultas",
    foreignKeys = [ForeignKey(
        entity = Asignatura::class,
        parentColumns = ["id"],
        childColumns = ["asignaturaId"],
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Unidad::class,
            parentColumns = ["id"],
            childColumns = ["unidadId"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index("asignaturaId"), Index("unidadId")]
)
data class Consulta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val asignaturaId: Int,
    val unidadId: Int,
    val estudiante: String,
    val docente: String,
    val asunto: String,
    val contenido: String,
    val fecha: String,
    val respondida: Boolean = false,
    val respuesta: String? = null
)
