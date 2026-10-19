package com.example.esucar.ui.view.home

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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Message
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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esucar.R
import com.example.esucar.data.AppDatabase
import com.example.esucar.ui.view.navigation.AuthDestination
import com.example.esucar.ui.view.navigation.Destination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import com.example.esucar.data.Sesion
import com.example.esucar.data.relation.AsignaturaConProgreso
import com.example.esucar.ui.view.docente.InicioDocente

class InicioViewModel(app: Application) : AndroidViewModel(app){
    private val db = AppDatabase.getInstance(app)

    val asignaturas = db.asignaturaDao().obtenerConProgreso()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val consultasPendientes = db.consultaDao().contarPendientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),0)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inicio(navController: NavController,
           viewModel: InicioViewModel= viewModel()
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
                        "Inicio",
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
            InicioDocente(innerPadding,navController, viewModel)
        }
        else{
            InicioEstudiante(innerPadding,navController,viewModel)
        }
    }
}

@Composable
fun AsignaturasCard(item: AsignaturaConProgreso,
                    mostrarProgreso: Boolean,
                    onCardClick: () -> Unit
){
    val asignatura = item.asignatura
    val progreso = if (item.totalMateriales > 0) {
        item.materialEstudiado.toFloat() / item.totalMateriales.toFloat()
    } else {
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

                //progreso vista estudiante
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

@Composable
fun TarjetaConsulta(titulo: String,
                    subtitulo:String,
                    onCardClick: () -> Unit
){
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.Black),
        onClick = onCardClick,
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Message,
                "consultas",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                Modifier.weight(1f).padding(horizontal = 12.dp)
            ) {
                Text(
                    titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    subtitulo,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }

            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                "Ir a consultas"
            )
        }
    }
}