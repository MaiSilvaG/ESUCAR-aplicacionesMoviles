package com.example.esucar.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.esucar.data.entity.Roles
import com.example.esucar.data.entity.Usuario

//guardar quien inicio sesion
object Sesion{
    var usuario by mutableStateOf<Usuario?>(null)
        private set

    val esDocente: Boolean
        get() = usuario?.rol == Roles.DOCENTE

    fun iniciar(u: Usuario){usuario = u}
    fun cerrar(){usuario == null}
}