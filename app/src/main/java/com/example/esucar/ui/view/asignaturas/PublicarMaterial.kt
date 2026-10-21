package com.example.esucar.ui.view.asignaturas

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.entity.Material
import com.example.esucar.data.entity.Unidad
import com.example.esucar.ui.view.navigation.AuthDestination
import com.example.esucar.ui.view.navigation.Destination
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PublicarMaterialViewModel(app: Application): AndroidViewModel(app){
    private val db = AppDatabase.getInstance(app)

    fun asignatura(id: Int) = db.asignaturaDao().obtenerPorId(id)

    fun unidades(asignaturaId: Int) = db.unidadDao().obtenerMateriales(asignaturaId)
        .map { lista -> lista.map { it.unidad } }

    fun publicar(material: Material, alTerminar: () -> Unit){
        viewModelScope.launch {
            db.materialDao().insertar(material)
            alTerminar()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicarMaterial(asignaturaId: Int,
                     navController: NavController,
                     viewModel: PublicarMaterialViewModel  = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val asignatura by remember(asignaturaId) {viewModel.asignatura(asignaturaId) }
        .collectAsState(initial = null)
    val unidades by remember(asignaturaId) {viewModel.unidades(asignaturaId)}
        .collectAsState(initial = emptyList())
    var unidad by remember { mutableStateOf<Unidad?>(null) }
    var titulo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var expandirUnidad by remember { mutableStateOf(false) }
    val formularioCompleto = unidad != null && titulo.isNotBlank() && tipo.isNotBlank()

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
                        "Publicar Material",
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
        ) {
            item {
                Column(
                    Modifier.fillMaxWidth(),
                    Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = asignatura?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Asignatura") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expandirUnidad,
                        { expandirUnidad = !expandirUnidad }
                    ) {
                        OutlinedTextField(
                            value = unidad?.nombre ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Unidad") },
                            placeholder = { Text("Seleccionar") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expandirUnidad)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expandirUnidad,
                            { expandirUnidad = false }
                        ) {
                            unidades.forEach { opcion ->
                                DropdownMenuItem(
                                    { Text(opcion.nombre) },
                                    {
                                        unidad = opcion
                                        expandirUnidad = false
                                    }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Titulo Material") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tipo,
                        onValueChange = { tipo = it },
                        label = { Text("Tipo") },
                        placeholder = { Text("Ej: PDF, Video, Guia") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val u = unidad
                            if (u != null && formularioCompleto) {
                                viewModel.publicar(
                                    Material(
                                        unidadId = u.id,
                                        titulo = titulo.trim(),
                                        tipo = tipo.trim(),
                                        estudiado = false
                                    )
                                ) { navController.popBackStack() }
                            }
                        },
                        enabled = formularioCompleto,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Publicar", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}