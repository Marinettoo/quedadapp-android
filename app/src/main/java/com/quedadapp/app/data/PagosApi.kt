package com.quedadapp.app.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PagosApi {
    @GET("api/v1/partidos/{id}/pagos")
    suspend fun pagos(@Header("Authorization") autorizacion: String, @Path("id") id: String): Response<PagosResponse>

    @POST("api/v1/partidos/{id}/pagos/indicar")
    suspend fun indicarPago(@Header("Authorization") autorizacion: String, @Path("id") id: String): Response<Unit>

    @DELETE("api/v1/partidos/{id}/pagos/indicar")
    suspend fun deshacerIndicacion(@Header("Authorization") autorizacion: String, @Path("id") id: String): Response<Unit>

    @POST("api/v1/partidos/{id}/pagos/confirmar")
    suspend fun confirmarPago(
        @Header("Authorization") autorizacion: String,
        @Path("id") id: String,
        @Body body: ConfirmarPagoRequest,
    ): Response<Unit>

    @DELETE("api/v1/partidos/{id}/pagos/confirmar")
    suspend fun corregirConfirmacion(
        @Header("Authorization") autorizacion: String,
        @Path("id") id: String,
        @Query("jugadorId") jugadorId: String,
        @Query("tipo") tipo: String,
    ): Response<Unit>
}
