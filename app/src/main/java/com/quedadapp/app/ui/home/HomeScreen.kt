package com.quedadapp.app.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quedadapp.app.data.Usuario
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Meta

// Pantalla de inicio todavía mínima: solo confirma que el login y la
// sesión guardada funcionan de punta a punta. El resto (próximos
// partidos, asistencia, pagos...) se añade en los siguientes bloques,
// reutilizando esta misma base de red y sesión.
@Composable
fun HomeScreen(
    usuario: Usuario,
    viewModel: HomeViewModel,
    onSesionCerrada: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Hola, ${usuario.nombreUsuario}", style = MaterialTheme.typography.titleLarge, color = Ink)
        Spacer(Modifier.height(4.dp))
        Text(
            if (usuario.rol == "ADMIN") "Administrador" else "Jugador",
            style = MaterialTheme.typography.bodyMedium,
            color = Meta,
        )

        Spacer(Modifier.height(32.dp))
        Text(
            "Próximos partidos, asistencia, equipos y pagos llegarán en próximas versiones.",
            style = MaterialTheme.typography.bodyMedium,
            color = Meta,
        )

        Spacer(Modifier.height(32.dp))
        TextButton(
            onClick = { viewModel.cerrarSesion(onSesionCerrada) },
            enabled = !viewModel.cerrandoSesion,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Cerrar sesión", color = Meta)
        }
    }
}
