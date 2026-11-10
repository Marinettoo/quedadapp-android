package com.quedadapp.app.data

import kotlinx.serialization.Serializable

@Serializable
data class PagoFila(
    val jugadorId: String,
    val tipo: String, // "USUARIO" | "INVITADO"
    val nombre: String,
    val esYo: Boolean,
    val estado: String, // "PENDIENTE" | "INDICADO" | "CONFIRMADO"
)

@Serializable
data class DatosPagos(
    val importeTotal: Double?,
    val parteImporte: Double?,
    val totalJugadores: Int,
    val puedeEstablecerImporte: Boolean,
    val puedeConfirmarPagos: Boolean,
    val filas: List<PagoFila>,
)

@Serializable
data class PagosResponse(val pagos: DatosPagos)

@Serializable
data class ConfirmarPagoRequest(val jugadorId: String, val tipo: String)
