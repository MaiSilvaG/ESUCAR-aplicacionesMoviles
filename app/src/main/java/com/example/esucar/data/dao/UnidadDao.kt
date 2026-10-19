package com.example.esucar.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.esucar.data.entity.Unidad
import com.example.esucar.data.relation.UnidadConMateriales
import kotlinx.coroutines.flow.Flow

@Dao
interface UnidadDao{
    @Transaction
    @Query("SELECT * FROM unidades WHERE asignaturaId = :asignaturaId ORDER BY id")
    fun obtenerMateriales(asignaturaId: Int): Flow<List<UnidadConMateriales>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(lista: List<Unidad>)

    @Update
    suspend fun actualizar(unidad: Unidad)

}