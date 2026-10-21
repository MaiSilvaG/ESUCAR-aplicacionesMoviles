package com.example.esucar.ui.view.asignaturas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.esucar.model.SubDestination

@Composable
fun DetalleAsignaturaEstudiante(
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
            UnidadItemAcordeon(item){materialId, estudiado ->
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.Download,
                        "Descargar Material"
                    )
                }
                Checkbox(
                    checked = estudiado,
                    onCheckedChange = {viewModel.marcarEstudiado(materialId,it)}
                )
            }
        }

        item{
            Button(
                onClick = {navController.navigate(SubDestination.FormularioConsulta.createRoute(asignaturaId))},
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text("Consultar sobre la asignatura")
            }
        }
    }
}
