package com.quedadapp.app.data

import retrofit2.Response

class PagosRepository(private val tokenStore: TokenStore) {
    private val api = ApiClient.pagosApi

    private fun autorizacion(): String? = tokenStore.leerToken()?.let { "Bearer $it" }

    suspend fun pagos(partidoId: String): Result<DatosPagos> = llamarApi {
        val auth = autorizacion() ?: return@llamarApi Result.failure(Exception("No has iniciado sesión."))
        val respuesta = api.pagos(auth, partidoId)
        if (!respuesta.isSuccessful) {
            return@llamarApi Result.failure(Exception(ApiClient.mensajeDeError(respuesta)))
        }
        val cuerpo = respuesta.body() ?: return@llamarApi Result.failure(Exception("Respuesta vacía del servidor."))
        Result.success(cuerpo.pagos)
    }

    suspend fun indicarPago(partidoId: String): Result<Unit> = llamarApi {
        val auth = autorizacion() ?: return@llamarApi Result.failure(Exception("No has iniciado sesión."))
        responderOk(api.indicarPago(auth, partidoId))
    }

    suspend fun deshacerIndicacion(partidoId: String): Result<Unit> = llamarApi {
        val auth = autorizacion() ?: return@llamarApi Result.failure(Exception("No has iniciado sesión."))
        responderOk(api.deshacerIndicacion(auth, partidoId))
    }

    suspend fun confirmarPago(partidoId: String, jugadorId: String, tipo: String): Result<Unit> = llamarApi {
        val auth = autorizacion() ?: return@llamarApi Result.failure(Exception("No has iniciado sesión."))
        responderOk(api.confirmarPago(auth, partidoId, ConfirmarPagoRequest(jugadorId, tipo)))
    }

    suspend fun corregirConfirmacion(partidoId: String, jugadorId: String, tipo: String): Result<Unit> = llamarApi {
        val auth = autorizacion() ?: return@llamarApi Result.failure(Exception("No has iniciado sesión."))
        responderOk(api.corregirConfirmacion(auth, partidoId, jugadorId, tipo))
    }

    private fun responderOk(respuesta: Response<Unit>): Result<Unit> =
        if (respuesta.isSuccessful) Result.success(Unit) else Result.failure(Exception(ApiClient.mensajeDeError(respuesta)))
}
