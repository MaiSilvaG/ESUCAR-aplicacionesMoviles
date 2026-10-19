package com.example.esucar.data.dao

import androidx.room.*
import com.example.esucar.data.entity.Asignatura
import com.example.esucar.data.relation.AsignaturaConProgreso
import kotlinx.coroutines.flow.Flow

@Dao
interface AsignaturaDao{
    @Query("""
        SELECT a.*,
            COUNT(m.id) AS totalMateriales,
            COALESCE(SUM(CASE WHEN m.estudiado = 1 THEN 1 ELSE 0 END), 0) AS materialEstudiado
        FROM asignaturas a
        LEFT JOIN unidades u ON u.asignaturaId = a.id
        LEFT JOIN materiales m ON m.unidadId = u.id
        GROUP BY a.id
        ORDER BY a.nombre
    """)
    fun obtenerConProgreso(): Flow<List<AsignaturaConProgreso>>

    @Query("SELECT * FROM asignaturas WHERE id = :id")
    fun obtenerPorId(id: Int): Flow<Asignatura?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(lista: List<Asignatura>)

}