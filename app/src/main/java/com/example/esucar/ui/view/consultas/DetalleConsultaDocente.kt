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
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esucar.data.relation.ConsultaDetalle
import androidx.compose.foundation.layout.imePadding

@Composable
fun DetalleConsultaDocente(innerPadding: PaddingValues,
                    detalle: ConsultaDetalle,
                    viewModel: DetalleConsultaViewModel
){
    val consulta = detalle.consulta
    var respuesta by remember(consulta.id) { mutableStateOf("") }

    LazyColumn(
        Modifier.fillMaxSize().imePadding().padding(horizontal = 16.dp),
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
                CabeceraConsulta(detalle,mostrarEstudiante = true)
                HorizontalDivider(Modifier.padding(vertical = 4.dp))

                if(consulta.respondida){
                    RespuestaConsulta(consulta)
                }
                else{
                    Text(
                        "Responder Consulta: ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    OutlinedTextField(
                        value = respuesta,
                        onValueChange = {respuesta = it},
                        label = {Text("Respuesta")},
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 15
                    )
                    Button(
                        onClick = {viewModel.responder(consulta.id,respuesta)},
                        enabled = respuesta.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Enviar Respuesta",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}