package com.quedadapp.app.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quedadapp.app.data.Usuario
import com.quedadapp.app.ui.partidos.EquiposScreen
import com.quedadapp.app.ui.partidos.EquiposViewModel
import com.quedadapp.app.ui.partidos.PagosScreen
import com.quedadapp.app.ui.partidos.PagosViewModel
import com.quedadapp.app.ui.partidos.PartidosScreen
import com.quedadapp.app.ui.partidos.PartidosViewModel
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Meta
import kotlinx.coroutines.delay

// Sin Navigation-Compose todavía: "ver equipos"/"ver pagos" son un
// segundo nivel dentro de Inicio, controlado con este estado local en
// vez de una pila de navegación de verdad.
private sealed interface PantallaInterior {
    data object Lista : PantallaInterior
    data class VerEquipos(val partidoId: String) : PantallaInterior
    data class VerPagos(val partidoId: String) : PantallaInterior
}

// Saludo + próximos partidos (de verdad, contra la API) + cerrar
// sesión. El resto (votaciones, apuestas, estadísticas...) se añade en
// los siguientes bloques.
@Composable
fun HomeScreen(
    usuario: Usuario,
    viewModel: HomeViewModel,
    partidosViewModel: PartidosViewModel,
    equiposViewModel: EquiposViewModel,
    pagosViewModel: PagosViewModel,
    onSesionCerrada: () -> Unit,
    onVerJugadores: () -> Unit,
) {
    var pantallaInterior by remember { mutableStateOf<PantallaInterior>(PantallaInterior.Lista) }

    // Entrada escalonada: el saludo, luego la lista y luego el botón,
    // cada uno un poco después del anterior, en vez de que aparezca
    // todo el bloque de golpe.
    var mostrarSaludo by remember { mutableStateOf(false) }
    var mostrarLista by remember { mutableStateOf(false) }
    var mostrarBoton by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        mostrarSaludo = true
        delay(90)
        mostrarLista = true
        delay(90)
        mostrarBoton = true
    }

    val entrada = fadeIn(tween(350)) + slideInVertically(tween(350)) { alto -> alto / 5 }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        AnimatedVisibility(visible = mostrarSaludo, enter = entrada) {
            Column {
                Text("Hola, ${usuario.nombreUsuario}", style = MaterialTheme.typography.titleLarge, color = Ink)
                Spacer(Modifier.height(4.dp))
                Text(
                    if (usuario.rol == "ADMIN") "Administrador" else "Jugador",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Meta,
                )
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onVerJugadores) {
                    Text("Ver jugadores →", color = Ink)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(visible = mostrarLista, enter = entrada, modifier = Modifier.weight(1f)) {
            when (val actual = pantallaInterior) {
                PantallaInterior.Lista -> PartidosScreen(
                    viewModel = partidosViewModel,
                    onVerEquipos = { partido -> pantallaInterior = PantallaInterior.VerEquipos(partido.id) },
                    onVerPagos = { partido -> pantallaInterior = PantallaInterior.VerPagos(partido.id) },
                )

                is PantallaInterior.VerEquipos -> EquiposScreen(
                    viewModel = equiposViewModel,
                    partidoId = actual.partidoId,
                    onVolver = { pantallaInterior = PantallaInterior.Lista },
                )

                is PantallaInterior.VerPagos -> PagosScreen(
                    viewModel = pagosViewModel,
                    partidoId = actual.partidoId,
                    onVolver = { pantallaInterior = PantallaInterior.Lista },
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(visible = mostrarBoton, enter = entrada) {
            TextButton(
                onClick = { viewModel.cerrarSesion(onSesionCerrada) },
                enabled = !viewModel.cerrandoSesion,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Cerrar sesión", color = Meta)
            }
        }
    }
}
