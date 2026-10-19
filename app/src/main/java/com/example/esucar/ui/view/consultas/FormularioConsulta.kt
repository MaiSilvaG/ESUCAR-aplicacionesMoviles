package com.example.esucar.ui.view.consultas

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.esucar.ui.view.navigation.Destination
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.automirrored.filled.Login
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.entity.Asignatura
import com.example.esucar.data.entity.Consulta
import com.example.esucar.data.entity.Unidad
import com.example.esucar.ui.view.navigation.AuthDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.esucar.data.Sesion
import com.example.esucar.data.entity.Roles
class FormularioConsultaViewModel(app: Application): AndroidViewModel(app){
    private val db = AppDatabase.getInstance(app)

    val asignaturas = db.asignaturaDao().obtenerConProgreso()
        .map { lista -> lista.map{it.asignatura} }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun unidades(asignaturaId: Int) = db.unidadDao().obtenerMateriales(asignaturaId)
        .map { lista -> lista.map{it.unidad} }

    fun guardar(consulta: Consulta, alTerminar: () -> Unit){
        viewModelScope.launch {
            val docente = db.usuarioDao().obtenerPorRol(Roles.DOCENTE)?.nombre
                ?: "Sin Docente Registrado"
            db.consultaDao().insertar(consulta.copy(docente = docente))
            alTerminar()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioConsulta(navController: NavController,
                       viewModel: FormularioConsultaViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var asignatura by remember { mutableStateOf<Asignatura?>(null) }
    var unidad by remember { mutableStateOf<Unidad?>(null) }
    var asunto by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    var expandirAsignatura by remember { mutableStateOf(false) }
    var expandirUnidad by remember {mutableStateOf(false) }

    val asignaturas by viewModel.asignaturas.collectAsState()
    val unidades by remember(asignatura?.id) {
        asignatura?.let { viewModel.unidades(it.id) } ?: flowOf(emptyList<Unidad>())
    }.collectAsState(initial = emptyList())

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
                        "Nueva Consulta",
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
        LazyColumn(
            Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expandirAsignatura,
                        {expandirAsignatura = !expandirAsignatura}
                    ) {
                        OutlinedTextField(
                            value = asignatura?.nombre ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Asignatura") },
                            placeholder = {Text("Seleccionar")},
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expandirAsignatura)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expandirAsignatura,
                            {expandirAsignatura = false}
                        ) {
                            asignaturas.forEach { opcion ->
                                DropdownMenuItem(
                                    {Text(opcion.nombre)},
                                    {
                                        asignatura = opcion
                                        unidad = null
                                        expandirAsignatura = false}
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expandirUnidad,
                        {if(asignatura != null) expandirUnidad = !expandirUnidad}
                    ) {
                        OutlinedTextField(
                            value = unidad?.nombre ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Unidad") },
                            placeholder = {
                                Text(
                                    if(asignatura == null) "Primero elige una asignatura"
                                    else "Seleccionar"
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expandirUnidad)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expandirUnidad,
                            {expandirUnidad = false}
                        ) {
                            unidades.forEach { opcion ->
                                DropdownMenuItem(
                                    {Text(opcion.nombre)},
                                    {
                                        unidad = opcion
                                        expandirUnidad = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = asunto,
                        onValueChange = { asunto = it },
                        label = { Text("Asunto") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = mensaje,
                        onValueChange = {mensaje = it},
                        label = {Text("Consulta")},
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 6
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val a = asignatura
                            val u = unidad
                            if(a != null && u != null && asunto.isNotBlank() && mensaje.isNotBlank()){
                                viewModel.guardar(
                                    Consulta(
                                        asignaturaId = a.id,
                                        unidadId = u.id,
                                        estudiante = Sesion.usuario?.nombre ?: "Estudiante",
                                        docente = "",
                                        asunto = asunto,
                                        contenido = mensaje,
                                        fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                            .format(Date()),
                                        respondida = false,
                                        respuesta = null
                                    )
                                ) {navController.popBackStack()}
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enviar Consulta", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}



