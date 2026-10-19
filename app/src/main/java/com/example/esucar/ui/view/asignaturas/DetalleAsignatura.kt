package com.example.esucar.ui.view.asignaturas

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.relation.UnidadConMateriales
import com.example.esucar.ui.view.navigation.Destination
import com.example.esucar.ui.view.navigation.AuthDestination
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import com.example.esucar.data.Sesion

class DetalleAsignaturaViewModel(app: Application): AndroidViewModel(app){
    private val db = AppDatabase.getInstance(app)

    fun asignatura(id: Int) = db.asignaturaDao().obtenerPorId(id)
    fun unidades(asignturaId: Int) = db.unidadDao().obtenerMateriales(asignturaId)
    fun marcarEstudiado(materialId: Int, estudiado: Boolean){
        viewModelScope.launch {
            db.materialDao().marcarEstudiado(materialId,estudiado)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleAsignatura(
    asignaturaId: Int,
    navController: NavController,
    viewModel: DetalleAsignaturaViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val asignatura by remember(asignaturaId){viewModel.asignatura(asignaturaId)}
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
                        text = asignatura?.nombre?: "Detalle Asignatura",
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
        if(Sesion.esDocente){
            DetalleAsignaturaDocente(innerPadding,asignaturaId,navController,viewModel)
        }
        else{
            DetalleAsignaturaEstudiante(innerPadding,asignaturaId,navController,viewModel)
        }
    }
}

@Composable
fun UnidadItemAcordeon(item: UnidadConMateriales,
                       acciones:(@Composable (Int, Boolean) -> Unit)? = null
){

    val unidad = item.unidad
    val materialesUnidad = item.materiales
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
                unidad.nombre,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (materialesUnidad.isEmpty()) {
                    Text(
                        "No hay material disponible en esta unidad.",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    materialesUnidad.forEach {material ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    material.titulo,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                                Text(
                                    "Tipo: ${material.tipo}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }

                            if(acciones != null){
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    acciones(material.id, material.estudiado)
                                }
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = Color.LightGray)
    }
}