package com.example.esucar.ui.view.asignaturas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.collectAsState
import com.example.esucar.model.SubDestination

@Composable
fun DetalleAsignaturaDocente(
    innerPadding: PaddingValues,
    asignaturaId: Int,
    navController: NavController,
    viewModel: DetalleAsignaturaViewModel
) {
    val unidadesAsignatura by remember(asignaturaId) { viewModel.unidades(asignaturaId) }
        .collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(unidadesAsignatura, key = {it.unidad.id}){ item ->
            UnidadItemAcordeon(item)
        }

        item{
            Button(
                onClick = {
                    navController.navigate(
                        SubDestination.PublicarMaterial.createRoute(asignaturaId)
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text("Publicar Material")
            }
        }
    }
}