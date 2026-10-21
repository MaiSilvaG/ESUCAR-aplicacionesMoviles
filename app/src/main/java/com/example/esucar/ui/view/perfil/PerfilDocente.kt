package com.example.esucar.ui.view.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PerfilDocente(innerPadding: PaddingValues,
                  navController: NavController,
                  viewModel: PerfilViewModel
) {
    val lista by viewModel.asignaturas.collectAsState()
    val pendientes by viewModel.consultasPendientes.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Encabezado("Docente")
        }
        item {
            InformacionPersonal()
        }
        item {
            MiActividadDocente(cantidadAsignaturas = lista.size, pendientes = pendientes)
        }
        item {
            MisAsignaturas(lista)
        }
        item {
            Configuracion()
        }
        item{
            BotonCerrarSesion(navController)
        }
    }
}

@Composable
fun MiActividadDocente(cantidadAsignaturas: Int, pendientes: Int) {
    SeccionPerfil("Mi Actividad") {
        Text("Asignaturas Inscritas: $cantidadAsignaturas", fontSize = 16.sp)
        Text("Consultas por Responder: $pendientes", fontSize = 16.sp)
    }
}