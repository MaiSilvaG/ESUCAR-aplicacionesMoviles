package com.example.esucar.ui.view.consultas

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.relation.ConsultaDetalle
import com.example.esucar.ui.view.navigation.AuthDestination
import com.example.esucar.ui.view.navigation.Destination
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.example.esucar.data.Sesion
import com.example.esucar.data.entity.Consulta
import kotlinx.coroutines.launch

class DetalleConsultaViewModel(app: Application): AndroidViewModel(app){
    private val db = AppDatabase.getInstance(app)

    fun consulta(id: Int) = db.consultaDao().obtenerPorId(id)
    fun responder(id: Int, respuesta: String){
        viewModelScope.launch {
            db.consultaDao().responder(id,respuesta.trim())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleConsulta(consultaId: Int,
                    navController: NavController,
                    viewModel: DetalleConsultaViewModel = viewModel()
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val item by remember(consultaId) {viewModel.consulta(consultaId)  }
        .collectAsState(initial = null)

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
                        item?.consulta?.asunto ?: "Detalle Consulta",
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
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
        item?.let { detalle ->
            if(Sesion.esDocente){
                DetalleConsultaDocente(innerPadding,detalle,viewModel)
            }
            else{
                DetalleConsultaEstudiante(innerPadding,detalle)
            }
        }
    }
}

@Composable
fun CabeceraConsulta(detalle: ConsultaDetalle, mostrarEstudiante: Boolean){
    val consulta = detalle.consulta

    Column(verticalArrangement = Arrangement.spacedBy(12.dp))
    {
        //asignatura
        Text(
            detalle.asignatura.nombre,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.primary
        )
        //unidad y fecha
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                detalle.unidad.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0, 0, 139)
            )
            Text(
                consulta.fecha,
                fontSize = 15.sp,
                color = Color.Gray
            )
        }
        //asunto
        Text(
            consulta.asunto,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        //informacion de la persona
        Text(
            if(mostrarEstudiante) "Estudiante: ${consulta.estudiante}"
            else "Docente: ${consulta.docente}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        //contenido
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp))
        {
            Text(
                "Consulta: ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Text(
                consulta.contenido,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun RespuestaConsulta(consulta: Consulta){
    val respuesta = consulta.respuesta

    //respuesta
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp))
    {
        Text(
            "Respuesta del Docente: ",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        if (consulta.respondida && !respuesta.isNullOrBlank()) {
            Text(
                respuesta,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2E7D32)
            )
        } else {
            Text(
                "Sin respuesta",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFC62828)
            )
        }
    }
}