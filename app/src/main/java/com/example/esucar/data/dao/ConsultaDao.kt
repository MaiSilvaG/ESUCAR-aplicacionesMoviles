package com.example.esucar.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.esucar.data.entity.Consulta
import com.example.esucar.data.relation.ConsultaDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsultaDao{
    @Transaction
    @Query("SELECT * FROM consultas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<ConsultaDetalle>>

    @Transaction
    @Query("SELECT * FROM consultas WHERE id = :id")
    fun obtenerPorId(id: Int): Flow<ConsultaDetalle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(consulta: Consulta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(lista: List<Consulta>)

    @Query("SELECT COUNT(*) FROM consultas WHERE  respondida = 0")
    fun contarPendientes(): Flow<Int>

    @Query("UPDATE consultas SET respuesta = :respuesta, respondida = 1 WHERE id = :id")
    suspend fun responder(id: Int, respuesta: String)
}