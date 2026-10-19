package com.example.esucar.ui.view.consultas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.esucar.data.relation.ConsultaDetalle

@Composable
fun DetalleConsultaEstudiante(innerPadding: PaddingValues, detalle: ConsultaDetalle) {
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(Modifier.height(8.dp))
            Column(
                Modifier.fillMaxWidth().padding(13.dp),
                Arrangement.spacedBy(12.dp)
            ) {
                CabeceraConsulta(detalle, mostrarEstudiante = false)
                HorizontalDivider(Modifier.padding(vertical = 4.dp))
                RespuestaConsulta(detalle.consulta)
            }
        }
    }
}