package com.example.esucar.ui.view.navigation

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.esucar.model.SubDestination

//paginas
import com.example.esucar.ui.view.asignaturas.Asignaturas
import com.example.esucar.ui.view.asignaturas.DetalleAsignatura
import com.example.esucar.ui.view.asignaturas.DetalleAsignaturaViewModel
import com.example.esucar.ui.view.home.Inicio
import com.example.esucar.ui.view.consultas.Consultas
import com.example.esucar.ui.view.consultas.DetalleConsulta
import com.example.esucar.ui.view.consultas.FormularioConsulta
import com.example.esucar.ui.view.login.Login
import com.example.esucar.ui.view.perfil.Perfil

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

object AuthDestination {
    const val LOGIN = "login"
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val startDestination = AuthDestination.LOGIN
    val pantallaActual by navController.currentBackStackEntryAsState()
    val rutaActual = pantallaActual?.destination?.route
    val mostrarBarra = rutaActual != AuthDestination.LOGIN

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if(mostrarBarra){
                NavigationBar(
                    windowInsets = NavigationBarDefaults.windowInsets
                ) {
                    Destination.entries.forEach{destination ->
                        val selected = rutaActual == destination.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if(!selected){
                                    navController.navigate(destination.route){
                                        popUpTo(Destination.HOME.route)
                                        launchSingleTop = true
                                    }
                                }
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
        }
    ) { contentPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(AuthDestination.LOGIN){
                Login(navController)
            }

            composable(Destination.HOME.route) {
                Inicio(navController)
            }

            composable(Destination.ASIGNATURAS.route) {
                Asignaturas(navController)
            }

            composable(Destination.CONSULTAS.route) {
                Consultas(navController)
            }

            composable(Destination.PERFIL.route) {
                Perfil(navController)
            }

            //rutas secundarias
            composable(
                route = SubDestination.DetalleAsignatura.route,
                arguments = listOf(navArgument("asignaturaId") { type = NavType.IntType })
            ) { backStackEntry ->
                val asignaturaId = backStackEntry.arguments?.getInt("asignaturaId") ?: 0
                val detalleViewModel: DetalleAsignaturaViewModel = viewModel()
                DetalleAsignatura(
                    asignaturaId = asignaturaId,
                    navController = navController,
                    viewModel = detalleViewModel
                )
            }

            composable(
                route = SubDestination.DetalleConsulta.route,
                arguments = listOf(navArgument("consultaId"){type = NavType.IntType})
            ){ backStackEntry ->
                val consultaId = backStackEntry.arguments?.getInt("consultaId") ?: 0
                DetalleConsulta(
                    consultaId = consultaId,
                    navController = navController
                )
            }

            composable(
                route = SubDestination.FormularioConsulta.route,
                arguments = listOf(navArgument("asignaturaId"){type = NavType.IntType})
            ){backStackEntry ->
                val asignaturaId = backStackEntry.arguments?.getInt("asignaturaId") ?: 0
                FormularioConsulta(
                    navController = navController
                )
            }
        }
    }
}

