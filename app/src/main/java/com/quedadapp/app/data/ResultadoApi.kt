package com.quedadapp.app.data

import java.io.IOException

// Mismo envoltorio try/catch que antes solo tenía AuthRepository,
// ahora compartido por cualquier repositorio que hable con la API.
suspend fun <T> llamarApi(bloque: suspend () -> Result<T>): Result<T> = try {
    bloque()
} catch (e: IOException) {
    Result.failure(Exception("No se pudo conectar con el servidor. Comprueba tu conexión."))
} catch (e: Exception) {
    Result.failure(e)
}
