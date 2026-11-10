package com.quedadapp.app.ui.partidos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quedadapp.app.data.Partido
import com.quedadapp.app.ui.comunes.formatearFechaIso
import com.quedadapp.app.ui.theme.Border
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Meta
import com.quedadapp.app.ui.theme.Paper

@Composable
fun PartidosScreen(viewModel: PartidosViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Próximos partidos", style = MaterialTheme.typography.titleLarge, color = Ink)
        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = viewModel.error != null,
            enter = fadeIn(tween(200)) + expandVertically(tween(200)),
            exit = fadeOut(tween(150)) + shrinkVertically(tween(150)),
        ) {
            Column {
                Text(
                    viewModel.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        when {
            viewModel.cargando && viewModel.partidos.isEmpty() -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Ink, modifier = Modifier.padding(32.dp))
                }
            }

            viewModel.partidos.isEmpty() -> {
                Text(
                    "No hay partidos programados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Meta,
                )
            }

            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(viewModel.partidos, key = { it.id }) { partido ->
                        TarjetaPartido(
                            partido = partido,
                            enProceso = viewModel.idsEnProceso.contains(partido.id),
                            onAlternarAsistencia = { viewModel.alternarAsistencia(partido) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaPartido(
    partido: Partido,
    enProceso: Boolean,
    onAlternarAsistencia: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Paper),
        border = BorderStroke(1.dp, Border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                if (partido.enCurso) "⚽ En curso" else formatearFechaIso(partido.fechaHora),
                style = MaterialTheme.typography.labelLarge,
                color = Ink,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "${partido.ubicacion} · ${partido.asistentes} apuntados",
                style = MaterialTheme.typography.bodyMedium,
                color = Meta,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "Encargado: ${partido.encargado}",
                style = MaterialTheme.typography.bodyMedium,
                color = Meta,
            )

            if (partido.abierto) {
                Spacer(Modifier.height(12.dp))
                Row {
                    OutlinedButton(
                        onClick = onAlternarAsistencia,
                        enabled = !enProceso,
                        colors = if (partido.yaApuntado) {
                            ButtonDefaults.outlinedButtonColors(contentColor = Ink)
                        } else {
                            ButtonDefaults.outlinedButtonColors(containerColor = Ink, contentColor = Paper)
                        },
                    ) {
                        if (enProceso) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = if (partido.yaApuntado) Ink else Paper,
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Text(if (partido.yaApuntado) "Cancelar asistencia" else "Apuntarme")
                        }
                    }
                }
            } else {
                Spacer(Modifier.height(8.dp))
                Text(
                    "El plazo para apuntarse ya ha cerrado.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Meta,
                )
            }
        }
    }
}
