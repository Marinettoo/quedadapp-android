package com.quedadapp.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Solo esquema claro por ahora (docs/diseno.md pide "tema claro,
// minimalista"); si el modo oscuro de la web se traslada aquí más
// adelante, este es el sitio para añadir un darkColorScheme.
private val QuedadappColorScheme = lightColorScheme(
    primary = Ink,
    onPrimary = Paper,
    secondary = Ink,
    background = Paper,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = Surface,
    onSurfaceVariant = Meta,
    outline = Border,
    error = Color(0xFFB00020), // rojo estándar M3, solo para mensajes de error
)

@Composable
fun QuedadappTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = QuedadappColorScheme,
        typography = Typography,
        content = content,
    )
}
