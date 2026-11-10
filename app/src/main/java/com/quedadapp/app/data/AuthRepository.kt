package com.quedadapp.app.data

import java.io.IOException

// Capa intermedia entre las pantallas y la API: traduce respuestas y
// fallos de red a Result<T>, y es quien sabe que el token se manda como
// cabecera "Bearer ...". Las pantallas no hablan con Retrofit directamente.
class AuthRepository(private val tokenStore: TokenStore) {
    private val api = ApiClient.authApi

    suspend fun iniciarSesion(nombreUsuario: String, password: String): Result<Usuario> =
        llamar {
            val respuesta = api.login(LoginRequest(nombreUsuario, password))
            if (!respuesta.isSuccessful) {
                return@llamar Result.failure(Exception(ApiClient.mensajeDeError(respuesta)))
            }
            val cuerpo = respuesta.body() ?: return@llamar Result.failure(Exception("Respuesta vacía del servidor."))
            tokenStore.guardarToken(cuerpo.token)
            Result.success(cuerpo.usuario)
        }

    suspend fun registrar(
        nombreUsuario: String,
        email: String,
        password: String,
        password2: String,
    ): Result<Unit> = llamar {
        val respuesta = api.registro(RegistroRequest(nombreUsuario, email, password, password2))
        if (!respuesta.isSuccessful) {
            Result.failure(Exception(ApiClient.mensajeDeError(respuesta)))
        } else {
            Result.success(Unit)
        }
    }

    // Se llama al abrir la app: si hay un token guardado, comprueba que
    // siga siendo válido (no caducado, cuenta no rechazada...) antes de
    // dar la sesión por buena.
    suspend fun sesionActual(): Usuario? {
        val token = tokenStore.leerToken() ?: return null
        val respuesta = runCatching { api.me("Bearer $token") }.getOrNull() ?: return null
        if (!respuesta.isSuccessful) {
            tokenStore.borrarToken()
            return null
        }
        return respuesta.body()?.usuario
    }

    suspend fun cerrarSesion() {
        val token = tokenStore.leerToken()
        if (token != null) {
            runCatching { api.logout("Bearer $token") }
        }
        tokenStore.borrarToken()
    }

    private suspend fun <T> llamar(bloque: suspend () -> Result<T>): Result<T> = try {
        bloque()
    } catch (e: IOException) {
        Result.failure(Exception("No se pudo conectar con el servidor. Comprueba tu conexión."))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
