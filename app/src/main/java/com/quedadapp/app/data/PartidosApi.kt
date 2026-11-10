package com.quedadapp.app.data

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PartidosApi {
    @GET("api/v1/partidos")
    suspend fun listar(@Header("Authorization") autorizacion: String): Response<PartidosResponse>

    @POST("api/v1/partidos/{id}/asistencia")
    suspend fun apuntarse(@Header("Authorization") autorizacion: String, @Path("id") id: String): Response<Unit>

    @DELETE("api/v1/partidos/{id}/asistencia")
    suspend fun cancelar(@Header("Authorization") autorizacion: String, @Path("id") id: String): Response<Unit>
}
