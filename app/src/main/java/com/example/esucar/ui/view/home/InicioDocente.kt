package com.example.esucar.ui.view.docente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.esucar.data.Sesion
import com.example.esucar.model.SubDestination
import com.example.esucar.ui.view.home.AsignaturasCard
import com.example.esucar.ui.view.home.InicioViewModel
import com.example.esucar.ui.view.home.TarjetaConsulta
import com.example.esucar.ui.view.navigation.Destination

@Composable
fun InicioDocente(innerPadding: PaddingValues,
                  navController: NavController,
                  viewModel: InicioViewModel
) {
    val lista by viewModel.asignaturas.collectAsState()
    val pendientes by viewModel.consultasPendientes.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item {
            Text("Bienvenido, ${Sesion.usuario?.nombre?.substringBefore(" ") ?: "Docente"}",
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 20.dp)
            )
        }

        item{
            Row(
                Modifier.fillMaxWidth(0.9f),
                Arrangement.spacedBy(12.dp)
            ) {
                Metrica("Asignaturas Dictadas", lista.size, Modifier.weight(1f))
                Metrica("Consultas Pendientes", pendientes, Modifier.weight(1f))
            }
        }

        item {
            HorizontalDivider(Modifier.padding(vertical = 4.dp))
            Text(
                "Mis cursos",
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(0.9f).padding(top = 8.dp)
            )
        }

        items(lista, key = {it.asignatura.id}) { item ->
            AsignaturasCard(
                item = item,
                mostrarProgreso = false,
                onCardClick = {
                    navController.navigate(
                        SubDestination.DetalleAsignatura.createRoute(item.asignatura.id)
                    )
                })
        }

        item {
            TarjetaConsulta(
                titulo = "Consultas recibidas",
                subtitulo = "Revisa las consultas que tienes",
                onCardClick = {
                    navController.navigate(Destination.CONSULTAS.route)
                }
            )
        }
    }
}

@Composable
fun Metrica(titulo: String, valor: Int,modifier: Modifier = Modifier){
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "$valor",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}