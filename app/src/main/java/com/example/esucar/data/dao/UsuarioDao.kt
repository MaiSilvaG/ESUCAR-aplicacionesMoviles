package com.example.esucar.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.esucar.data.entity.Usuario

@Dao
interface UsuarioDao{
    @Query("SELECT * FROM usuarios WHERE correo = :correo COLLATE NOCASE AND contrasena = :contrasena LIMIT 1")
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(lista: List<Usuario>)

    @Query("SELECT * FROM usuarios WHERE rol = :rol LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?
}