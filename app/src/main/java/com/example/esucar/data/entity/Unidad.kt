package com.example.esucar.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "unidades",
    foreignKeys = [ForeignKey(
        entity = Asignatura::class,
        parentColumns = ["id"],
        childColumns = ["asignaturaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("asignaturaId")]
)
data class Unidad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val asignaturaId: Int,
    val nombre: String
)

