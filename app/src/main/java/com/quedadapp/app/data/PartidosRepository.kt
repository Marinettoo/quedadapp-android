package com.quedadapp.app.data

class PartidosRepository(private val tokenStore: TokenStore) {
    private val api = ApiClient.partidosApi

    private fun autorizacion(): String? = tokenStore.leerToken()?.let { "Bearer $it" }

    suspend fun listar(): Result<List<Partido>> = llamarApi {
        val auth = autorizacion() ?: return@llamarApi Result.failure(Exception("No has iniciado sesión."))
        val respuesta = api.listar(auth)
        if (!respuesta.isSuccessful) {
            return@llamarApi Result.failure(Exception(ApiClient.mensajeDeError(respuesta)))
        }
        Result.success(respuesta.body()?.partidos ?: emptyList())
    }

    suspend fun apuntarse(partidoId: String): Result<Unit> = llamarApi {
        val auth = autorizacion() ?: return@llamarApi Result.failure(Exception("No has iniciado sesión."))
        val respuesta = api.apuntarse(auth, partidoId)
        if (!respuesta.isSuccessful) {
            Result.failure(Exception(ApiClient.mensajeDeError(respuesta)))
        } else {
            Result.success(Unit)
        }
    }

    suspend fun cancelar(partidoId: String): Result<Unit> = llamarApi {
        val auth = autorizacion() ?: return@llamarApi Result.failure(Exception("No has iniciado sesión."))
        val respuesta = api.cancelar(auth, partidoId)
        if (!respuesta.isSuccessful) {
            Result.failure(Exception(ApiClient.mensajeDeError(respuesta)))
        } else {
            Result.success(Unit)
        }
    }
}
