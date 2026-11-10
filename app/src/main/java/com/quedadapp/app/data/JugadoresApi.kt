package com.quedadapp.app.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface JugadoresApi {
    @GET("api/v1/jugadores")
    suspend fun listar(@Header("Authorization") autorizacion: String): Response<JugadoresResponse>

    @POST("api/v1/jugadores/valorar")
    suspend fun valorar(@Header("Authorization") autorizacion: String, @Body body: ValorarRequest): Response<Unit>
}
