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
    val tieneEquipos: Boolean,
)

@Serializable
data class PartidosResponse(val partidos: List<Partido>)

@Serializable
data class Jugador(
    val nombre: String,
    val esYo: Boolean,
    val esPortero: Boolean,
)

@Serializable
data class Equipo(
    val numero: Int,
    val color: String,
    val jugadores: List<Jugador>,
)

@Serializable
data class DatosEquipos(
    val formato: String,
    val generadoEn: String,
    val equipos: List<Equipo>,
    val listaEspera: List<Jugador>,
)

@Serializable
data class EquiposResponse(val equipos: DatosEquipos)
