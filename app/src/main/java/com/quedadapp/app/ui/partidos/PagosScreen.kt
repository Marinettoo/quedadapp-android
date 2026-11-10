package com.quedadapp.app.ui.partidos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quedadapp.app.data.PagoFila
import com.quedadapp.app.ui.theme.Border
import com.quedadapp.app.ui.theme.Ink
import com.quedadapp.app.ui.theme.Lime
import com.quedadapp.app.ui.theme.Meta
import com.quedadapp.app.ui.theme.Paper

@Composable
fun PagosScreen(
    viewModel: PagosViewModel,
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
                if (datos.parteImporte != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Lime, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Text(
                            "%.2f €".format(datos.parteImporte),
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                            color = Ink,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "por jugador (${datos.totalJugadores})",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Ink,
                        )
                    }
                } else {
                    Text(
                        "Todavía no se ha fijado el importe de la reserva.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Meta,
                    )
                }

                Spacer(Modifier.height(16.dp))

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    datos.filas.forEach { fila ->
                        FilaPago(
                            fila = fila,
                            puedeConfirmarPagos = datos.puedeConfirmarPagos,
                            enProceso = viewModel.idsEnProceso.contains(if (fila.esYo) "yo" else fila.jugadorId),
                            onIndicar = { viewModel.indicarMiPago() },
                            onDeshacer = { viewModel.deshacerMiIndicacion() },
                            onConfirmar = { viewModel.confirmar(fila) },
                            onCorregir = { viewModel.corregir(fila) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaPago(
    fila: PagoFila,
    puedeConfirmarPagos: Boolean,
    enProceso: Boolean,
    onIndicar: () -> Unit,
    onDeshacer: () -> Unit,
    onConfirmar: () -> Unit,
    onCorregir: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Paper),
        border = BorderStroke(1.dp, Border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(fila.nombre, style = MaterialTheme.typography.labelLarge, color = Ink)
            Spacer(Modifier.height(2.dp))
            Text(
                when (fila.estado) {
                    "CONFIRMADO" -> "Confirmado"
                    "INDICADO" -> "Indicado, pendiente de confirmación"
                    else -> "Pendiente de indicar"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Meta,
            )

            val hayBotones = (fila.esYo && fila.estado != "CONFIRMADO") || puedeConfirmarPagos
            if (hayBotones) {
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (fila.esYo && fila.estado != "CONFIRMADO") {
                        if (fila.estado == "PENDIENTE") {
                            BotonPago(texto = "He pagado", enProceso = enProceso, relleno = true, onClick = onIndicar)
                        } else {
                            BotonPago(texto = "Deshacer", enProceso = enProceso, relleno = false, onClick = onDeshacer)
                        }
                    }
                    if (puedeConfirmarPagos) {
                        if (fila.estado != "CONFIRMADO") {
                            BotonPago(texto = "Confirmar", enProceso = enProceso, relleno = true, onClick = onConfirmar)
                        } else {
                            BotonPago(texto = "Corregir", enProceso = enProceso, relleno = false, onClick = onCorregir)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BotonPago(texto: String, enProceso: Boolean, relleno: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        enabled = !enProceso,
        colors = if (relleno) {
            ButtonDefaults.outlinedButtonColors(containerColor = Ink, contentColor = Paper)
        } else {
            ButtonDefaults.outlinedButtonColors(contentColor = Ink)
        },
    ) {
        if (enProceso) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = if (relleno) Paper else Ink,
                strokeWidth = 2.dp,
            )
        } else {
            Text(texto)
        }
    }
}
