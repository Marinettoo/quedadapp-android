package com.quedadapp.app.data

import kotlinx.serialization.Serializable

// Mismas formas que devuelve la API (src/app/api/v1/... en el proyecto
// web). Cualquier cambio ahí debe reflejarse aquí.

@Serializable
data class LoginRequest(val nombreUsuario: String, val password: String)

@Serializable
data class LoginResponse(val token: String, val expiraEn: String, val usuario: Usuario)

@Serializable
data class RegistroRequest(
    val nombreUsuario: String,
    val email: String,
    val password: String,
    val password2: String,
)

@Serializable
data class MeResponse(val usuario: Usuario)

@Serializable
data class ErrorResponse(val error: String)

@Serializable
data class Usuario(
    val id: String,
    val nombreUsuario: String,
    val email: String,
    val rol: String, // "ADMIN" | "USUARIO"
    val estado: String, // "PENDIENTE" | "APROBADA" | "RECHAZADA"
    val fotoActualizadaEn: String?,
)
