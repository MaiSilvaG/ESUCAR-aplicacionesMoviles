package com.example.esucar.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asignaturas")
data class Asignatura(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val codigo: String,
    val periodo: String
)
