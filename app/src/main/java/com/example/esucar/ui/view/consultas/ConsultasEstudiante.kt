package com.example.esucar.ui.view.consultas

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp
import com.example.esucar.model.SubDestination
import androidx.compose.runtime.getValue

@Composable
fun ConsultasEstudiante(innerPadding: PaddingValues,
                        navController: NavController,
                        viewModel: ConsultasViewModel
) {
    val lista by viewModel.consultas.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            Button(
                onClick = {navController.navigate(SubDestination.FormularioConsulta.createRoute(0))},
                modifier = Modifier.fillMaxSize(0.6f).padding(vertical = 4.dp)
            ){
                Text("+ Nueva Consulta", fontSize = 16.sp)
            }
        }

        items(lista, key = {it.consulta.id}) { item ->
            ConsultaCard(
                item = item,
                mostrarEstudiante = false,
                onCardClick = {
                    navController.navigate(
                        SubDestination.DetalleConsulta.createRoute(item.consulta.id)
                    )
                }
            )
        }
    }
}