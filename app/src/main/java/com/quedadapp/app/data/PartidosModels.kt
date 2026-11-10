package com.quedadapp.app.data

import kotlinx.serialization.Serializable

@Serializable
data class Partido(
    val id: String,
    val fechaHora: String,
    val horaFin: String?,
    val ubicacion: String,
    val encargado: String,
    val asistentes: Int,
    val enCurso: Boolean,
    val abierto: Boolean,
    val cierre: String,
    val yaApuntado: Boolean,
)

@Serializable
data class PartidosResponse(val partidos: List<Partido>)
