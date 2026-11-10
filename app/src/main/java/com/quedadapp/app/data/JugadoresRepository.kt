package com.quedadapp.app.data

class JugadoresRepository(private val tokenStore: TokenStore) {
    private val api = ApiClient.jugadoresApi

    private fun autorizacion(): String? = tokenStore.leerToken()?.let { "Bearer $it" }

    // Para que la pantalla pueda pedirle a Coil la foto con la misma
    // cabecera Authorization que el resto de peticiones (Coil no
    // comparte el OkHttpClient de Retrofit).
    fun tokenActual(): String? = tokenStore.leerToken()

    suspend fun listar(): Result<List<Jugador>> = llamarApi {
        val auth = autorizacion() ?: return@llamarApi Result.failure(Exception("No has iniciado sesión."))
        val respuesta = api.listar(auth)
        if (!respuesta.isSuccessful) {
            return@llamarApi Result.failure(Exception(ApiClient.mensajeDeError(respuesta)))
        }
        Result.success(respuesta.body()?.jugadores ?: emptyList())
    }

    suspend fun valorar(valoradoId: String, estrellas: Int): Result<Unit> = llamarApi {
        val auth = autorizacion() ?: return@llamarApi Result.failure(Exception("No has iniciado sesión."))
        val respuesta = api.valorar(auth, ValorarRequest(valoradoId, estrellas))
        if (!respuesta.isSuccessful) {
            Result.failure(Exception(ApiClient.mensajeDeError(respuesta)))
        } else {
            Result.success(Unit)
        }
    }
}
