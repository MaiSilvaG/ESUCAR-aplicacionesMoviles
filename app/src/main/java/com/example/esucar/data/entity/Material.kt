package com.example.esucar.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "materiales",
    foreignKeys = [ForeignKey(
        entity = Unidad::class,
        parentColumns = ["id"],
        childColumns = ["unidadId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("unidadId")]
)
data class Material(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val unidadId: Int,
    val titulo: String,
    val tipo: String,
    val estudiado: Boolean = false
)
