package com.example.esucar.ui.view.consultas

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.relation.ConsultaDetalle
import com.example.esucar.ui.view.navigation.AuthDestination
import com.example.esucar.ui.view.navigation.Destination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import com.example.esucar.data.Sesion

class ConsultasViewModel(app: Application): AndroidViewModel(app){
    private val db = AppDatabase.getInstance(app)

    val consultas = db.consultaDao().obtenerTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Consultas(navController: NavController,
              viewModel: ConsultasViewModel = viewModel()
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
                        "Consultas",
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
            ConsultasDocente(innerPadding,navController,viewModel)
        }
        else{
            ConsultasEstudiante(innerPadding,navController,viewModel)
        }
    }
}

@Composable
fun ConsultaCard(item: ConsultaDetalle,
                 mostrarEstudiante: Boolean,
                 onCardClick: () -> Unit
){
    val consulta = item.consulta
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 4.dp).clickable{onCardClick()}
    ) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            Arrangement.spacedBy(6.dp)
        ) {
            //asignatura
            Text(
                item.asignatura.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
            )
            //unidad y flecha para ver detalles
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(
                    item.unidad.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0, 0, 139)
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "ver detalles",
                    tint = Color.Gray
                )
            }
            //asunto y estado
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(
                    consulta.asunto,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    if(consulta.respondida) "Respondida" else "Sin Respuesta",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if(consulta.respondida) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
            //informacion de estudiante o docente
            Text(
                if(mostrarEstudiante) "Estudiante: ${consulta.estudiante}"
                else "Docente: ${consulta.docente}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}