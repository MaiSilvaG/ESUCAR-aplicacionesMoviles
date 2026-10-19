package com.example.esucar.ui.view.asignaturas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.esucar.model.SubDestination
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignaturasEstudiante(innerPadding: PaddingValues,
                          navController: NavController,
                          viewModel: AsignaturasViewModel
){
    val lista by viewModel.asignaturas.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(lista, key = {it.asignatura.id}) { item ->
            AsignaturaCard(
                item = item,
                mostrarProgreso = true,
                onCardClick = {
                    navController.navigate(
                        SubDestination.DetalleAsignatura.createRoute(item.asignatura.id)
                    )
                }
            )
        }
    }
}
