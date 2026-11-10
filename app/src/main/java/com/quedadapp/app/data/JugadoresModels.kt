package com.quedadapp.app.data

import kotlinx.serialization.Serializable

@Serializable
data class Jugador(
    val id: String,
    val nombreUsuario: String,
    val tieneFoto: Boolean,
    val fotoActualizadaEn: String?,
    val media: Double?,
    val cuenta: Int,
    val partidosJugados: Int,
    val golesMarcados: Int,
    val edad: Int?,
    val piernaBuena: String?,
    val fotoOffsetX: Double,
    val fotoOffsetY: Double,
    val fotoEscala: Double,
    val esMvp: Boolean,
    val esYo: Boolean,
    val miValoracion: Int,
)

@Serializable
data class JugadoresResponse(val jugadores: List<Jugador>)

@Serializable
data class ValorarRequest(val valoradoId: String, val estrellas: Int)
