package com.example.esucar.ui.view.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.esucar.model.SubDestination
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.esucar.data.Sesion
import com.example.esucar.data.relation.AsignaturaConProgreso

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioEstudiante(innerPadding: PaddingValues,
                     navController: NavController,
                     viewModel: InicioViewModel
) {
    val lista by viewModel.asignaturas.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item {
            Text("Bienvenido, ${Sesion.usuario?.nombre?.substringBefore(" ") ?: "Estudiante"}",
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        item{
            ProgresoGeneral(lista)
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
                mostrarProgreso = true,
                onCardClick = {
                    navController.navigate(
                        SubDestination.DetalleAsignatura.createRoute(item.asignatura.id)
                    )
                })
        }

        item {
            TarjetaConsulta(
                titulo = "¿Tienes alguna Consulta?",
                subtitulo = "Envía un mensaje",
                onCardClick = {
                    navController.navigate(SubDestination.FormularioConsulta.createRoute(0))
                }
            )
        }
    }
}

@Composable
fun ProgresoGeneral(lista: List<AsignaturaConProgreso>){
    val totalMaterial = lista.sumOf { it.totalMateriales }
    val totalEstudiados = lista.sumOf { it.materialEstudiado }
    val progresoGlobal = if (totalMaterial > 0) {
        totalEstudiados.toFloat() / totalMaterial.toFloat()
    } else {
        0f
    }
    val procentajeGlobal = (progresoGlobal * 100).toInt()

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(0.9f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Progeso General",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = {progresoGlobal},
                modifier = Modifier.fillMaxWidth().height(12.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "$procentajeGlobal% de avance ($totalEstudiados de $totalMaterial materiales)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
