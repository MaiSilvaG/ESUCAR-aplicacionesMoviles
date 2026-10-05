package com.example.esucar.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

//paginas
import com.example.esucar.asignaturas.Asignaturas
import com.example.esucar.asignaturas.DetalleAsignatura
import com.example.esucar.home.Inicio
import com.example.esucar.consultas.Consultas
import com.example.esucar.perfil.Perfil

enum class Destination(
    val route:String,
    val label:String,
    val icon: ImageVector,
    val contentDescription: String
){
    HOME("home","inicio", Icons.Default.Home,"Inicio"),
    ASIGNATURAS("asignaturas","Asignaturas",Icons.Default.Book,"Asignatura"),
    CONSULTAS("consultas","Consultas",Icons.Default.Message,"Consultas"),
    PERFIL("perfil","Perfil",Icons.Default.AccountCircle,"Perfil")
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val startDestination = Destination.HOME

    var selectedDestination by rememberSaveable {
        mutableIntStateOf(startDestination.ordinal)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(destination.route)
                            selectedDestination = index
                        },

                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },

                        label = {
                            Text(destination.label)
                        }
                    )
                }
            }
        }
    ) { contentPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = Modifier.padding(contentPadding)
        ) {

            composable(Destination.HOME.route) {
                Inicio()
            }

            composable(Destination.ASIGNATURAS.route) {
                Asignaturas(navController)
            }

            composable(Destination.CONSULTAS.route) {
                Consultas()
            }

            composable(Destination.PERFIL.route) {
                Perfil()
            }

            //rutas secundarias
            composable(
                route = SubDestination.DetalleAsignatura.route,
                arguments = listOf(navArgument("asignaturaId") { type = NavType.IntType })
            ) { backStackEntry ->
                val asignaturaId = backStackEntry.arguments?.getInt("asignaturaId") ?: 0
                DetalleAsignatura(
                    asignaturaId = asignaturaId,
                    navController = navController
                )
            }
        }
    }
}

