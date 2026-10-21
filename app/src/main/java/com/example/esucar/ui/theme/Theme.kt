package com.example.esucar.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = VerdePrincipalOscuro,
    onPrimary = SobreVerdeOscuro,
    primaryContainer = VerdeContenedorOscuro,
    onPrimaryContainer = SobreVerdeContenedorOscuro,
    secondary = NaranjoSecundarioOscuro,
    onSecondary = SobreNaranjoOscuro,
    secondaryContainer = NaranjoContenedorOscuro,
    onSecondaryContainer = SobreNaranjoContenedorOscuro,
    tertiary = TerciarioOscuro
)

private val LightColorScheme = lightColorScheme(
    primary = VerdePrincipal,
    onPrimary = Color.White,
    primaryContainer = VerdeContenedor,
    onPrimaryContainer = SobreVerdeContenedor,
    secondary = NaranjoSecundario,
    onSecondary = Color.White,
    secondaryContainer = NaranjoContenedor,
    onSecondaryContainer = SobreNaranjoContenedor,
    tertiary = TerciarioClaro
)

@Composable
fun EsucarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // false para que se use la paleta verde y naranja de la app
    // (en true, Android 12+ la reemplaza por los colores del fondo de pantalla)
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}