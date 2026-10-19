package com.example.esucar.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.esucar.data.entity.Material

@Dao
interface MaterialDao{
    @Query("UPDATE materiales SET estudiado = :estudiado WHERE id = :id")
    suspend fun marcarEstudiado(id: Int, estudiado: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(material: Material)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(lista: List<Material>)

    @Update
    suspend fun actualizar(material: Material)

    @Delete
    suspend fun eliminar(material: Material)
}