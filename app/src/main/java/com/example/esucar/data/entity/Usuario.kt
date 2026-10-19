package com.example.esucar.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

object Roles{
    const val ESTUDIANTE = "ESTUDIANTE"
    const val DOCENTE = "DOCENTE"
}

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val rol: String
)