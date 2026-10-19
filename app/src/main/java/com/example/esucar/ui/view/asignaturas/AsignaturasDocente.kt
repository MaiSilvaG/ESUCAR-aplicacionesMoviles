package com.example.esucar.ui.view.asignaturas

import android.app.Application
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
import com.example.esucar.ui.view.navigation.Destination
import com.example.esucar.model.SubDestination
import com.example.esucar.R
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.runtime.collectAsState
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.relation.AsignaturaConProgreso
import com.example.esucar.ui.view.navigation.AuthDestination
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignaturasDocente(innerPadding: PaddingValues,
                       navController: NavController,
                       viewModel: AsignaturasViewModel
) {
    val lista by viewModel.asignaturas.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(lista, key = {it.asignatura.id}) { item ->
            AsignaturaCard(
                item = item,
                mostrarProgreso = false,
                onCardClick = {
                    navController.navigate(
                        SubDestination.DetalleAsignatura.createRoute(item.asignatura.id)
                    )
                }
            )
        }
    }
}