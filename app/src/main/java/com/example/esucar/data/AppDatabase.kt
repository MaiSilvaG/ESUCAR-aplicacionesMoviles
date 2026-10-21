package com.example.esucar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import com.example.esucar.data.dao.AsignaturaDao
import com.example.esucar.data.dao.ConsultaDao
import com.example.esucar.data.dao.MaterialDao
import com.example.esucar.data.dao.UnidadDao
import com.example.esucar.data.dao.UsuarioDao
import com.example.esucar.data.entity.Asignatura
import com.example.esucar.data.entity.Consulta
import com.example.esucar.data.entity.Material
import com.example.esucar.data.entity.Unidad
import com.example.esucar.data.entity.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [Asignatura::class, Unidad::class, Material::class, Consulta::class, Usuario::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase: RoomDatabase(){
    abstract fun asignaturaDao(): AsignaturaDao
    abstract fun unidadDao(): UnidadDao
    abstract fun materialDao(): MaterialDao
    abstract fun consultaDao(): ConsultaDao
    abstract fun usuarioDao(): UsuarioDao

    companion object{
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(object: Callback(){
                        override fun onCreate(connection: SQLiteConnection) {
                            super.onCreate(connection)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { bd ->
                                    bd.asignaturaDao().insertarTodas(asignaturas)
                                    bd.unidadDao().insertarTodas(unidades)
                                    bd.materialDao().insertarTodos(materiales)
                                    bd.consultaDao().insertarTodas(consultas)
                                    bd.usuarioDao().insertarTodos(usuarios)
                                }
                            }
                        }
                    })
                    .build().also{INSTANCE = it}
            }
    }
}