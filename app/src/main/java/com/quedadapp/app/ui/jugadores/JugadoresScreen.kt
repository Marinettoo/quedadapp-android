package com.quedadapp.app.ui.jugadores

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quedadapp.app.data.Jugador
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Meta

@Composable
fun JugadoresScreen(viewModel: JugadoresViewModel, onVolver: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TextButton(onClick = onVolver) {
            Text("← Volver", color = Meta)
        }
        Text("Valorar jugadores", style = MaterialTheme.typography.titleLarge, color = Ink)
        Spacer(Modifier.height(4.dp))
        Text(
            "De 1 a 5 estrellas, anónimo: solo se ve la media de los votos.",
            style = MaterialTheme.typography.bodyMedium,
            color = Meta,
        )
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
            viewModel.cargando && viewModel.jugadores.isEmpty() -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Ink, modifier = Modifier.padding(32.dp))
                }
            }

            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    items(viewModel.jugadores, key = { it.id }) { jugador ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            TarjetaJugador(jugador = jugador, token = viewModel.token)
                            Spacer(Modifier.height(10.dp))
                            if (jugador.esYo) {
                                Text(
                                    "Esta es tu ficha, no puedes valorarte a ti mismo.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Meta,
                                )
                            } else {
                                FilaEstrellas(
                                    valoracion = jugador.miValoracion,
                                    enProceso = viewModel.idsValorando.contains(jugador.id),
                                    onValorar = { estrellas -> viewModel.valorar(jugador, estrellas) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaEstrellas(valoracion: Int, enProceso: Boolean, onValorar: (Int) -> Unit) {
    Row {
        for (n in 1..5) {
            IconButton(onClick = { if (!enProceso) onValorar(n) }) {
                Text(
                    "★",
                    fontSize = 26.sp,
                    color = if (n <= valoracion) Ink else Color.Black.copy(alpha = 0.15f),
                )
            }
        }
    }
}
