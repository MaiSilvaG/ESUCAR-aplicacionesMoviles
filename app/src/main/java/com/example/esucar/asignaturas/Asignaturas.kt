package com.example.esucar.asignaturas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.esucar.data.Asignatura
import com.example.esucar.data.asignaturas
import com.example.esucar.data.unidades
import com.example.esucar.data.materiales
import com.example.esucar.navigation.Destination
import com.example.esucar.navigation.SubDestination
import com.example.esucar.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight

@Composable
fun Asignaturas(navController: NavController) {
    TopBar(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Asignaturas",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Destination.CONSULTAS.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Consultas"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        interior(innerPadding, navController)
    }
}

@Composable
fun interior(innerPadding: PaddingValues, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(asignaturas) { asignatura ->
            AsignaturaCard(
                asignatura = asignatura,
                onCardClick = {
                    navController.navigate(SubDestination.DetalleAsignatura.createRoute(asignatura.id))
                })
        }
    }
}

@Composable
fun AsignaturaCard(asignatura: Asignatura, onCardClick: () -> Unit){
    //calcular progeso
    val unidadesAsignatura = unidades.filter { it.asignaturaId == asignatura.id }
    val idUnidades = unidadesAsignatura.map { it.id }
    val materialAsignatura = materiales.filter {it.unidadId in idUnidades}
    val totalMateriales = materialAsignatura.size
    val materialEstudiado = materialAsignatura.count { it.estudiado }

    val progreso = if(totalMateriales > 0){
        materialEstudiado.toFloat() / totalMateriales.toFloat()
    }
    else{
        0f //previene division por cero
    }
    val porcentaje = (progreso * 100).toInt()

    //tarjetas de las asignaturas
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.Black),
        onClick = onCardClick,
    ) {
        Column(Modifier.fillMaxWidth()) {
            Column(
                Modifier.fillMaxWidth().height(120.dp),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Image(painterResource(R.drawable.ic_launcher_foreground), "imagen asignatura")
            }

            HorizontalDivider(color = Color.Black, thickness = 1.dp)

            //datos de la asignatura
            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                //nombre y codigo
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    Text(
                        "${asignatura.nombre} / ${asignatura.codigo}",
                        Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    IconButton(onCardClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            "detalle asignatura")
                    }
                }

                Spacer(Modifier.height(4.dp))

                //perido
                Text(
                    "Periodo: ${asignatura.periodo}",
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(12.dp))

                //barra de progreso
                LinearProgressIndicator(
                    progress = {progreso},
                    Modifier.fillMaxWidth().height(12.dp)
                )

                Spacer(Modifier.height(6.dp))

                //porcentaje del progreso completado
                Text(
                    "$porcentaje% completado",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
