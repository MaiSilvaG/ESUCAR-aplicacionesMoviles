package com.example.esucar.ui.view.asignaturas

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.esucar.ui.view.navigation.Destination
import com.example.esucar.R
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.ui.layout.ContentScale
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.relation.AsignaturaConProgreso
import com.example.esucar.ui.view.navigation.AuthDestination
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.esucar.data.Sesion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AsignaturasViewModel(app: Application): AndroidViewModel(app){
    private val db = AppDatabase.getInstance(app)
    val asignaturas = db.asignaturaDao().obtenerConProgreso()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Asignaturas(navController: NavController,
                viewModel: AsignaturasViewModel = viewModel()
) {
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

                    IconButton(onClick = {
                        navController.navigate(AuthDestination.LOGIN){
                            popUpTo(0){inclusive = true}
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Login,
                            "login"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        if(Sesion.esDocente){
            AsignaturasDocente(innerPadding,navController,viewModel)
        }
        else{
            AsignaturasEstudiante(innerPadding,navController,viewModel)
        }
    }
}


@Composable
fun AsignaturaCard(item: AsignaturaConProgreso,
                   mostrarProgreso: Boolean,
                   onCardClick: () -> Unit
){
    val asignatura = item.asignatura
    val progreso = if(item.totalMateriales > 0){
        val calculo = item.materialEstudiado.toFloat() / item.totalMateriales.toFloat()
        if(calculo.isNaN()) 0f else calculo.coerceIn(0f,1f)
    }
    else{
        0f
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
                Modifier.fillMaxWidth().height(200.dp),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Image(painterResource(R.drawable.fondo),
                    "imagen asignatura",
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Crop
                )

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
                //periodo
                Text(
                    "Periodo: ${asignatura.periodo}",
                    fontSize = 14.sp
                )

                if(mostrarProgreso){
                    Spacer(Modifier.height(12.dp))
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
}