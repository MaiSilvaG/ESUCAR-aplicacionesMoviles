package com.example.esucar.ui.view.login

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.Sesion
import com.example.esucar.data.entity.Usuario
import com.example.esucar.ui.view.navigation.Destination
import kotlinx.coroutines.launch

class LoginViewModel(app: Application): AndroidViewModel(app){
    private val db = AppDatabase.getInstance(app)

    fun iniciarSesion(correo: String, contrasena: String, resultado: (Usuario?) -> Unit){
        viewModelScope.launch {
            val usuario = db.usuarioDao().iniciarSesion(correo.trim(),contrasena)
            resultado(usuario)
        }
    }
}
@Composable
fun Login(navController: NavController) {
    Scaffold{ innerPadding ->
        InteriorLogin(innerPadding, navController)
    }
}

@Composable
fun InteriorLogin(innerPadding: PaddingValues, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            InicioSesion(navController)
        }
    }
}

@Composable
fun InicioSesion(navController: NavController,
                 viewModel: LoginViewModel = viewModel()
){
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        Arrangement.spacedBy(12.dp),
        Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesion",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 20.dp, top = 35.dp)
        )

        OutlinedTextField(
            value = correo,
            onValueChange = {correo = it},
            label = {Text("Correo")},
            placeholder = {Text("ejemplo@duocuc.cl")},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contrasena,
            onValueChange = {contrasena = it},
            label = {Text("Contraseña")},
            placeholder = {Text("Ingrese su contraseña")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                if(correo.isBlank() || contrasena.isBlank()){
                    error = "Completa el correo y la contraseña"
                }
                else{
                    viewModel.iniciarSesion(correo,contrasena){usuario ->
                        if(usuario == null){
                            error = "Correo o contraseña incorrectos"
                        }
                        else{
                            error = null
                            Sesion.iniciar(usuario)
                            navController.navigate(Destination.HOME.route){
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Login,
                "Iniciar sesion"
            )
            Text(
                "Iniciar Sesion",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}