package com.example.esucar.ui.view.login

import android.app.Application
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.esucar.R
import com.example.esucar.data.AppDatabase
import com.example.esucar.data.entity.Roles
import com.example.esucar.data.entity.Usuario
import com.example.esucar.ui.view.navigation.AuthDestination
import kotlinx.coroutines.launch

class RegistroViewModel(app: Application): AndroidViewModel(app){
    private val db = AppDatabase.getInstance(app)

    fun registrar(
        nombre: String,
        correro: String,
        contrasena: String,
        resultado: (Boolean) -> Unit
    ){
        viewModelScope.launch {
            val id = db.usuarioDao().insertar(
                Usuario(
                    nombre = nombre.trim(),
                    correo = correro.trim().lowercase(),
                    contrasena = contrasena,
                    rol = Roles.ESTUDIANTE
                )
            )
            resultado(id != -1L)
        }
    }
}

@Composable
fun Registro(navController: NavController){
    Scaffold { innerPadding ->
        LazyColumn(
            Modifier.fillMaxSize().imePadding(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                FormularioRegistro(navController)
            }
        }
    }
}

@Composable
fun FormularioRegistro(
    navController: NavController,
    viewModel: RegistroViewModel = viewModel()
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var guardando by remember { mutableStateOf(false) }

    val irALogin: () -> Unit = {
        navController.navigate(AuthDestination.LOGIN) {
            popUpTo(AuthDestination.LOGIN) { inclusive = true }
        }
    }

    Column(
        Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        Arrangement.spacedBy(12.dp),
        Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.logo),
            "logo",
            Modifier.padding(top = 32.dp).size(100.dp)
        )

        Text(
            "ESUCAR",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            "Crear Cuenta",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            placeholder = { Text("ejemplo@esucar.com") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = confirmar,
            onValueChange = { confirmar = it },
            label = { Text("Confirmar contraseña") },
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
                val correoLimpio = correo.trim()
                when {
                    nombre.isBlank() || correoLimpio.isBlank() ||
                            contrasena.isBlank() || confirmar.isBlank() ->
                        error = "Completa todos los campos"

                    !Patterns.EMAIL_ADDRESS.matcher(correoLimpio).matches() ->
                        error = "Ingresa un correo valido"

                    contrasena != confirmar ->
                        error = "Las contraseñas no coinciden"

                    else -> {
                        error = null
                        guardando = true
                        viewModel.registrar(nombre, correoLimpio, contrasena) { creada ->
                            if (creada) {
                                Toast.makeText(
                                    context,
                                    "Cuenta creada. Ahora inicia sesion",
                                    Toast.LENGTH_LONG
                                ).show()
                                irALogin()
                            } else {
                                guardando = false
                                error = "Ese correo ya esta registrado"
                            }
                        }
                    }
                }
            },
            enabled = !guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse", fontSize = 16.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "¿Ya tienes cuenta?",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            TextButton(onClick = irALogin) {
                Text(
                    "Inicia Sesion",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}