package com.quedadapp.app.ui.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Meta
import com.quedadapp.app.ui.theme.Paper

@Composable
fun RegistroEnviadoScreen(onVolverALogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            "Solicitud enviada",
            style = MaterialTheme.typography.titleLarge,
            color = Ink,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Un administrador tiene que aprobar tu cuenta antes de que puedas entrar.",
            style = MaterialTheme.typography.bodyMedium,
            color = Meta,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onVolverALogin,
            colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Paper),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Volver al inicio de sesión")
        }
    }
}
