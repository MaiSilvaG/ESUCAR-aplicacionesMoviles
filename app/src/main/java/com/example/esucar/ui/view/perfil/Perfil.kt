package com.example.esucar.ui.view.perfil

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.esucar.ui.view.navigation.AuthDestination
import com.example.esucar.ui.view.navigation.Destination
import android.app.Application
import androidx.compose.foundation.layout.ColumnScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.Sesion
import com.example.esucar.data.relation.AsignaturaConProgreso
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PerfilViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getInstance(app)

    val asignaturas = db.asignaturaDao().obtenerConProgreso()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val consultasPendientes = db.consultaDao().contarPendientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),0)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Perfil(navController: NavController, viewModel: PerfilViewModel = viewModel()) {
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
                        "Perfil",
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
            PerfilDocente(innerPadding,navController,viewModel)
        }
        else{
            PerfilEstudiante(innerPadding,navController,viewModel)
        }
    }
}

@Composable
fun SeccionPerfil(titulo: String,contenido: @Composable ColumnScope.() -> Unit){
    var expanded by remember { mutableStateOf(false) }
    val degrees by animateFloatAsState(if(expanded) 90f else 0f)

    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable{expanded = !expanded}
                .padding(16.dp),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                Icons.Default.ChevronRight,
                if(expanded) "Colapsar" else "Expandir",
                modifier = Modifier.rotate(degrees)
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                Arrangement.spacedBy(8.dp),
                content = contenido
            )
        }
        HorizontalDivider(color = Color.LightGray)
    }
}

@Composable
fun Encabezado(rol: String){
    Column(
        Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            Sesion.usuario?.nombre?.substringBefore(" ") ?: "Nombre",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            rol,
            fontSize = 19.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun InformacionPersonal(){
    val usuario = Sesion.usuario

    SeccionPerfil("Informacion Personal") {
        Text(
            "Nombre: ${usuario?.nombre ?: "-"}",
            fontSize = 16.sp
        )
        Text(
            "Correo: ${usuario?.correo ?: "-"}",
            fontSize = 16.sp
        )
    }
}

@Composable
fun MisAsignaturas(lista: List<AsignaturaConProgreso>) {
    SeccionPerfil("Mis Asignaturas") {
        lista.forEach { item ->
            Text(
                "* ${item.asignatura.nombre} - (${item.asignatura.codigo})",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun Configuracion(){
    var notificaciones by remember { mutableStateOf(true) }

    SeccionPerfil("Configuracion") {
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                "Recibir Notificaciones",
                fontSize = 16.sp
            )
            Switch(
                notificaciones,
                {notificaciones = it}
            )
        }
        Spacer(Modifier.width(4.dp))
        Text(
            "Ajustes de la Aplicacion",
            fontSize = 16.sp
        )
    }
}

@Composable
fun BotonCerrarSesion(navController: NavController){
    Button(
        onClick = {
            navController.navigate(AuthDestination.LOGIN){
                popUpTo(0) {inclusive = true}
            }
        },
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
    ) {
        Icon(
            Icons.AutoMirrored.Filled.Logout,
            "cerrar sesion"
        )
        Text("Cerrar Sesion")
    }
}