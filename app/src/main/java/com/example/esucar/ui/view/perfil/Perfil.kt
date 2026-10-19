package com.example.esucar.ui.view.perfil

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.relation.AsignaturaConProgreso
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PerfilViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getInstance(app)

    val asignaturas = db.asignaturaDao().obtenerConProgreso()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Perfil(navController: NavController) {
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
        InteriorPerfil(innerPadding, navController)
    }
}

@Composable
fun InteriorPerfil(innerPadding: PaddingValues,
                   navController: NavController,
                   viewModel: PerfilViewModel = viewModel()
) {
    val lista by viewModel.asignaturas.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Encabezado()
        }
        item {
            InformacionPersonal()
        }
        item {
            MiActividad(lista)
        }
        item {
            MisAsignaturas(lista)
        }
        item {
            MaterialesDescargados()
        }
        item {
            Configuracion()
        }
        item{
            Button(
                onClick = {navController.navigate(AuthDestination.LOGIN)},
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ){
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    "cerrar sesion"
                )
                Text("Cerrar Sesion")
            }
        }
    }
}

@Composable
fun Encabezado(){
    Column(
        Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            "Nombre",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Estudiante",
            fontSize = 19.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun InformacionPersonal(){
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
                "Informacion Personal",
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
                Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Nombre: Maria Silva",
                    fontSize = 16.sp
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "Correo: mai.silvag@duocuc.cl",
                    fontSize = 16.sp
                )
            }
        }
        HorizontalDivider(color = Color.LightGray)
    }
}

@Composable
fun MiActividad(lista: List<AsignaturaConProgreso>){
    var expanded by remember { mutableStateOf(false) }
    val degrees by animateFloatAsState(if (expanded) 90f else 0f)

    val totalAsignaturas = lista.size
    val totalMaterial = lista.sumOf { it.totalMateriales }
    val materialesEstudiados = lista.sumOf { it.materialEstudiado }
    val progresoGeneral = if (totalMaterial > 0) {
        (materialesEstudiados * 100) / totalMaterial
    } else {
        0
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Mi Actividad",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = if (expanded) "Colapsar" else "Expandir",
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
                Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Asignaturas Inscritas: $totalAsignaturas",
                    fontSize = 16.sp
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "Progreso General: $progresoGeneral%",
                    fontSize = 16.sp
                )
            }
        }
        HorizontalDivider(color = Color.LightGray)
    }
}

@Composable
fun MisAsignaturas(lista: List<AsignaturaConProgreso>) {
    var expanded by remember { mutableStateOf(false) }
    val degrees by animateFloatAsState(if (expanded) 90f else 0f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Mis Asignaturas",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = if (expanded) "Colapsar" else "Expandir",
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
                Arrangement.spacedBy(8.dp)
            ) {
                lista.forEach { item ->
                    Text(
                        "• ${item.asignatura.nombre} (${item.asignatura.codigo})",
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.width(4.dp))
                }
            }
        }
        HorizontalDivider(color = Color.LightGray)
    }
}

@Composable
fun MaterialesDescargados(){
    var expanded by remember { mutableStateOf(false) }
    val degrees by animateFloatAsState(if (expanded) 90f else 0f)

    val descargados = listOf(
        "Apuntes de la unidad" to "PDF",
        "Video de Compose" to "Video",
        "Guía de ejercicios" to "PDF"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Materiales Descargados",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = if (expanded) "Colapsar" else "Expandir",
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
                Arrangement.spacedBy(8.dp)
            ) {
                descargados.forEach { (titulo, tipo) ->
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Download,
                            contentDescription = "descargado",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column(Modifier.padding(start = 12.dp)) {
                            Text(titulo, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Text("Tipo: $tipo", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
        HorizontalDivider(color = Color.LightGray)
    }
}
@Composable
fun Configuracion(){
    var expanded by remember { mutableStateOf(false) }
    val degrees by animateFloatAsState(if(expanded) 90f else 0f)
    var notificaciones by remember { mutableStateOf(true) }

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
                "Configuracion",
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
                Arrangement.spacedBy(8.dp)
            ) {
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
    }
}