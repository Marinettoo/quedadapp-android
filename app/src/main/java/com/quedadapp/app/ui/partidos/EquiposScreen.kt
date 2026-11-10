package com.quedadapp.app.ui.partidos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.quedadapp.app.data.Jugador
import com.quedadapp.app.ui.comunes.formatearFechaIso
import com.quedadapp.app.ui.theme.Border
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Lime
import com.quedadapp.app.ui.theme.Meta
import com.quedadapp.app.ui.theme.Paper

@Composable
fun EquiposScreen(
    viewModel: EquiposViewModel,
    partidoId: String,
    onVolver: () -> Unit,
) {
    LaunchedEffect(partidoId) { viewModel.cargar(partidoId) }

    Column(modifier = Modifier.fillMaxSize()) {
        TextButton(onClick = onVolver) {
            Text("← Volver", color = Meta)
        }
        Spacer(Modifier.height(8.dp))

        val datos = viewModel.datos
        when {
            viewModel.cargando && datos == null -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Ink, modifier = Modifier.padding(32.dp))
                }
            }

            viewModel.error != null && datos == null -> {
                Text(
                    viewModel.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            datos != null -> {
                Text(
                    "Generados el ${formatearFechaIso(datos.generadoEn)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Meta,
                )
                Spacer(Modifier.height(16.dp))

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    datos.equipos.forEach { equipo ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Paper),
                            border = BorderStroke(1.dp, Border),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CirculoColor(hex = equipo.color)
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "Equipo ${equipo.numero}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Ink,
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                equipo.jugadores.forEach { jugador -> FilaJugador(jugador) }
                            }
                        }
                    }

                    if (datos.listaEspera.isNotEmpty()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Paper),
                            border = BorderStroke(1.dp, Border),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Lista de espera", style = MaterialTheme.typography.labelLarge, color = Ink)
                                Spacer(Modifier.height(8.dp))
                                datos.listaEspera.forEach { jugador -> FilaJugador(jugador) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CirculoColor(hex: String) {
    val color = runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrDefault(Ink)
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(color)
            .then(if (color == Paper) Modifier.border(1.dp, Border, CircleShape) else Modifier),
    )
}

@Composable
private fun FilaJugador(jugador: Jugador) {
    val fondo = if (jugador.esYo) Modifier.background(Lime, RoundedCornerShape(8.dp)) else Modifier
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .then(fondo)
            .padding(horizontal = if (jugador.esYo) 8.dp else 0.dp, vertical = if (jugador.esYo) 4.dp else 2.dp),
    ) {
        Text(
            buildString {
                append(jugador.nombre)
                if (jugador.esYo) append(" (tú)")
                if (jugador.esPortero) append(" · 🧤 Portero")
            },
            style = MaterialTheme.typography.bodyMedium,
            color = Ink,
        )
    }
}
